package in.games.gdmatkalive.Fragment.GamesFragment;

import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JodiFamilyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JodiFamilyFragment extends Fragment implements View.OnClickListener {
    AutoCompleteTextView et_digit;
    EditText et_point;
    Button btn_add,btn_submit;;
    ListView list_table;
    TableAdapter tableAdaper;
  Module module;
Dialog dialog;
    List<String> digitlist;
    List<TableModel> list;
TextView tv;
    String betdate,w_amount="",bettype,s_time ,e_time,matka_name ,game_id,matka_id,game_name;
    TextView tv_date,tv_open,tv_close,tv_single,tv_jodi,tv_date1,tv_date2,tv_date3,txtDate_id,tv_type;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JodiFamilyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JodiFamilyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JodiFamilyFragment newInstance(String param1, String param2) {
        JodiFamilyFragment fragment = new JodiFamilyFragment ( );
        Bundle args = new Bundle ( );
        args.putString (ARG_PARAM1, param1);
        args.putString (ARG_PARAM2, param2);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (getArguments ( ) != null) {
            mParam1 = getArguments ( ).getString (ARG_PARAM1);
            mParam2 = getArguments ( ).getString (ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_jodi_family, container, false);
    initview(view);


        matka_name = getArguments().getString("matka_name");
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");

        w_amount = ((MainActivity) getActivity()).getWallet();
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,group_jodi_digits);
        et_digit.setAdapter(adapter2);
        digitlist= Arrays.asList (group_jodi_digits);
         return view;
    }

    private void initview(View view) {
        et_digit=view.findViewById (R.id.et_digit);
        et_point=view.findViewById (R.id.et_point);
        btn_add=view.findViewById (R.id.btn_add);
        btn_add.setOnClickListener (this);
        btn_submit=view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        tv_date=view.findViewById (R.id.tv_date);
        module=new Module (getContext ());
        tv_date.setOnClickListener (this);
        //tv_type=view.findViewById (R.id.tv_type);
        digitlist=new ArrayList<> ();
        list=new ArrayList<> ();
        list_table=view.findViewById(R.id.list_table);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        String digit=et_digit.getText ().toString ();
        String point=et_point.getText ().toString ();
        if(v.getId ()==R.id.btn_add)
        {
            betdate = tv_date.getText().toString();
            //bettype = tv_type.getText().toString();



            if(betdate.equalsIgnoreCase ("SELECT DATE"))
            {
                module.fieldRequired ("Date Required");
            }

//
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
                    module.showToast ("Insufficient Amount");
                }
                else {

                    int num = 1;
                    for (int n = 0; n < list.size(); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                        num=num+1;
                    }
                    String number=String.valueOf(num);
                    module.addData(number,"JODI FAMILY",digit,point,"close",list,tableAdaper,list_table,btn_submit);
                    et_digit.requestFocus();
                    clearData ();


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
    }
    private void clearData() {
        et_digit.setText ("");
        et_point.setText ("");
    }
}