package in.games.gdmatkalive.Fragment.AllTable;

import static in.games.gdmatkalive.Adapter.NewSinglePointsAdapter.is_error;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.list_input_data.single_digit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.NewSinglePointsAdapter;
import in.games.gdmatkalive.Adapter.StarlineGameAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.StarlineGameModel;
import in.games.gdmatkalive.Model.TableModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;


public class SingleDigirtFragment extends Fragment implements View.OnClickListener {
    List<TableModel> list;
    List<String> digit_list ;
    private int val_p=0;
    public static Button btnSubmit;
    public  static TextView btnReset;
    ImageView iv_back,img_notification;
    public static TextView tv_game;

    String matName="";
    String game;
    String gamedate;
    LoadingBar progressDialog;
    private TextView tv_wallet;
    public  static TextView tv_type;
    private String game_id,game_name;
    private String m_id ,end_time,start_time ,bet_type,title;
    String name,betdate,bettype,w_amount="", matka_name, matka_id ,s_time ,e_time,no;


    RecyclerView rv_digits ;
    NewSinglePointsAdapter pointsAdapter ;
    String dashName,user_id="";
    Dialog dialog;
    SessionMangement session_management;
    public static ArrayList<TableModel>bet_list,tempList;
    String type="";
    TextView tv_title,tv_market,txt_date;

    TextView tv_mid;
    TextView tv_date,tv_open,tv_close,tv_single,tv_jodi,tv_date1,tv_date2,tv_date3,txtDate_id;

    ArrayList<StarlineGameModel> sList;
    RecyclerView rec_market;
    Module common;
    StarlineGameAdapter starlineMarketAdapter;
    public static String th="";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public SingleDigirtFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_single_digirt, container, false);
        initview(view);
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        matka_name = getArguments().getString("matka_name");
        title = getArguments().getString("title");



        gamedate=tv_date.getText ().toString ();

        int  m_id = Integer.parseInt (matka_id);
//int min_amt= Integer.parseInt (min_add_amount);


        if (m_id>20)
        {
            ((MainActivity) getActivity ( )).setTitles (title);

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
            try {
                common.getCurrentDate(tv_date);
                if (common.getTimeDifference(s_time) > 0) {

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
            common.getCurrentDate(tv_date);
        }



        w_amount = ((MainActivity) getActivity()).getWallet();

        return view;

    }

    private void initview(View view) {
        common=new Module (getActivity ());

        sList = new ArrayList<>();

        session_management = new SessionMangement (getActivity ());
        user_id = session_management.getUserDetails().get(KEY_ID);
        list=new ArrayList<>();
        tv_game=view.findViewById(R.id.tv_game);
        rv_digits =view.findViewById(R.id.rec_jodi);

        progressDialog=new LoadingBar(getActivity ());
        tv_wallet=view.findViewById(R.id.tv_wallet);
        tv_type=view.findViewById (R.id.tv_type);
        tv_type.setOnClickListener (this);
        btnSubmit =view.findViewById(R.id.btn_add);
        btnReset = view.findViewById(R.id.btnreset);
        tv_title=view.findViewById(R.id.tv_title);
        tv_date =view.findViewById(R.id.tv_date);

        list = new ArrayList<>();
        bet_list=new ArrayList<>();
        tempList=new ArrayList<>();
        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        digit_list =  Arrays.asList(single_digit);
        pointsAdapter =new NewSinglePointsAdapter (digit_list,getActivity ());
        rv_digits.setNestedScrollingEnabled(false);
        rv_digits.setLayoutManager(new GridLayoutManager (getActivity (),2));
        rv_digits.setAdapter(pointsAdapter);



    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        bettype=tv_type.getText ().toString () ;
        int id = v.getId();
        if(v.getId ()==R.id.tv_type){
            common.setBetTypeDialog (dialog,gamedate,tv_open,tv_close,tv_type,s_time,e_time,game_id);
        }
        else if (id == R.id.btn_add) {
            tempList.clear();
            Log.e ("wdefrgthy", String.valueOf(bet_list.size ()));
            for(int k=0; k<bet_list.size();k++)
            {
                Log.e ("dfgh", "onClick: "+bet_list.get(k).getPoints().toString() );
                if(bet_list.get(k).getPoints().toString().equals("0") || bet_list.get(k).getPoints().toString().equals(""))
                {
                    //Toast.makeText (getActivity (), "fghjk", Toast.LENGTH_SHORT).show ( );
                }
                else
                {
                    Log.e ("wdefrgthy", "axsdfghyj");
                    tempList.add(bet_list.get(k));
                    Log.e ("list_data", "onClick: "+bet_list.toString ());

                }
            }
            for(TableModel model:tempList){
                Log.e("temp_data",""+model.getDigits()+" - "+model.getPoints());
            }
            if (tempList.size()<=0) {
                common.fieldRequired ("Please enter some points");

            }
            else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
            {
                common.fieldRequired ("Please Select Game Type");

            }
            else {
                if (is_error) {
                    common.fieldRequired ("Minimum bid amount is 10");
                } else {

//                    String dt = btnGameType.getText().toString().trim();
                    //String c = txt_date.getText().toString().substring(0, 10);
//                    String d[] = dt.split(" ");

//                    String c = d[0].toString();
                    // String w = tv_wallet.getText().toString().trim();
                    Date date = new Date();
//                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                    String cur_time = format.format(date);

                    try {
                        Date s_date = format.parse(s_time);
                        Date e_date = format.parse(e_time);
                        Date c_date = format.parse(cur_time);
                        long difference = c_date.getTime() - s_date.getTime();
                        long as = (difference / 1000) / 60;

                        long diff_close = c_date.getTime() - e_date.getTime();
                        long curr = (diff_close / 1000) / 60;
                        long current_time = c_date.getTime();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long bidTime = common.getTimeDifference(e_time);

                    if(bidTime>0)
                    {
//                        common.setBidsDialog(tempList, m_id, c, game_id, w, dashName, progressDialog, btnSubmit, start_time, end_time);
                        common.setBidsDialog(Integer.parseInt(w_amount),tempList, matka_id,betdate, game_id, w_amount, matka_name, btnSubmit, s_time, e_time);

                        //Log.e("my", "onClick: "+Integer.parseInt(w_amount)+""+list+ matka_id+betdate+game_id+w_amount+matka_name+s_time+e_time );
                    }
                    else
                    {
                        common.fieldRequired ("Biding is Closed Now");
                    }


//                    list.clear();
                }
            }

        }


    }
    public long[] getASandC(String startTime,String endTime){
        long[] tArr=new long[2];
        Date date=new Date();
        SimpleDateFormat sim=new SimpleDateFormat ("HH:mm:ss");
        String time1 = startTime.toString();
        String time2 = endTime.toString();

        Date cdate=new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat ("HH:mm:ss");
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