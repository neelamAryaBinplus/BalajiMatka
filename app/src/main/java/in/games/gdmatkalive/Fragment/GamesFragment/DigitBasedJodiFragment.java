package in.games.gdmatkalive.Fragment.GamesFragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.TableAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.TableModel;
import in.games.gdmatkalive.R;


public class DigitBasedJodiFragment extends Fragment implements View.OnClickListener {
LinearLayout lin_selectDate;
TextView tv_date;
EditText et_lDigit,et_rDigit,et_points;
Button btn_add,btn_submit;
Module module;
String w_amount="";
TextView tv;
ListView list_table;
TableAdapter tableAdaper;
List<TableModel> list;
Dialog dialog;
TextView tv_date1,tv_date2,tv_date3,txtDate_id;
private String matka_id,e_time,s_time ,matka_name , game_id , game_name ,type = "open" ,game_date="",title="";


 public DigitBasedJodiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_digit_based_jodi, container, false);

        initView(view);
        int  m_id = Integer.parseInt (matka_id);
        if (m_id>20)
        {


            ((MainActivity) getActivity ( )).setTitles ("STARLINE");

            tv_date.setVisibility(View.GONE);

            try {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");

                String cur_date = sdf.format(date);
                tv_date.setText(cur_date);
                if (module.getTimeDifference(s_time) > 0) {


                } else {

                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {

            module.getCurrentDate(tv_date);

            matka_name = getArguments().getString("matka_name");
        }

        return view;
    }

    private void initView(View view) {
        w_amount = ((MainActivity)getActivity()).getWallet();
        module = new Module(getActivity());
        list=new ArrayList<>();
        matka_name = getArguments().getString("matka_name");
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        title = getArguments().getString("title");

        list_table=view.findViewById(R.id.list_table);
      //  lin_selectDate = view.findViewById(R.id.lin_selectDate);
        tv_date = view.findViewById(R.id.tv_date);
        et_lDigit = view.findViewById(R.id.et_lDigit);
        et_rDigit = view.findViewById(R.id.et_rDigit);
        et_points = view.findViewById(R.id.et_points);
        btn_add = view.findViewById(R.id.btn_add);
        btn_submit = view.findViewById(R.id.btn_submit);

        btn_add.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_date.setOnClickListener(this);
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
            case R.id.btn_submit:
                placedBid();
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }


    private void onValidation() {
        String lDigit = et_lDigit.getText().toString();
        String rDigit = et_rDigit.getText().toString();
        String points = et_points.getText().toString();
        game_date = tv_date.getText().toString();

            if(game_date.equalsIgnoreCase ("Select Date"))
            {
                module.fieldRequired ("Date Required");
            }else
         if (TextUtils.isEmpty(lDigit)) {
            et_lDigit.setError("Please enter digit");
            et_lDigit.requestFocus();
            return;
        }  else if (TextUtils.isEmpty(rDigit)) {
            et_rDigit.setError("Please enter digit");
            et_rDigit.requestFocus();
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

                int num = 1;
                for (int n = 0; n < list.size(); n++) {
                    num=num+1;
                }
                String number=String.valueOf(num);
                module.addData(number, game_name, lDigit+"-"+rDigit, points, "close", list, tableAdaper, list_table, btn_submit);
                clearData();
                et_lDigit.requestFocus();
            }
        }
    }
    private void clearData() {
        et_rDigit.setText ("");
        et_lDigit.setText("");
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
                ArrayList list_digits = new ArrayList();
                ArrayList list_type = new ArrayList();
                ArrayList list_points = new ArrayList();
                int rows = list.size();

                for (int i = 0; i < rows; i++) {


                    TableModel tableModel = list.get(i);
                    String asd = tableModel.getDigits();
//                    String d_all[] = asd.split("-");
//                    String d0 = d_all[0].toString();
//                    String d1 = d_all[1].toString();

                    String asd1 = tableModel.getPoints().toString();

                    amt = amt + Integer.parseInt(asd1);

                }

                int wallet_amount = Integer.parseInt(w_amount);
                if (wallet_amount < amt) {
                    module.errorToast("Insufficient Amount");
                } else {
//                    module.setBidsDialog(Integer.parseInt(w_amount),list,matka_id,"10","10",w_amount,"matka_name",btn_submit, "s_time", "e_time");

                        module.setBidsDialog(Integer.parseInt(w_amount),list,matka_id,game_date.substring(0,10),game_id,w_amount,matka_name,btn_submit, s_time, e_time);
                }} catch (Exception err) {
                err.printStackTrace();
                module.errorToast("Err" + err.getMessage());
            }

        }
    }
}