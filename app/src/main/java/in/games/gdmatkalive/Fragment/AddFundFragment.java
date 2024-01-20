package in.games.gdmatkalive.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.razorpay.PaymentResultListener;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.wangsun.upi.payment.UpiPayment;
import com.wangsun.upi.payment.model.PaymentDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Activity.PaymentActivity;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Interfaces.GetAppSettingData;
import in.games.gdmatkalive.Model.IndexResponse;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_GET_GATEWAY;
import static in.games.gdmatkalive.Config.BaseUrls.URL_OrderId;
import static in.games.gdmatkalive.Config.BaseUrls.URL_REQUEST;
import static in.games.gdmatkalive.Config.Constants.KEY_EMAIL;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.Constants.KEY_MOBILE;
import static in.games.gdmatkalive.Config.Constants.KEY_NAME;

public class AddFundFragment extends Fragment implements View.OnClickListener, PaymentResultListener, PaymentStatusListener {
SwipeRefreshLayout swipe;
CircleImageView civ_logo;
TextView tv_message,tv_whatsapp,tv_wallet;
EditText et_points;
LinearLayout lin_whatsapp;
Button btn_add;
SessionMangement sessionMangement;
Module module;
String pnts,wallet_amt="",withdr_no="",minAmount="";
LoadingBar loadingBar;
String themeColor,imageUrl,requestStatus,gatewayStatus,upi="",upi_name="",online_payment_mode="";
RadioGroup rd_group;
RadioButton rb_phonepe,rb_other,rb_gpay,rb_paytm;
String radio_upipay="",transactionId="",phonepay_val="",order_val="";
public static String s_amount="",s_transaction_id="",razorpay_email="",razor_pay="",desc="";
ArrayList<String> newList = new ArrayList<String>();
private EasyUpiPayment mEasyUpiPayment;
boolean upi_flag=false;

    public AddFundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_fund, container, false);
        ((MainActivity)getActivity()).setTitles("Add Fund");
        wallet_amt = ((MainActivity)getActivity()).getWallet();
        initView(root);
        getGatewaySetting();
        return root;
    }

    private void initView(View root) {

        loadingBar = new LoadingBar(getActivity());
        module = new Module(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        swipe = root.findViewById(R.id.swipe);
        civ_logo = root.findViewById(R.id.civ_logo);
        tv_message = root.findViewById(R.id.tv_message);
        tv_whatsapp = root.findViewById(R.id.tv_whatsapp);
        tv_wallet = root.findViewById(R.id.tv_wallet);
        et_points = root.findViewById(R.id.et_points);
        lin_whatsapp = root.findViewById(R.id.lin_whatsapp);
        btn_add = root.findViewById(R.id.btn_add);
        rb_phonepe = root.findViewById(R.id.rb_phonepe);
        rb_other = root.findViewById(R.id.rb_other);
        rb_paytm = root.findViewById(R.id.rb_paytm);
        rb_gpay = root.findViewById(R.id.rb_gpay);
        rd_group = root.findViewById(R.id.rd_group);

        btn_add.setOnClickListener(this);
        lin_whatsapp.setOnClickListener(this);
        tv_wallet.setText(wallet_amt);
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                tv_whatsapp.setText(model.getWithdraw_no());
                withdr_no =model.getWithdraw_no();
                minAmount = model.getMin_amount();
            }
        });

        installedApps();
        rd_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("fgchbjn", "onCheckedChanged: " + rd_group);

                switch (checkedId) {

                    case R.id.rb_phonepe:
                        radio_upipay = "PHONE_PE";
                        break;

                    case R.id.rb_other:
//                        rb_phonepe.setClickable(false);
//                        rb_gpay.setClickable(false);
                        radio_upipay = "OTHER";
                        break;

                    case R.id.rb_gpay:
                        radio_upipay = "GOOGLE_PAY";
                        break;

                    case R.id.rb_paytm:
                        radio_upipay = "PAYTM";
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_add:
                if(TextUtils.isEmpty(et_points.getText().toString()))
                {
                    et_points.setError("Enter Some Points");
                    return;
                }
                else if((Integer.parseInt(et_points.getText().toString().trim()))<(Integer.parseInt(minAmount)))
                    {
                        module.fieldRequired("Minimum Range for points is "+ minAmount);
                    }
                else {
                getOrderId();
                }
                break;
            case R.id.lin_whatsapp:
                module.whatsapp(withdr_no,"Hello! Admin ");
                break;
        }
    }

    private void onValidatingData( String orderid){

        int points=0;

            if(TextUtils.isEmpty(et_points.getText().toString()))
            {
                et_points.setError("Enter Some Points");
                return;
            }
            else
            {
                points=Integer.parseInt(et_points.getText().toString().trim());

                if(points<Integer.parseInt(minAmount))
                {
                    module.fieldRequired("Minimum Range for points is "+ minAmount);
                }
                else
                {
                    String user_id = sessionMangement.getUserDetails().get(KEY_ID);
                    pnts=String.valueOf(points);
                    if (ConnectivityReceiver.isConnected()) {

                        String p = String.valueOf(points);
                        String st = "pending";
                        transactionId = "TXN" + System.currentTimeMillis();
                        String payeeVpa = upi;
                        String payeeName = upi_name;
                        String transactionRefId = transactionId;
                        String description = desc;
                        String amount = p.toString() + ".00";
                        s_amount=p;
                        s_transaction_id=transactionRefId;

                        if(gatewayStatus.equals("0")){
                            saveInfoIntoDatabase(user_id, pnts, requestStatus, "Add","");
                        }else if(gatewayStatus.equals("1")){
                            if (online_payment_mode.equals("0")){
                             //razor pay
                                if(module.checkNull (orderid).equalsIgnoreCase (""))
                                {
                                    module.errorToast ("Something went Wrong");
                                }
                                else {
                                    Intent intents = new Intent(getActivity(), PaymentActivity.class);
                                    intents.putExtra("email", sessionMangement.getUserDetails().get(KEY_EMAIL));
                                    intents.putExtra("mob", sessionMangement.getUserDetails().get(KEY_MOBILE));
                                    intents.putExtra("code", "");
                                    intents.putExtra("order_id_value", orderid);
                                    startActivity(intents);
                                }

                            }
                            else {
                                try {
                                    if (radio_upipay.equals("")){
                                        module.showToast("Please select payment type");
                                    }
//                                    else if (radio_upipay.equalsIgnoreCase("OTHER")) {
//                                        try {
//                                            newList.clear();
//                                            newList.addAll(UpiPayment.getExistingUpiApps(getActivity()));
//                                            newList.remove(newList.indexOf("phonepe"));
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        startUpiPayment(transactionId, payeeVpa, payeeName, transactionRefId, description, amount);
//
//                                    }
                                    else {
                                        payViaUpi(transactionId, payeeVpa, payeeName, transactionRefId, description, amount);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else
                    {
                       module.noInternet();
                    }
                }
            }
        }
    private void saveInfoIntoDatabase(final String user_id, final String points, final String st,String type,String trans_id) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();

        params.put("user_id",user_id);
        params.put("points",points);
        params.put("request_status",st);
        params.put("type",type);
        params.put("trans_id",trans_id);
        params.put("w_type","");
        params.put ("upi_name",radio_upipay);

        if(radio_upipay.equalsIgnoreCase ("PHONE_PE"))
        {
            params.put("request_status","pending");
        }else {
            params.put("request_status",st);
        }

        Log.e("TAG", "saveInfoIntoDatabase: "+params.toString() );
        module.postRequest(URL_REQUEST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("add_fund",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean resp=obj.getBoolean("responce");
                    if(resp)
                    {
                        module.successToast(""+obj.getString("message"));
                        loadingBar.dismiss();
                        Intent i = new Intent(getActivity(),MainActivity.class);
                        startActivity(i);
                    } else {
                        module.errorToast(""+obj.getString("error"));
                        loadingBar.dismiss();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
      }
    public void getGatewaySetting(){
        HashMap<String,String> params=new HashMap<>();

        module.postRequest(URL_GET_GATEWAY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("gatway",response.toString());
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject object = arr.getJSONObject(0);
                    imageUrl=object.getString("icon");
                    themeColor=object.getString("theme_color");
                    desc=object.getString("description");
                    requestStatus=object.getString("request_status");
                    gatewayStatus=object.getString("gateway_status");//upi status
//                    gatewayStatus="1";
                    upi = object.getString("upi_id");
                    upi_name = object.getString("upi_name");
                    online_payment_mode = object.getString("online_payment_mode");//0-razorpay//1-upi
//                    online_payment_mode = "0";


                    razor_pay = object.getString("razor_pay_id");
                    razorpay_email = object.getString("email_id");

                    Log.e("fghjk",gatewayStatus);

                    if(gatewayStatus.equals("0")){
                        rd_group.setVisibility(View.GONE);
                    }else if(gatewayStatus.equals("1")){
                        if (online_payment_mode.equals("0")){
                            rd_group.setVisibility(View.GONE);
                        }
                        else {
                            rd_group.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
            }
        });
        }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("TAG", "onPaymentSuccess: "+s.toString() );
//        common.showToast("Payment Success");
        saveInfoIntoDatabase(sessionMangement.getUserDetails().get(KEY_ID),pnts,requestStatus,"add","");

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "onPaymentError: "+s );
        module.errorToast("Payment Failed. Try again later");
    }
    private void startUpiPayment(String transactionId, String payeeVpa, String payeeName, String transactionRefId, String description, String amount) {
        //and pass this to: setUpiApps(newList): or setUpiApps(appList):
        Log.e("TAG", "startUpiPayment: "+payeeVpa+" :: "+payeeName+" :: "+transactionRefId+" :: "+description+" :: "+amount );

        PaymentDetail payment = new PaymentDetail(payeeVpa,payeeName,
                "","", description,String.valueOf(Double.parseDouble(amount)));

        new UpiPayment(getActivity())
                .setPaymentDetail(payment)
//                .setUpiApps(newList.remove(newList.indexOf("phonepe")))
                .setUpiApps(newList)
                .setCallBackListener(new UpiPayment.OnUpiPaymentListener() {
                    @Override
                    public void onSuccess(@NonNull com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Log.e("success", String.valueOf(transactionDetails));
                        Toast.makeText(getActivity(), "transaction sucess: " + transactionDetails, Toast.LENGTH_LONG).show();
                        if(transactionDetails.getStatus().equalsIgnoreCase("success"))
                        {
                            saveInfoIntoDatabase(sessionMangement.getUserDetails().get(KEY_ID),pnts,requestStatus,"add",transactionDetails.getTransactionId().toString());
                        }
                        else
                        {
                            module.showToast("Payment Failed.");
                        }

                    }

                    @Override
                    public void onSubmitted(@NonNull com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Log.e("onSubmitted", String.valueOf(transactionDetails));
                        Toast.makeText(getActivity(), "transaction pending: " + transactionDetails, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError( String message) {
                        Log.e("error", String.valueOf(message));
                        Toast.makeText(getActivity(), "transaction failed: " + message, Toast.LENGTH_LONG).show();
                    }


                }).pay();
    }
    private void payViaUpi(String transactionId, String payeeVpa, String payeeName, String transactionRefId, String description, String amount) {
        // START PAYMENT INITIALIZATION
//        Q004367838@ybl
        Log.e("TAG", "payViaUpi: "+payeeVpa+" :: "+payeeName+" :: "+transactionRefId+" :: "+description+" :: "+amount );
        upi_flag=true;
        mEasyUpiPayment = new EasyUpiPayment.Builder()
                .with(getActivity())
                .setPayeeVpa(payeeVpa)
                .setPayeeName(payeeName)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionRefId)
                .setDescription(description)
                .setAmount(amount)
                .build();

        // Register Listener for Events
        mEasyUpiPayment.setPaymentStatusListener(this);


        switch (radio_upipay) {
            case "OTHER":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);
                break;
            case "None":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);
                break;
            case "AMAZON_PAY":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.AMAZON_PAY);
                break;
            case "BHIM_UPI":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.BHIM_UPI);
                break;
            case "GOOGLE_PAY":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.GOOGLE_PAY);
                break;
            case "PHONE_PE":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PHONE_PE);
                break;
            case "PAYTM":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PAYTM);
                break;

        }

//        mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);

        // Check if app exists or not
        if (mEasyUpiPayment.isDefaultAppExist()) {
            onAppNotFound();
            return;
        }
        // END INITIALIZATION

        // START PAYMENT
        mEasyUpiPayment.startPayment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(upi_flag){
            try {
                mEasyUpiPayment.detachListener();
            }catch (Exception ex){
                module.showToast(String.valueOf(ex.toString()));
                ex.printStackTrace();
            }
        }
    }
    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Log.e("transactionDetails",""+transactionDetails);
        if(transactionDetails.getStatus().equalsIgnoreCase("success"))
        {
            saveInfoIntoDatabase(sessionMangement.getUserDetails().get(KEY_ID),pnts,requestStatus,"add",transactionDetails.getTransactionId().toString());
        }
        else
        {
            module.showToast("Payment Failed.");
        }
    }

    @Override
    public void onTransactionSuccess() {

    }

    @Override
    public void onTransactionSubmitted() {

    }

    @Override
    public void onTransactionFailed() {
        module.showToast("Failed");
    }

    @Override
    public void onTransactionCancelled() {
        module.showToast("Cancelled");
    }

    @Override
    public void onAppNotFound() {
        module.showToast("App Not Found");
    }



    public void installedApps()
    {
        List<PackageInfo> packList = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
//                appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("Appname", packInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
                if(packInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString().equalsIgnoreCase ("PhonePe"))
                {
                    phonepay_val="true";
                }

//                Log.e ("allval", "installedApps: "+paytm_val+"-"+phonepay_val+Gpay_val );

            }

        }
//uncomment
        if(phonepay_val.equalsIgnoreCase ("true"))
        {
            rb_phonepe.setVisibility (View.VISIBLE);
        }
        else
        {
            rb_phonepe.setVisibility (View.GONE);
        }
    }

    public void getOrderId() {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",sessionMangement.getUserDetails ().get (KEY_ID));
        Log.e("sendRequest", "saveInfoIntoDatabase: "+params.toString() );
        module.postRequest(URL_OrderId, params, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                loadingBar.dismiss ();
                Log.e("oder_id_valu",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean resp=obj.getBoolean("responce");
                    if(resp)
                    {
                        order_val =obj.getString ("message");
                        onValidatingData(order_val);
                    }
                    else
                    {
                        module.errorToast(""+obj.getString("message"));
                        loadingBar.dismiss();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
        // return order_val;
    }


}