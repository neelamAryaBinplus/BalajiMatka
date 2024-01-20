package in.games.gdmatkalive.Fragment.GamesFragment;

import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.TableAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.TableModel;
import in.games.gdmatkalive.R;

public class SpDpTpFragment extends Fragment implements View.OnClickListener {
LinearLayout lin_selectDate,lin_type;
TextView tv_type,tv_date;
EditText et_digit,et_points;
Button btn_add,btn_submit;
CheckBox cb_sp,cb_dp,cb_tp;
Module module;
int num=0;
String gamedate;
    String gameType="";
String w_amount="";
Dialog dialog;
TextView tv_date1,tv_date2,tv_date3,txtDate_id,tv_open,tv_close;
private String matka_id,e_time,s_time ,matka_name , game_id , game_name ,type = "open" ,game_date="",title="";
ListView list_table;
TableAdapter tableAdaper;
List<TableModel> list;
    public SpDpTpFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_dp_moter, container, false);
        initView(view);

        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        matka_name = getArguments().getString("matka_name");
        int  m_id = Integer.parseInt (matka_id);
//int min_amt= Integer.parseInt (min_add_amount);
        gamedate=tv_date.getText ().toString ();

        if (m_id>20)
        {


            ((MainActivity) getActivity ( )).setTitles ("STARLINE");

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
            try {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                String cur_date = sdf.format(date);
                tv_date.setText(cur_date);
                if (module.getTimeDifference(s_time) > 0) {

                    tv_type.setText("Open");
                } else {
                    tv_type.setText("Close");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {

            tv_type.setVisibility(View.VISIBLE);
            matka_name = getArguments().getString("matka_name");
        }
        return view;
    }
    private void initView(View view) {
        w_amount = ((MainActivity)getActivity()).getWallet();

//        matka_name = getArguments().getString("matka_name");
//        game_name = getArguments().getString("game_name");
//        matka_id = getArguments().getString("m_id");
//        game_id = getArguments().getString("game_id");
//        s_time = getArguments().getString("start_time");
//        e_time = getArguments().getString("end_time");
//        title = getArguments().getString("title");

        module = new Module(getActivity());
        list=new ArrayList<>();
        list_table=view.findViewById(R.id.list_table);
//        lin_selectDate = view.findViewById(R.id.lin_selectDate);
//        lin_type = view.findViewById(R.id.lin_type);
        tv_type = view.findViewById(R.id.tv_type);
        tv_date = view.findViewById(R.id.tv_date);
        et_digit = view.findViewById(R.id.et_digit);
        et_points = view.findViewById(R.id.et_points);
        btn_add = view.findViewById(R.id.btn_add);
        cb_tp = view.findViewById(R.id.cb_tp);
        cb_sp = view.findViewById(R.id.cb_sp);
        cb_dp = view.findViewById(R.id.cb_dp);
        btn_submit = view.findViewById(R.id.btn_submit);

        btn_add.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        cb_tp.setOnClickListener(this);
        cb_sp.setOnClickListener(this);
        cb_dp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId())
        {
            case R.id.btn_add:
                onValidation();
                break;
            case R.id.tv_date:
                module.setDateDialog (dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
                break;
            case R.id.tv_type:
                module.setBetTypeDialog (dialog,game_date,tv_open,tv_close,tv_type,s_time,e_time,game_id);
                break;
            case R.id.btn_submit:
                placedBid();
                break;
            case R.id.cb_sp:
                num =1;
                break;
            case R.id.cb_dp:
                num =2;
                break;
            case R.id.cb_tp:
                num =3;
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    private void onValidation() {
        String digit = et_digit.getText().toString();;
        String points = et_points.getText().toString();
        String type = tv_type.getText().toString();
         game_date = tv_date.getText().toString();

         if(game_date.equalsIgnoreCase ("Select Date"))
          {
              module.fieldRequired ("Date Required");
          }else if (type.equalsIgnoreCase("Select Game Type"))
          {
            module.fieldRequired("Select game type");
        } else if (num==0)
        {
            module.customToast("Please select alteast one check box");
        }else if (TextUtils.isEmpty(digit)) {
            et_digit.setError("Please enter digit");
            et_digit.requestFocus();
            return;
        } else if (TextUtils.isEmpty(points)) {
            et_points.setError("Please enter point");
            et_points.requestFocus();
            return;

        } else {
            int pints = Integer.parseInt(points);
            if (pints < 10) {
                et_points.setError("Minimum Biding amount is 10");
                et_points.requestFocus();
                return;

            } else if (pints > Integer.parseInt(w_amount)) {
                module.errorToast("Insufficient Amount");

            }else {
                if (num==1)
                {
                    gameType = "SP";
                }else if (num==2)
                {
                    gameType = "DP";
                }else if (num ==3)
                {
                    gameType = "TP";
                }
                int num = 1;
                for (int n = 0; n < list.size(); n++) {
                    num=num+1;
                }
                String number=String.valueOf(num);
                module.addData(number,gameType, digit, points, type, list, tableAdaper, list_table, btn_submit);
                clearData();
                et_digit.requestFocus();
            }
        }
    }
    private void clearData() {

        et_digit.setText("");
        et_points.setText("");
    }
    private void placedBid() {

        int er = list.size();
        if (er <= 0) {
            String message = "Please Add Some Bids";
            module.fieldRequired(message);
            return;
        } else {
            try {
                int amt = 0;
                for (int j = 0; j < list.size(); j++) {
                    amt = amt + Integer.parseInt(list.get(j).getPoints());
                }
                if (amt > Integer.parseInt(w_amount)) {
                    module.errorToast("Insufficient Amount");
                    clearData();
                } else {
//                try {
//                    Date date = new Date();
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//                    String cur_time = format.format(date);
//                    String cur_date = sdf.format(date);
//                    String g_d = game_date.substring(0, 10);
////                Toast.makeText(getActivity(),""+g_d,Toast.LENGTH_LONG).show();
//                    Log.e("date ", String.valueOf(g_d) + "\n" + String.valueOf(cur_date));
//
//                    if (cur_date.equals(g_d)) {
//                        Log.e("true", "today");
//                        Date s_date = format.parse(s_time);
//                        Date e_date = format.parse(e_time);
//                        Date c_date = format.parse(cur_time);
//                        long difference = c_date.getTime() - s_date.getTime();
//                        long as = (difference / 1000) / 60;
//
//                        long diff_close = c_date.getTime() - e_date.getTime();
//                        long curr = (diff_close / 1000) / 60;
//                        long current_time = c_date.getTime();
//
//                        if (as < 0) {
//
//                            module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date, game_id, w_amount, matka_name, btnSave, s_time, e_time);
//                        } else if (curr < 0) {
//                            module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date, game_id, w_amount, matka_name, progressDialog, btnSave, s_time, e_time);
//                        } else {
//                            clearData();
//                            module.marketClosed("Biding is Closed Now");
//
//                        }
//                    } else {

//                        module.setBidsDialog(Integer.parseInt(w_amount),list,"11","10","10",w_amount,"matka_name",btn_submit, "s_time", "e_time");

                    module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date.substring(0, 10), game_id, w_amount, matka_name, btn_submit, s_time, e_time);
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}