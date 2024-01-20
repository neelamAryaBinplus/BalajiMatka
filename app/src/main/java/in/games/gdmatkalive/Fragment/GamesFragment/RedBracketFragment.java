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
import android.widget.CheckBox;
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

import static in.games.gdmatkalive.Config.list_input_data.red_bracket;


public class RedBracketFragment extends Fragment implements View.OnClickListener {
    AutoCompleteTextView et_digit;
    EditText et_point;
    Dialog dialog;
    String gamedate;
    Button btn_add,btn_submit;
    CheckBox check_bid;
    RelativeLayout rel_bracket;
    String betdate,bettype,w_amount="",s_time ,e_time,matka_name ,game_id,matka_id,game_name;
    TextView tv_date,tv_type,tv_open,tv_close,tv_date1,tv_date2,tv_date3,txtDate_id;
    ListView list_table;
    TableAdapter tableAdaper;
    Module module;
    List<String> digitlist;
    List<TableModel> list;

    public RedBracketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_red_bracket, container, false);
        initview(view);

        w_amount = ((MainActivity) getActivity()).getWallet();
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,red_bracket);
        et_digit.setFilters (new InputFilter[]{new InputFilter.LengthFilter (2)});
        et_digit.setAdapter(adapter2);
        digitlist= Arrays.asList (red_bracket);
       check_bid.setOnClickListener (new View.OnClickListener ( ) {
    @Override
    public void onClick(View v) {
        if(check_bid.isChecked ()){
            rel_bracket.setVisibility (View.GONE);
        }else {
            rel_bracket.setVisibility (View.VISIBLE);
        }

    }
});


        return view;
    }

    private void initview(View view) {
        module=new Module (getContext ());
        rel_bracket=view.findViewById (R.id.rel_bracket);
        et_digit=view.findViewById (R.id.et_digit);
        et_point=view.findViewById (R.id.et_point);
        btn_add=view.findViewById (R.id.btn_add);
        btn_add.setOnClickListener (this);
        btn_submit=view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        check_bid=view.findViewById (R.id.check_bid);
        tv_date=view.findViewById (R.id.tv_date);

//        tv_date.setOnClickListener (this);
        module.getCurrentDate(tv_date);

        list=new ArrayList<> ();
        digitlist=new ArrayList<> ();
        list_table=view.findViewById(R.id.list_table);

        bettype="open";
        matka_name = getArguments().getString("matka_name");
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        String digit=et_digit.getText ().toString ();
        String point=et_point.getText ().toString ();
        if(v.getId ()==R.id.btn_add)
        {

            betdate = tv_date.getText().toString();

            int betType;
            betType=getBetType(getASandC(s_time,e_time));
            if (betType==1)
            {
                module.errorToast ("Bidding is closed for today !");
            }
            else {

                if (betdate.equalsIgnoreCase ("SELECT DATE")) {
                    module.fieldRequired ("Date Required");
                } else if (rel_bracket.getVisibility ( ) == View.GONE) {

                    if (et_point.getText ( ).toString ( ).isEmpty ( )) {
                        et_point.setError ("Point Required");
                        et_point.requestFocus ( );
                    }
//            else  if(!digitlist.contains (digit)){
//                et_digit.setError ("Invalid");
//                et_digit.setText ("");
//                et_digit.requestFocus ();
//            }
                    else {
                        int points = Integer.parseInt (et_point.getText ( ).toString ( ).trim ( ));
                        if (points < 10) {
                            et_point.setError ("Minimum Biding amount is 10");
                            et_point.requestFocus ( );
                            return;
                        } else if (points > Integer.parseInt (w_amount)) {
                            module.fieldRequired ("Insufficient Amount");
                        } else {
                            for (int i = 0; i <= red_bracket.length - 1; i++) {
                                int num = 1;
                                for (int n = 0; n < list.size ( ); n++) {
                                    num = num + 1;
                                }
                                String number = String.valueOf (num);
                                module.addData (number, "Red Bracket", red_bracket[i], point, "Close", list, tableAdaper, list_table, btn_submit);

                            }
                        }
                    }
                } else if (rel_bracket.getVisibility ( ) == View.VISIBLE) {
                    if (et_digit.getText ( ).toString ( ).isEmpty ( )) {
                        et_digit.setError ("Digit Required");
                        et_digit.requestFocus ( );

                    } else if (et_point.getText ( ).toString ( ).isEmpty ( )) {
                        et_point.setError ("Point Required");
                        et_point.requestFocus ( );
                    } else if (!digitlist.contains (digit)) {
                        et_digit.setError ("Invalid");
                        et_digit.setText ("");
                        et_digit.requestFocus ( );
                    } else {
                        int points = Integer.parseInt (et_point.getText ( ).toString ( ).trim ( ));
                        if (points < 10) {
                            et_point.setError ("Minimum Biding amount is 10");
                            et_point.requestFocus ( );
                            return;

                        } else if (points > Integer.parseInt (w_amount)) {
                            module.fieldRequired ("Insufficient Amount");
                        } else {
                            int num = 1;
                            for (int n = 0; n < list.size ( ); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                                num = num + 1;
                            }
                            String number = String.valueOf (num);
                            module.addData (number, "Red Bracket", digit, point, bettype, list, tableAdaper, list_table, btn_submit);
                            et_digit.requestFocus ( );
                            clearData ( );
                        }
                    }

                }
            }
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
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String cur_time = format.format(date);
//                    String cur_date = sdf.format(date);

                        Log.e("true", "today");
//                        Date s_date = format.parse(s_time);
                        Date e_date = format.parse(e_time);
                        Date c_date = format.parse(cur_time);
//                        long difference = c_date.getTime() - s_date.getTime();
//                        long as = (difference / 1000) / 60;

                        long diff_close = c_date.getTime() - e_date.getTime();
                        long curr = (diff_close / 1000) / 60;
//                        long current_time = c_date.getTime();

                         if (curr < 0) {
                             module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id,betdate, game_id, w_amount, matka_name, btn_submit, s_time, e_time);
                         }else{
                             module.fieldRequired ("Biding is Closed Now");
                            }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(v.getId ()==R.id.tv_date){
            module.setDateDialog (dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
        }
        else if(v.getId ()==R.id.tv_type){

            module.setBetTypeDialog (dialog,gamedate,tv_open,tv_close,tv_type,s_time,e_time,game_id);
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