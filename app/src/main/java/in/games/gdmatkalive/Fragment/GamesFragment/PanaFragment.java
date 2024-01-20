package in.games.gdmatkalive.Fragment.GamesFragment;

import android.app.Dialog;
import android.os.Bundle;

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

import static in.games.gdmatkalive.Config.list_input_data.doublePanna;
import static in.games.gdmatkalive.Config.list_input_data.singlePaana;
import static in.games.gdmatkalive.Config.list_input_data.triplePanna;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PanaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PanaFragment extends Fragment implements View.OnClickListener {
    private final String TAG=PanaFragment.class.getSimpleName();
    Module module;
    Dialog dialog;
    String gamedate;
    AutoCompleteTextView et_panna;
    RelativeLayout rel_single;
    EditText et_point;
    ListView list_table;
    TableAdapter tableAdaper;
    Button btn_add,btn_submit;
    List<TableModel> list;
    List<String> digitlist;
    String name,betdate,bettype,w_amount="",s_time ,e_time,matka_name ,game_id,matka_id,game_name,title;
    TextView tv_date,tv_type,tv_open,tv_close,tv_single,tv_double,tv_triple,tv_date1,tv_date2,tv_date3,txtDate_id;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PanaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PanaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PanaFragment newInstance(String param1, String param2) {
        PanaFragment fragment = new PanaFragment ( );
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
        View view= inflater.inflate (R.layout.fragment_pana, container, false);
        initview(view);
        w_amount = ((MainActivity) getActivity()).getWallet();

        Bundle bundle = getArguments ();
        name= bundle.getString ("panna");
        if(name.equalsIgnoreCase ("SINGLE PANNA")){
            tv_single.setVisibility (View.VISIBLE);
            tv_double.setVisibility (View.GONE);
            tv_triple.setVisibility (View.GONE);
            et_panna.setFilters (new InputFilter[]{new InputFilter.LengthFilter (3)});
            digitlist= Arrays.asList (singlePaana);
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,singlePaana);
            et_panna.setAdapter(adapter1);

        }
       else if(name.equalsIgnoreCase ("DOUBLE PANNA")){
            tv_single.setVisibility (View.GONE);
            tv_triple.setVisibility (View.GONE);
            tv_double.setVisibility (View.VISIBLE);
            et_panna.setFilters (new InputFilter[]{new InputFilter.LengthFilter (3)});
            digitlist= Arrays.asList (doublePanna);
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,doublePanna);
            et_panna.setAdapter(adapter1);

        }
        else if(name.equalsIgnoreCase ("TRIPLE PANNA")){
            tv_triple.setVisibility (View.VISIBLE);
            tv_double.setVisibility (View.GONE);
            tv_single.setVisibility (View.GONE);
            et_panna.setFilters (new InputFilter[]{new InputFilter.LengthFilter (3)});
            digitlist= Arrays.asList (triplePanna);
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,triplePanna);
            et_panna.setAdapter(adapter1);
        }
        return view;
    }

    private void initview(View view) {
        et_panna=view.findViewById (R.id.et_panna);
        et_point=view.findViewById (R.id.et_point);
        btn_add=view.findViewById (R.id.btn_add);
        btn_add.setOnClickListener (this);
        btn_submit=view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        tv_date=view.findViewById (R.id.tv_date);
        tv_type=view.findViewById (R.id.tv_type);
        tv_single=view.findViewById (R.id.tv_single);
        tv_double=view.findViewById (R.id.tv_double);
        tv_triple=view.findViewById (R.id.tv_triple);
        tv_type.setOnClickListener (this);
//        tv_date.setOnClickListener (this);
        list=new ArrayList<> ();
        digitlist=new ArrayList<> ();
        module=new Module (getContext ());
        list_table=view.findViewById(R.id.list_table);

        gamedate=tv_date.getText ().toString ();
        matka_name = getArguments().getString("matka_name");
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        title = getArguments().getString("title");
int m_id= Integer.parseInt(matka_id);

        if (m_id>20)

        {
            ((MainActivity) getActivity ( )).setTitles (title);

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
//            tv_type.setText("Open");
            try {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy EEEE");
                String cur_date = dateFormat.format(date);
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

        }else {
            ((MainActivity) getActivity ( )).setTitles (title+"("+matka_name+")");
            module.getCurrentDate(tv_date);
        }

    }

    @Override
    public void onClick(View v) {
        String panna=et_panna.getText ().toString ();
        String point=et_point.getText ().toString ();
        if (v.getId ( ) == R.id.btn_add) {

            betdate = tv_date.getText ( ).toString ( );
            bettype = tv_type.getText ( ).toString ( );
            if(betdate.equalsIgnoreCase ("SELECT DATE"))
            {
                module.fieldRequired ("Date Required");
            }

             else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
            {
                module.fieldRequired ("Please Select Game Type");
            }
            else if (et_panna.getText ( ).toString ( ).isEmpty ( )) {
                et_panna.setError ("Panna Required");
                et_panna.requestFocus ( );

            } else if (et_point.getText ( ).toString ( ).isEmpty ( )) {
                et_point.setError ("Point Required");
                et_point.requestFocus ( );
            }
            else  if(!digitlist.contains (panna)){
                et_panna.setError ("Invalid");
                et_panna.setText ("");
                et_panna.requestFocus ();
            }
            else {
                int points = Integer.parseInt (et_point.getText ( ).toString ( ).trim ( ));
                if (points < 10) {
                    et_point.setError ("Minimum Biding amount is 10");
                    et_point.requestFocus ( );
                    return;


                } else if (points > Integer.parseInt (w_amount)) {
                    module.fieldRequired ("Insufficient Amount");
                } else {
                    int num = 1;
                    for (int n = 0; n < list.size(); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                        num=num+1;
                    }
                    String number=String.valueOf(num);


                    module.addData(number,name,panna,point,bettype,list,tableAdaper,list_table,btn_submit);

                    et_panna.requestFocus();
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
            et_panna.setText ("");
            et_point.setText ("");
        }
}