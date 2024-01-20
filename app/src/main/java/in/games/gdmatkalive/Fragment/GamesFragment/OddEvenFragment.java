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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.TableAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.TableModel;
import in.games.gdmatkalive.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OddEvenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OddEvenFragment extends Fragment implements View.OnClickListener {
  CheckBox check_odd,check_even;
    EditText et_point;
    Dialog dialog;
    String gamedate;
    String betdate,bettype,w_amount="",check_value,game_name, matka_name, matka_id ,game_id,s_time ,e_time,no;
    Button btn_add,btn_submit;
    TextView tv_date,tv_type,tv_open,tv_close,tv_date1,tv_date2,tv_date3,txtDate_id;
    ListView list_table;
    TableAdapter tableAdaper;
    Module module;
    List<TableModel> list;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OddEvenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OddEvenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OddEvenFragment newInstance(String param1, String param2) {
        OddEvenFragment fragment = new OddEvenFragment ( );
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
        View view= inflater.inflate (R.layout.fragment_odd_even, container, false);
        initview(view);
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        matka_name = getArguments().getString("matka_name");

        check_even.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.size()>0)
                {
                    list.clear();
                    tableAdaper = new TableAdapter(list, getActivity());
                    list_table.setAdapter(tableAdaper);
                    tableAdaper.notifyDataSetChanged();

                }
                if(check_odd.isChecked())
                {
                    check_odd.setChecked(false);
                    check_even.setChecked(true);
                }
                else
                {
                    check_even.setChecked(true);
                }

            }
        });

        check_odd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.size()>0)
                {
                    list.clear();
                    tableAdaper = new TableAdapter(list, getActivity());
                    list_table.setAdapter(tableAdaper);
                    tableAdaper.notifyDataSetChanged();

                }
                if(check_even.isChecked())
                {
                    check_odd.setChecked(true);
                    check_even.setChecked(false);
                }
                else
                {
                    check_odd.setChecked(true);
                }

            }
        });



        w_amount = ((MainActivity) getActivity()).getWallet();


        return view;
    }

    private void initview(View view) {

        et_point=view.findViewById (R.id.et_point);
        gamedate=tv_date.getText ().toString ();
        tv_date=view.findViewById (R.id.tv_date);
        tv_type=view.findViewById (R.id.tv_type);
        tv_type.setOnClickListener (this);
        tv_date.setOnClickListener (this);
        check_even=view.findViewById (R.id.check_even);
        check_odd=view.findViewById (R.id.check_odd);
        btn_add=view.findViewById (R.id.btn_add);
        btn_add.setOnClickListener (this);
        btn_submit=view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);

        list=new ArrayList<> ();
        list_table=view.findViewById(R.id.list_table);
        module=new Module (getContext ());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        String point=et_point.getText ().toString ();

        if(v.getId ()==R.id.btn_add)
        {
            betdate = tv_date.getText().toString();
            bettype = tv_type.getText().toString();
            bettype = tv_type.getText().toString();

            if(betdate.equalsIgnoreCase ("SELECT DATE"))
            {
                module.fieldRequired ("Please Select Date");

            }
            else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
            {
                module.fieldRequired ("Please Select Game Type");
            }
            else if(!check_odd.isChecked ()&&!check_even.isChecked ()){
                module.fieldRequired ("Please Select Odd & Even");
            }



            else  if(et_point.getText ().toString ().isEmpty ()){
                et_point.setError ("Point Required");
                et_point.requestFocus ();
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


                    String p=et_point.getText().toString().trim();
                    if(check_odd.isChecked())
                    {

                        String[] odd={"1","3","5","7","9"};

                        for(int i=0; i<=odd.length-1; i++)
                        {

                            module.addData(number,"Odd",odd[i],p,bettype,list,tableAdaper,list_table,btn_submit);

                        }
                    }
                    else if(check_even.isChecked())
                    {

                        String[] even={"0","2","4","6","8"};


                        for(int i=0; i<=even.length-1; i++)
                        {

                            module.addData(number,"Even",even[i],p,bettype,list,tableAdaper,list_table,btn_submit);


                        }
                    }
                    else
                    {
                        module.errorToast("Please select any digit type");
                        return;
                    }
                    //module.addData(number,"Odd Even",check_value,point,bettype,list,tableAdaper,list_table,btn_submit);
                    et_point.requestFocus();
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
        else if(v.getId ()==R.id.tv_type){

            module.setBetTypeDialog (dialog,gamedate,tv_open,tv_close,tv_type,s_time,e_time,game_id);
        }
    }
    private void clearData() {

        et_point.setText ("");

    }
}