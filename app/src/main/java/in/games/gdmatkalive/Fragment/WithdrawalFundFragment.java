package in.games.gdmatkalive.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Config.BaseUrls;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Interfaces.GetAppSettingData;
import in.games.gdmatkalive.Model.IndexResponse;
import in.games.gdmatkalive.Model.TimeSlots;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_REQUEST;
import static in.games.gdmatkalive.Config.Constants.KEY_ACCOUNNO;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.Constants.KEY_IFSC;
import static in.games.gdmatkalive.Config.Constants.KEY_PAYTM;
import static in.games.gdmatkalive.Config.Constants.KEY_PHONEPAY;
import static in.games.gdmatkalive.Config.Constants.KEY_TEZ;
import static in.games.gdmatkalive.Config.Constants.KEY_WALLET;

public class WithdrawalFundFragment extends Fragment implements View.OnClickListener {
    SwipeRefreshLayout swipe;
    CircleImageView civ_logo;
    TextView tv_message,tv_whatsapp,tv_wallet;
    EditText et_points;
    LinearLayout lin_whatsapp;
    Button btn_add;
    String wallet_amt="",bank_type="",withdraw_no="";
    Module module;
    SessionMangement sessionMangement;
    LoadingBar loadingBar;
    int wMinAmt=0;
    int wSaturday=0,wSunday=0;
    ArrayList<TimeSlots> timeList;

    public WithdrawalFundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_withdrawal_fund, container, false);
        ((MainActivity)getActivity()).setTitles("Redeem Fund");
        wallet_amt = ((MainActivity)getActivity()).getWallet();
        initView(root);
        getTimeSlots();

        return root;
    }
    private void initView(View root) {

        loadingBar = new LoadingBar(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        module = new Module(getActivity());
        swipe = root.findViewById(R.id.swipe);
        civ_logo = root.findViewById(R.id.civ_logo);
        tv_message = root.findViewById(R.id.tv_message);
        tv_whatsapp = root.findViewById(R.id.tv_whatsapp);
        tv_wallet = root.findViewById(R.id.tv_wallet);
        et_points = root.findViewById(R.id.et_points);
        lin_whatsapp = root.findViewById(R.id.lin_whatsapp);
        btn_add = root.findViewById(R.id.btn_add);
        timeList = new ArrayList<>();
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                tv_message.setText(model.getWithdraw_text());
                withdraw_no = model.getWithdraw_no();
                tv_whatsapp.setText(withdraw_no);
            }
        });


        btn_add.setOnClickListener(this);
        lin_whatsapp.setOnClickListener(this);
        tv_wallet.setText(wallet_amt);


        String tez = sessionMangement.getUserDetails().get(KEY_TEZ);
        String phonePay = sessionMangement.getUserDetails().get(KEY_PHONEPAY);
        String accountno = sessionMangement.getUserDetails().get(KEY_ACCOUNNO);
        String paytm = sessionMangement.getUserDetails().get(KEY_PAYTM);
        String ifsc = sessionMangement.getUserDetails().get(KEY_IFSC);

        Log.e("check_deatail",tez+"\n"+phonePay+"\n"+accountno+"\n"+paytm+"\n"+ifsc);


//        if (!tez.equals("")|| !tez.equalsIgnoreCase("null"))
//        {
//            bank_type="Google Pay";
//
//        }else if (!phonePay.equals("")|| !phonePay.equalsIgnoreCase("null"))
//        {
//            bank_type="PhonePe";
//        }else if (!paytm.equals("")|| !paytm.equalsIgnoreCase("null"))
//        {
//            bank_type = "paytm";
//        }else if (!ifsc.equals("")||!ifsc.equalsIgnoreCase("null") && !accountno.equals("")||!accountno.equalsIgnoreCase("null"))
//        {
//            bank_type = "Bank";
//        }else {
//            module.fieldRequired("Please fill any of your payment details to proceed!");
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_add:
               onValidation();
            break;
            case R.id.lin_whatsapp:
                module.whatsapp(withdraw_no.toString(),"Hello! Admin ");
                break;
        }
    }

    private void onValidation() {

                String points=et_points.getText().toString().trim();

                if(points.isEmpty()) {
                    et_points.setError("Enter Some Points");
                }
                else
                {

                    String user_id = sessionMangement.getUserDetails().get(KEY_ID);
                    String pnts = String.valueOf(points);
                    String st = "pending";
                    int w_amt = Integer.parseInt(wallet_amt);
                    int t_amt = Integer.parseInt(pnts);

                    if (w_amt > 0) {

                        if(t_amt<wMinAmt)
                        {
                            module.fieldRequired("Minimum Withdrawal amount "+wMinAmt+".");
                        }
                        else
                        {

                            if (t_amt > w_amt) {

                                module.fieldRequired("Your requested amount exceeded");
                                return;
                            } else {

                                int flg=0;
                                if(getCurrentDay().equalsIgnoreCase("Sunday"))
                                {
                                    if(wSunday==1){
                                        flg=1;
                                    }
                                    else{
                                        flg=2;
                                    }
                                }
                                else if(getCurrentDay().equalsIgnoreCase("Saturday"))
                                {
                                    if(wSaturday==1){
                                        flg=3;
                                    }
                                    else{
                                        flg=4;
                                    }
                                }
                                else
                                {
                                    flg=5;
                                }
                                if(flg==1 || flg==3 || flg==5){
                                    if(getStartTimeOutStatus(timeList) || getEndTimOutStatus(timeList))
                                    {
                                        getwithdrawAmount(user_id,st,"Withdraw",bank_type, String.valueOf(t_amt));
                                    }
                                    else
                                    {
                                        module.customToast("Timeout");
                                    }
                                }
                                else if(flg==2 || flg==4)
                                {
                                    module.errorToast("Withdrawal Request is not allowed on "+getCurrentDay());
                                }
                            }
                        }

                    } else {
                        module.fieldRequired("You don't have enough points in wallet ");
                    }

                }
    }


    public boolean getStartTimeOutStatus(ArrayList<TimeSlots> list){
        int j=0;
        boolean flag=false;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
        SimpleDateFormat spdfMin=new SimpleDateFormat("mm");
        Date c_date=new Date();
        int chours=Integer.parseInt(simpleDateFormat.format(c_date));
        int cMins=Integer.parseInt(spdfMin.format(c_date));
        for(int i=0; i<list.size();i++){
            int shours=Integer.parseInt(list.get(i).getStart_time().split(":")[0].toString());
            int sMins=Integer.parseInt(list.get(i).getStart_time().split(":")[1].toString());
            if(chours>shours)
            {j=1;
                flag=true;
                break;
            }
            else if(chours == shours)
            {
                if(cMins<=sMins)
                {
                    j=2;
                    flag=true;
                    break;
                }
                else{
                    j=3;
                    flag=false;
                    break;
                }
            }
        }

        return flag;

    }
    public boolean getEndTimOutStatus(ArrayList<TimeSlots> list){
        int j=0;
        boolean flag=false;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
        SimpleDateFormat spdfMin=new SimpleDateFormat("mm");
        Date c_date=new Date();
        int chours=Integer.parseInt(simpleDateFormat.format(c_date));
        int cMins=Integer.parseInt(spdfMin.format(c_date));
        for(int i=0; i<list.size();i++){
            int ehours=Integer.parseInt(list.get(i).getEnd_time().split(":")[0].toString());
            int eMins=Integer.parseInt(list.get(i).getEnd_time().split(":")[1].toString());
            if(chours<ehours)
            {j=1;
                flag=true;
                break;
            }
            else if(chours == ehours)
            {
                if(cMins<=eMins)
                {
                    j=4;
                    flag=true;
                    break;
                }
                else{
                    j=5;
                    flag=false;
                    break;
                }
            }
        }
        return flag;
    }
    public String getCurrentDay()
    {
        Date date=new Date();
        SimpleDateFormat smdf=new SimpleDateFormat("EEEE");
        String day=smdf.format(date);
        return day;
    }

    public void getwithdrawAmount(String user_id,String st, String type, String bank_type,String points)
    {
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        String dt=dateFormat.format(date);
        int wl = Integer.parseInt(sessionMangement.getUserDetails().get(KEY_WALLET));
        final String wal = String.valueOf(wl - Integer.parseInt(points));
        loadingBar.show();
        HashMap<String,String> params=new HashMap<String, String>();

        params.put("user_id", user_id);
        params.put("points", points);
        params.put("request_status", st);
        params.put("type", "Withdraw");
        params.put("wallet", wal);
        params.put("trans_id", "");
        params.put("w_type","w_type");
        params.put("date",dt);
        params.put("bank_type","bank");
        Log.e("asdasd",""+params.toString());

        module.postRequest(URL_REQUEST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("withdrawal_request", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    boolean resp = object.getBoolean("responce");
                    if (resp) {
                        loadingBar.dismiss();
                        module.successToast("Request Added");
                        Intent i = new Intent(getActivity(),MainActivity.class);
                        startActivity(i);
                    }else {
                        module.errorToast(object.getString("error"));
                        loadingBar.dismiss();
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }
    public void getTimeSlots(){
        loadingBar.show();
        timeList.clear();
        HashMap<String,String> params=new HashMap<>();
        module.postRequest(BaseUrls.URL_TIME_SLOTS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                Log.e("timeslot",resp.toString());

                loadingBar.dismiss();
                try {
                    JSONObject response=new JSONObject(resp);
                    if(response.getBoolean("responce")){
                        Gson gson=new Gson();
                        Type typeList=new TypeToken<List<TimeSlots>>(){}.getType();
                        timeList=gson.fromJson(response.getString("data").toString(),typeList);

                        wMinAmt=Integer.parseInt(response.getString("min_amount"));
                        wSaturday=Integer.parseInt(response.getString("w_saturday").toString());
                        wSunday=Integer.parseInt(response.getString("w_sunday").toString());

                    }else{
                        module.errorToast("Something Went Wrong");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }
}