package in.games.gdmatkalive.Fragment.GamesFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
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


public class DigitFragment extends Fragment implements View.OnClickListener {
AutoCompleteTextView et_digit;

EditText et_point;
Dialog dialog;
ListView list_table;
TableAdapter tableAdaper;
Button btn_add,btn_submit;
List<TableModel> list;
String name,betdate,bettype,w_amount="",game_name, matka_name, matka_id ,game_id,s_time ,e_time,no,title;
Module module;
RelativeLayout rel_single,rel_jodi;
List<String> digitlist;
    String game;
    String gamedate;
TextView tv_date,tv_type,tv_open,tv_close,tv_single,tv_jodi,tv_date1,tv_date2,tv_date3,txtDate_id;

    public DigitFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_digit, container, false);
       initview(view);

        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        matka_name = getArguments().getString("matka_name");
        title = getArguments().getString("title");
       int  m_id = Integer.parseInt (matka_id);
//int min_amt= Integer.parseInt (min_add_amount);


        if (m_id>20)
        {
            ((MainActivity) getActivity ( )).setTitles (title);

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
            try {
                module.getCurrentDate(tv_date);
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
            module.getCurrentDate(tv_date);
        }



        w_amount = ((MainActivity) getActivity()).getWallet();
        final  Bundle bundle = getArguments ();
         name= bundle.getString ("type");
        game=bundle.getString ("matka_name");
         if(name.equalsIgnoreCase ("SINGLE")){

             et_digit.setFilters (new InputFilter[]{new InputFilter.LengthFilter (1)});
             ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,single_digit);
             et_digit.setAdapter(adapter1);
             digitlist= Arrays.asList (single_digit);
             tv_single.setVisibility (View.VISIBLE);
             tv_jodi.setVisibility (View.GONE);

         }



       else if(name.equals ("JODI")){

             ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,group_jodi_digits);
             et_digit.setAdapter(adapter2);
             et_digit.setFilters (new InputFilter[]{new InputFilter.LengthFilter (2)});
             digitlist= Arrays.asList (group_jodi_digits);
             tv_single.setVisibility (View.GONE);
             tv_jodi.setVisibility (View.VISIBLE);
             tv_type.setVisibility (View.INVISIBLE);


            tv_type.setText ("close");


        }
        return view;
    }
//
    private void initview(View view) {
        module=new Module (getActivity ());
        rel_single=view.findViewById (R.id.rel_single);
        et_digit=view.findViewById (R.id.et_digit);
        et_point=view.findViewById (R.id.et_point);
        btn_add=view.findViewById (R.id.btn_add);
        btn_add.setOnClickListener (this);
        btn_submit=view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        tv_date=view.findViewById (R.id.tv_date);
        tv_type=view.findViewById (R.id.tv_type);
        tv_single=view.findViewById (R.id.tv_single);
        tv_jodi=view.findViewById (R.id.tv_jodi);
        tv_type.setOnClickListener (this);
//        tv_date.setOnClickListener (this);
        list=new ArrayList<> ();
        list_table=view.findViewById(R.id.list_table);
        digitlist=new ArrayList<> ();

 gamedate=tv_date.getText ().toString ();



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
if(name.equalsIgnoreCase ("Jodi")){
    tv_type.setText ("close");
    int betType;
//
    betType=getBetType(getASandC(s_time,e_time));
    if (betType==1)
    {
        module.errorToast ("Bidding is closed for today !");
    }
}

        String digit=et_digit.getText ().toString ();
        String point=et_point.getText ().toString ();
        if(v.getId ()==R.id.btn_add){
            betdate = tv_date.getText().toString();
            bettype = tv_type.getText().toString();
            if(name.equalsIgnoreCase ("Jodi")){
                tv_type.setText ("close");
                int betType;
//
                betType=getBetType(getASandC(s_time,e_time));
                if (betType==1)
                {
                    module.errorToast ("Bidding is closed for today !");
                }
                else{
                      if(betdate.equalsIgnoreCase ("SELECT DATE"))
                    {
                        module.fieldRequired ("Please Select Date");

                    }

                    else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
                    {
                        module.fieldRequired ("Please Select Game Type");

                    }
                    else if(et_digit.getText ().toString ().isEmpty ()){
                        et_digit.setError ("Digit Required");
                        et_digit.requestFocus ();

                    }
                    else  if(et_point.getText ().toString ().isEmpty ()){
                        et_point.setError ("Point Required");
                        et_point.requestFocus ();
                    }
                    else  if(!digitlist.contains (digit)){
                        et_digit.setError ("Invalid");
                        et_digit.setText ("");
                        et_digit.requestFocus ();
                    }
                    else {
                        int points = Integer.parseInt(et_point.getText().toString().trim());
                        if (points < 10) {
                            et_point.setError("Minimum Biding amount is 10");
                            et_point.requestFocus();
                            return;


                        }
                        else if (points > Integer.parseInt(w_amount)) {
                            module.fieldRequired ("Insufficient Amount");
                        }
                        else {

                            int num=1;
                            for (int n = 0; n < list.size(); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                                num=num+1;
                            }
                            String number=String.valueOf(num);

                            module.addData(number,name,digit,point,bettype,list,tableAdaper,list_table,btn_submit);
                            et_digit.requestFocus();
                            clearData ();


                        }
                    }
                }


            }

            
            else if(betdate.equalsIgnoreCase ("SELECT DATE"))
            {
            module.fieldRequired ("Please Select Date");

            }

             else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
            {
                module.fieldRequired ("Please Select Game Type");

            }
         else if(et_digit.getText ().toString ().isEmpty ()){
         et_digit.setError ("Digit Required");
         et_digit.requestFocus ();

           }
          else  if(et_point.getText ().toString ().isEmpty ()){
            et_point.setError ("Point Required");
             et_point.requestFocus ();
            }
             else  if(!digitlist.contains (digit)){
                 et_digit.setError ("Invalid");
                 et_digit.setText ("");
                 et_digit.requestFocus ();
             }
          else {
              int points = Integer.parseInt(et_point.getText().toString().trim());
              if (points < 10) {
                  et_point.setError("Minimum Biding amount is 10");
                  et_point.requestFocus();
                  return;


              }
              else if (points > Integer.parseInt(w_amount)) {
                  module.fieldRequired ("Insufficient Amount");
              }
              else {

                  int num=1;
                  for (int n = 0; n < list.size(); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                       num=num+1;
                  }
                  String number=String.valueOf(num);

                  module.addData(number,name,digit,point,bettype,list,tableAdaper,list_table,btn_submit);
                  et_digit.requestFocus();
                      clearData ();


              }
          }

        }
        else if(v.getId ()==R.id.tv_date){
           module.setDateDialog (dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
        }
       else if(v.getId ()==R.id.tv_type){
           module.setBetTypeDialog (dialog,gamedate,tv_open,tv_close,tv_type,s_time,e_time,game_id);
        }
        else if(v.getId ()==R.id.btn_submit){
            int er = list.size();
            if (er <= 0) {
                String message = "Please Add Some Bids";
                module.fieldRequired (message);
                return;

            } else {



                try {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String cur_time = format.format(date);
                    String cur_date = sdf.format(date);

                        Log.e("true", "today");
                        Date s_date = format.parse(s_time);
                        Date e_date = format.parse(e_time);
                        Date c_date = format.parse(cur_time);
                        long difference = c_date.getTime() - s_date.getTime();
                        long as = (difference / 1000) / 60;

                        long diff_close = c_date.getTime() - e_date.getTime();
                        long curr = (diff_close / 1000) / 60;
                        long current_time = c_date.getTime();



                         if (curr < 0) {
                         module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id,betdate, game_id, w_amount, matka_name, btn_submit, s_time, e_time);
                             Log.e("my", "onClick: "+Integer.parseInt(w_amount)+""+list+ matka_id+betdate+game_id+w_amount+matka_name+s_time+e_time );
                         }else{
                             module.fieldRequired ("Biding is Closed Now");
                            }


                } catch (ParseException e) {
                    e.printStackTrace();
              }
            }
        }
    }

    private void clearData() {
        et_digit.setText ("");
        et_point.setText ("");
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
