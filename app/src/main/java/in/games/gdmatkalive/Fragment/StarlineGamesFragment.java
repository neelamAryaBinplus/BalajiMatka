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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.StarlineGameAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.RateModel;
import in.games.gdmatkalive.Model.StarlineGameModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.RecyclerTouchListener;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_NOTICE;
import static in.games.gdmatkalive.Config.BaseUrls.URL_STARLINE;


public class StarlineGamesFragment extends Fragment implements View.OnClickListener {
    private final String TAG= StarlineGamesFragment.class.getSimpleName();
    RecyclerView rec_starline;
    SwipeRefreshLayout swipe;
    ArrayList<StarlineGameModel> sList;
    ArrayList<RateModel> list;
    ArrayList<RateModel> slist;
    Module module;
    LoadingBar loadingBar;
    SessionMangement sessionMangement;
    StarlineGameAdapter starlineGameAdapter;
    TextView tv_singleDigit,tv_singlePanna,tv_doublePanna,tv_triplePanna;
    RelativeLayout rel_bidHistory,rel_winHistory,rel_terms;

    public StarlineGamesFragment() {
        // Required empty public constructor
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_starline_games, container, false);
        ((MainActivity)getActivity()).setTitles("Starline Games");
        initView(view);
        //tv_singleDigit.setText (getRate());
        //allStarlineGames();

        if (ConnectivityReceiver.isConnected()) {
            module.loginStatus (getActivity());
            StarlineGames ();
        }else {
            module.noInternet();
        }

        getmatkaRate ();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    if (ConnectivityReceiver.isConnected())
                    {
                        StarlineGames ();
                    }else {module.noInternet();}

                    getmatkaRate();
                    swipe.setRefreshing(false);
                }
            }
        });

        rec_starline.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rec_starline, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StarlineGameModel model = sList.get(position);
                String e_t = get24Hours(model.getS_game_end_time());
                String s_t = get24Hours(model.getS_game_time());
                int sTime=module.getTimeFormatStatus(model.getS_game_time());
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
                String ddt=simpleDateFormat.format(date);
                int c_tm=Integer.parseInt(ddt);
                String st=module.get24Hours(model.getS_game_end_time().toString());
                long tmLong=module.getTimeDifference(st);
                if(tmLong<=0)
                {module.marketClosed("Betting is closed for today");
                    return;
                }else {
                    Bundle bundle = new Bundle();
                    Fragment fragment = new SelectGameFragment();

                    bundle.putString("m_id",model.getId());
                    bundle.putString("end_time",e_t);
                    bundle.putString("start_time",s_t);
                    bundle.putString("matka_name",model.getS_game_end_time());
                    bundle.putString("type","starline");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }
    private void getmatkaRate() {
        loadingBar.show();
        sList.clear ();
        list.clear ();
        list=new ArrayList<>();
        slist=new ArrayList<>();
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
                            gameRateModel.setRate(object.getString("rate"));
                            String type=object.getString("type").toString();
                            gameRateModel.setType(type);
                            if(type.equals("0"))
                            {
                                list.add(gameRateModel);
                            }
                            else
                            {
                                slist.add(gameRateModel);
                            }
                        }
                        tv_singleDigit.setText(getRate ());
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
            stringBuffer.append(m.getName()+" - "+m.getRate_range()+" : "+m.getRate()+"\n");
        }
        Log.e(TAG, "setRates: "+stringBuffer.toString() );
        return stringBuffer.toString();
    }

    private void initView(View view) {
        sList = new ArrayList<>();
        rec_starline = view.findViewById(R.id.rec_starline);
        swipe = view.findViewById(R.id.swipe);
        tv_singleDigit = view.findViewById(R.id.tv_singleDigit);
        tv_singlePanna = view.findViewById(R.id.tv_singlePanna);
        tv_doublePanna = view.findViewById(R.id.tv_doublePanna);
        tv_triplePanna = view.findViewById(R.id.tv_triplePanna);
        rel_bidHistory = view.findViewById(R.id.rel_bidHistory);
        rel_winHistory = view.findViewById(R.id.rel_winHistory);
        rel_terms = view.findViewById(R.id.rel_terms);

        rel_bidHistory.setOnClickListener(this);
        rel_winHistory.setOnClickListener(this);
        rel_terms.setOnClickListener(this);
        list = new ArrayList<>();
        slist = new ArrayList<>();
        sessionMangement=new SessionMangement (getActivity());
        loadingBar=new LoadingBar (getActivity ());
        module=new Module (getActivity ());

    }

    private  void StarlineGames()
    {
        sList.clear ();
        loadingBar.show();

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_STARLINE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("starline_response",response.toString());

                for(int i=0; i<response.length();i++)
                {
                    try
                    {
                        JSONObject jsonObject=response.getJSONObject(i);

                        StarlineGameModel matkasObjects=new StarlineGameModel ();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
                        matkasObjects.setS_game_number(jsonObject.getString("s_game_number"));
                        matkasObjects.setS_game_end_time(jsonObject.getString("s_game_end_time"));

                        sList.add(matkasObjects);
                    }
                    catch (Exception ex)
                    {
                        loadingBar.dismiss();

                        return;
                    }
                }
                loadingBar.dismiss();
                rec_starline.setLayoutManager(new GridLayoutManager(getActivity(),2));
                starlineGameAdapter = new StarlineGameAdapter(getActivity(),sList);
                starlineGameAdapter.notifyDataSetChanged();
                rec_starline.setAdapter(starlineGameAdapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingBar.dismiss();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (v.getId())
        {
            case R.id.rel_bidHistory:
                fragment = new MatkaNameHistoryFragment();
                bundle.putString("type","starline");
                break;
            case R.id.rel_winHistory:
                fragment = new MatkaNameHistoryFragment();
                bundle.putString("type","starline_win");
                break;
            case R.id.rel_terms:
                fragment = new TermsFragment();
                break;
        }
        if (fragment!=null)
        {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    String  get24Hours(String time)
    {
        String t[]=time.split(" ");
        String time_type=t[1].toString();
        String t1[]=t[0].split(":");

        int tm=Integer.parseInt(t1[0].toString());

        if(time_type.equalsIgnoreCase("PM") || time_type.equalsIgnoreCase("p.m"))
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }

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
}