package in.games.gdmatkalive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.JackpotAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.JackpotModel;
import in.games.gdmatkalive.Model.RateModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.RecyclerTouchListener;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_JackpotMatka;
import static in.games.gdmatkalive.Config.BaseUrls.URL_NOTICE;

public class JackpotGameFragment extends Fragment implements View.OnClickListener {
    private final String TAG= JackpotGameFragment.class.getSimpleName();
SwipeRefreshLayout swipe;
RelativeLayout rel_bidHistory,rel_winHistory;
RecyclerView rec_jackpot;
    ArrayList<RateModel> slist;
ArrayList<JackpotModel> jList;
JackpotAdapter jackpotAdapter;
    Module module;
    TextView tv_jodi,tv_singeDigit;
    ArrayList<RateModel> list;
    LoadingBar loadingBar;
    String srate,jrate,jname,jrange,sname,srange;
    SessionMangement sessionMangement;

    public JackpotGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jackpot_game, container, false);
        ((MainActivity)getActivity()).setTitles("Jackpot Games");
        initView(view);

        if (ConnectivityReceiver.isConnected()) {
            module.loginStatus(getActivity());
            allJackpotGame();
            getmatkaRate ();
        }else {
            module.noInternet();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    allJackpotGame();
                    getmatkaRate ();
                    swipe.setRefreshing(false);
                }
            }
        });

        rec_jackpot.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rec_jackpot, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();


               JackpotModel model = jList.get(position);
//                String e_t = get24Hours(model.getEnd_time ());
//                String s_t = get24Hours(model.getStart_time ());
//                int sTime=module.getTimeFormatStatus(model.getStart_time ());
//                Date date=new Date();
//                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
//                String ddt=simpleDateFormat.format(date);
//                int c_tm=Integer.parseInt(ddt);
//                String st=module.get24Hours(model.getEnd_time ().toString());
//                long tmLong=module.getTimeDifference(st);
//                if(tmLong<=0)
//                {module.marketClosed("Betting is closed for today");
//                    return;
//                }else {
//
//                    Fragment fm  = new SelectGameFragment ();
//                    bundle.putString("m_id",model.getId());
//                    bundle.putString("end_time",e_t);
//                    bundle.putString("start_time",s_t);
//                    bundle.putString("matka_name",module.get24To12Format(model.getName ()));
//                    bundle.putString("type","jackpot");
//                    fm.setArguments(bundle);
//               FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//               fragmentManager.beginTransaction().replace(R.id.frame,fm).addToBackStack(null).commit();
//                }
                Fragment fm  = new SelectGameFragment ();

                bundle.putString("matka_name",model.getName ());
                bundle.putString ("m_id",model.getId ());
                bundle.putString("s_num","");
                bundle.putString("num","");
                bundle.putString("e_num","");
                bundle.putString("end_time",model.getEnd_time ());
                bundle.putString("start_time",model.getStart_time ());

                bundle.putString("type","jackpot");
                fm.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame,fm).addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }
    private void getmatkaRate() {
        loadingBar.show();
        slist.clear ();
        list.clear ();

        String tag_json_obj = "json_notice_req";
        HashMap<String, String> params = new HashMap<String, String> ();
        module.postRequest (URL_NOTICE, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e (TAG, "onResponse: "+response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject (String.valueOf (response));
                    jsonObject.getString ("status");
                    if(jsonObject.getString ("status").equals ("success")) {
                        JSONArray array=jsonObject.getJSONArray("data");

                        for (int i=0; i<array.length();i++)
                        {
                            RateModel gameRateModel=new RateModel();
                            JSONObject object=array.getJSONObject(i);
                            gameRateModel.setId(object.getString("id"));
                            gameRateModel.setName(object.getString("name"));
                            gameRateModel.setRate_range(object.getString("rate_range"));
                            gameRateModel.setJackpot_rate (object.getString("jackpot_rate"));

if(object.getString("id").equals ("4")){

    srate=object.getString("jackpot_rate");
    sname=object.getString ("name");
    srange=object.getString ("rate_range");
    slist.add(gameRateModel);
}

    else if(object.getString("id").equals ("5")) {

    jrate=object.getString("jackpot_rate");
    jname=object.getString ("name");
    jrange=object.getString ("rate_range");
    slist.add(gameRateModel);

                        }


                            //tv_singeDigit.setText(rate);

                        }
                        tv_singeDigit.setText(sname+"-"+srange+":"+srate);
                        tv_jodi.setText (jname+"-"+jrange+":"+jrate);

//                        tv_singeDigit.setText(getRate ());
                    }
                    else
                    {
                        module.errorToast ("Something Went Wrong");
                    }
                    loadingBar.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
            }
        });
    }
    private String getRate() {
        StringBuffer stringBuffer=new StringBuffer();
        for(RateModel m:slist){
            if(m.getJackpot_rate ().equals (null)){
                stringBuffer.append("");
                tv_singeDigit.setVisibility (View.GONE);
            }
            else {
                stringBuffer.append(m.getName()+" - "+m.getRate_range()+" : "+m.getJackpot_rate ()+"\n");
            }

        }
        Log.e(TAG, "setRates: "+stringBuffer.toString() );
        return stringBuffer.toString();
    }
    private void initView(View view) {
        sessionMangement=new SessionMangement (getActivity());
        loadingBar=new LoadingBar (getActivity ());
        module=new Module (getActivity ());
        jList = new ArrayList<>();
        list=new ArrayList<> ();
        tv_singeDigit=view.findViewById (R.id.tv_singeDigit);
        slist=new ArrayList<>();
        tv_jodi=view.findViewById (R.id.tv_jodi);
        rec_jackpot = view.findViewById(R.id.rec_jackpot);
        swipe = view.findViewById(R.id.swipe);
        rel_bidHistory = view.findViewById(R.id.rel_bidHistory);
        rel_winHistory = view.findViewById(R.id.rel_winHistory);
        rec_jackpot = view.findViewById(R.id.rec_jackpot);
        rec_jackpot.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rel_bidHistory.setOnClickListener(this);
        rel_winHistory.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (v.getId())
        {
            case R.id.rel_bidHistory:
                fragment = new MatkaNameHistoryFragment();
                bundle.putString("type","jackpot");
                break;
            case R.id.rel_winHistory:
                fragment = new MatkaNameHistoryFragment();
                bundle.putString("type","jackpot_win");
                break;
        }
        if (fragment!=null)
        {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    private void allJackpotGame() {
//        jList = new ArrayList<>();
//        jList.add(new JackpotModel());
//        jList.add(new JackpotModel());
//        jList.add(new JackpotModel());
//
//
//        jackpotAdapter = new JackpotAdapter(getActivity(),jList);
//        jackpotAdapter.notifyDataSetChanged();
//        rec_jackpot.setAdapter(jackpotAdapter);
        loadingBar.show();
        jList.clear ();
        String tag_json_obj = "json_notice_req";
        HashMap<String, String> params = new HashMap<String, String> ();
        module.postRequest (URL_JackpotMatka, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e (TAG, "onResponse: "+response);
                   loadingBar.dismiss ();
                try
                {
                    JSONArray jsonArray=new JSONArray(response.toString ());

                    for(int i=0; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        Log.e ("jackpot", "onResponse: "+jsonObject );
                        JackpotModel obj=new JackpotModel ();
                        obj.setId(jsonObject.getString("id"));
                        obj.setName(jsonObject.getString("name"));
                        obj.setStart_time (jsonObject.getString("start_time"));
                        obj.setEnd_time (jsonObject.getString("end_time"));
                        obj.setStatus (jsonObject.getString("status"));
                        obj.setResult (jsonObject.getString("result"));

                        jList.add(obj);

                    }
                    if (jList.size()>0)
                    {
                        jackpotAdapter = new JackpotAdapter (getActivity (), jList);
                        jackpotAdapter.notifyDataSetChanged();
                        rec_jackpot.setAdapter(jackpotAdapter);
                    }

                } catch (JSONException e) {
                    loadingBar.dismiss ();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss ();
            }
        });

    }
    String  get24Hours(String time)
    {
        String t[]=time.split(" ");
       // String time_type=t[1].toString();
        String t1[]=t[0].split(":");

        int tm=Integer.parseInt(t1[0].toString());

//        if(time_type.equalsIgnoreCase("PM") || time_type.equalsIgnoreCase("p.m"))
//        {
//            if(tm==12)
//            {
//
//            }
//            else
//            {
//                tm=12+tm;
//            }
//        }

//       String s ="";
//       String h = time.substring(0,1);
//       if (time.contains("PM")|| time.contains("p.m"))
//       {
//       int hours = Integer.parseInt(h);
//       if (hours<12)
//       {
//          hours =hours+12;
//       }
        String s = String.valueOf(tm)+":"+t1[1]+":00";

        return s;
    }
    private void getNotice() {

        loadingBar.show();
        list = new ArrayList<>();


        HashMap<String, String> params = new HashMap<String, String>();

        module.postRequest(URL_NOTICE, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    if (status.equals("success")) {
                        JSONArray array = obj.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {
                            RateModel gameRateModel = new RateModel();
                            JSONObject object = array.getJSONObject(i);
                            gameRateModel.setId(object.getString("id"));
                            gameRateModel.setName(object.getString("name"));
                            gameRateModel.setRate_range(object.getString("rate_range"));
                            gameRateModel.setRate(object.getString("jackpot_rate"));
                            String type = object.getString("type").toString();
                            gameRateModel.setType(type);


if((object.getString("name").equals ("Single Digit"))){
    list.add(gameRateModel);
    tv_jodi.setText ("Single Digit"+(object.getString("jackpot_rate")));
}


                            }
                        //else {
                               // slist.add(gameRateModel);
                           // }
                       // }

//                        rateAdapter = new RateAdapter (getActivity (), list);
//                        rec_rate.setAdapter(rateAdapter);
//                        rateAdapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }

                    loadingBar.dismiss();
                } catch (Exception ex) {
                    loadingBar.dismiss();
                    module.showToast(ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }
}