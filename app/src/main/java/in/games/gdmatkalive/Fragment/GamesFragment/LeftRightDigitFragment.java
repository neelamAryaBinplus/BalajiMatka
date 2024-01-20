package in.games.gdmatkalive.Fragment.GamesFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.TableAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.TableModel;
import in.games.gdmatkalive.R;

import static in.games.gdmatkalive.Config.list_input_data.group_jodi_digits;
import static in.games.gdmatkalive.Config.list_input_data.single_digit;

public class LeftRightDigitFragment extends Fragment implements View.OnClickListener {
    LinearLayout lin_selectDate;
    TextView tv_date,tv_type,tv_digit,tv_jodi,tv_date1,tv_date2,tv_date3,txtDate_id;;
    EditText et_points;
    AutoCompleteTextView et_digit;
    Button btn_add,btn_submit;
    Module module;
    String name,betdate,w_amount;
    Dialog dialog;
    ListView list_table;
    TableAdapter tableAdaper;
    List<TableModel> list;
    List<String> digitlist;
    private String matka_id,e_time,s_time ,matka_name , game_id , game_name ,type = "open" ,game_date="",title="",bet_type="";

    public LeftRightDigitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_left_right_digit, container, false);
        initView(view);

        w_amount = ((MainActivity) getActivity()).getWallet();

        Bundle bundle = getArguments ();
        name= bundle.getString ("game_name");
        if(name.equalsIgnoreCase ("LEFT")){
            bet_type = "open";
            tv_digit.setVisibility (View.VISIBLE);
            tv_jodi.setVisibility (View.GONE);
            et_digit.setFilters (new InputFilter[]{new InputFilter.LengthFilter (1)});
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,single_digit);
            et_digit.setAdapter(adapter2);
            digitlist= Arrays.asList (single_digit);
        }
        else if(name.equalsIgnoreCase ("RIGHT")){
            bet_type="close";
            tv_digit.setVisibility (View.VISIBLE);
            tv_jodi.setVisibility (View.GONE);
            et_digit.setFilters (new InputFilter[]{new InputFilter.LengthFilter (1)});
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,single_digit);
            et_digit.setAdapter(adapter2);
            digitlist= Arrays.asList (single_digit);
        }
       else if(name.equalsIgnoreCase ("JODI")){
            bet_type="close";
            tv_jodi.setVisibility (View.VISIBLE);
            tv_digit.setVisibility (View.GONE);
            et_digit.setFilters (new InputFilter[]{new InputFilter.LengthFilter (2)});
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,group_jodi_digits);
            et_digit.setAdapter(adapter2);
            digitlist= Arrays.asList (group_jodi_digits);
        }

        int  m_id = Integer.parseInt (matka_id);

            try {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
                module.getCurrentDate(tv_date);
                String cur_date = sdf.format(date);

                if (module.getTimeDifference(s_time) > 0) {

                } else {

                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        return view;
    }

    private void initView(View view) {
        matka_name = getArguments().getString("matka_name");
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        title = getArguments().getString("title");
        module = new Module(getActivity());
        //lin_selectDate = view.findViewById(R.id.lin_selectDate);
        tv_date = view.findViewById(R.id.tv_date);
       // tv_type = view.findViewById(R.id.tv_type);

        et_points = view.findViewById(R.id.et_points);
        btn_add = view.findViewById(R.id.btn_add);
        btn_submit = view.findViewById(R.id.btn_submit);
        et_digit = view.findViewById(R.id.et_digit);

        //tv_date.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_digit= view.findViewById(R.id.tv_digit);
        tv_jodi= view.findViewById(R.id.tv_jodi);

        list=new ArrayList<> ();
        digitlist=new ArrayList<> ();
        module=new Module (getContext ());
        list_table=view.findViewById(R.id.list_table);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId())
        {
            case R.id.btn_add:
                addValidation();
                break;
            case R.id.tv_date:
                module.setDateDialog (dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
                break;
            case R.id.btn_submit:
           submitData();
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    private void submitData() {
        int er = list.size();
        if (er <= 0) {
            String message = "Please Add Some Bids";
            module.fieldRequired(message);
            return;
        } else {
            try {
                int amt = 0;
                int rows = list.size();

                for (int i = 0; i < rows; i++) {
                    TableModel tableModel = list.get(i);
                    String asd1 = tableModel.getPoints().toString();
                    amt = amt + Integer.parseInt(asd1);
                }

                int wallet_amount = Integer.parseInt(w_amount);
                if (wallet_amount < amt) {
                    module.errorToast("Insufficient Amount");
                } else {
                    module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id,betdate, game_id, w_amount, matka_name, btn_submit, s_time, e_time);
                    Log.e("my", "onClick: "+Integer.parseInt(w_amount)+""+list+ matka_id+betdate+game_id+w_amount+matka_name+s_time+e_time );
                }
            } catch (Exception err) {
                err.printStackTrace();
                module.errorToast("Err" + err.getMessage());
            }

        }
    }

    private void addValidation() {
        String digit=et_digit.getText ().toString ();
        String point=et_points.getText ().toString ();
        betdate = tv_date.getText ( ).toString ( );
        int betType;
        betType=getBetType(getASandC(s_time,e_time));
        if (betType==1)
        {
            module.errorToast ("Bidding is closed for today !");
        } else {
            if(betdate.equalsIgnoreCase ("Select Date"))
            {
                module.fieldRequired ("Date Required");
            }else if (et_digit.getText ( ).toString ( ).isEmpty ( )) {
               et_digit.setError ("Digit Required");
               et_digit.requestFocus ( );

        } else if (et_points.getText ( ).toString ( ).isEmpty ( )) {
            et_points.setError ("Point Required");
            et_points.requestFocus ( );
        } else  if(!digitlist.contains (digit)){
             et_digit.setError ("Invalid");
             et_digit.setText ("");
             et_digit.requestFocus ();
         } else {
                int points = Integer.parseInt (et_points.getText ( ).toString ( ).trim ( ));
                if (points < 10) {
                    et_points.setError ("Minimum Biding amount is 10");
                    et_points.requestFocus ( );
                    return;
                } else if (points > Integer.parseInt (w_amount)) {
                    module.fieldRequired ("Insufficient Amount");
                } else {
                    int num = 1;
                    for (int n = 0; n < list.size ( ); n++) {
                        num = num + 1;
                    }
                    String number = String.valueOf (num);
                    module.addData (number, game_name, digit, point, bet_type, list, tableAdaper, list_table, btn_submit);
                    et_digit.requestFocus ( );
                    clearData ( );

                }
            }
        }
    }
    private void clearData() {
        et_digit.setText ("");
        et_points.setText ("");
    }
    public void getCurrentDate(TextView txt)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy EEEE");
        String cur_date = sdf.format(date);
        txt.setText(cur_date);
    }
    public long[] getASandC(String startTime,String endTime){
        long[] tArr=new long[2];
        Date date=new Date();
        java.text.SimpleDateFormat sim=new java.text.SimpleDateFormat ("HH:mm:ss");
        String time1 = startTime.toString();
        String time2 = endTime.toString();

        Date cdate=new Date();
        @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat format = new java.text.SimpleDateFormat ("HH:mm:ss");
        String time3=format.format(cdate);
        Date date1 = null;
        Date date2=null;
        Date date3=null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
            date3=format.parse(time3);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long difference = date3.getTime() - date1.getTime();
        long as=(difference/1000)/60;

        long diff_close=date3.getTime()-date2.getTime();
        long c=(diff_close/1000)/60;
        tArr[0]=as;
        tArr[1]=c;
        return tArr;
    }
    public int getBetType(long[] tArr) {
        // as<0 => open,close
        //c>0 =>nothing but biding closed
        //else=>close
        long as = tArr[0];
        long c = tArr[1];
        if (as < 0) {
            return 2;
        } else if (c > 0) {
            return 0;
        } else {
            return 1;
        }
    }
}