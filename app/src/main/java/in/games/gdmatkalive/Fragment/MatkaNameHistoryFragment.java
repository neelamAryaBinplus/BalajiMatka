package in.games.gdmatkalive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.gdmatkalive.Adapter.JackpotMatkaAdapter;
import in.games.gdmatkalive.Adapter.MatkaHistoryAdapter;
import in.games.gdmatkalive.Adapter.StarlineGameAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.MatkahistoryModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.RecyclerTouchListener;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_JackpotMatka;
import static in.games.gdmatkalive.Config.BaseUrls.URL_Matka;
import static in.games.gdmatkalive.Config.BaseUrls.URL_STARLINE;

public class MatkaNameHistoryFragment extends Fragment {
    RecyclerView rec_mname;
    ArrayList<MatkahistoryModel> mList;
//    ArrayList<StarlineGameModel> sList;
    MatkaHistoryAdapter matkaHistoryAdapter;
    LoadingBar loadingBar;
    JackpotMatkaAdapter jackpotMatkaAdapter;
    Module module;
    SwipeRefreshLayout swipe;
    SessionMangement sessionMangement;
    String type="";
    StarlineGameAdapter starlineGameAdapter;
    public MatkaNameHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_matka_name_history, container, false);
        initview(view);

        rec_mname.setLayoutManager (new LinearLayoutManager (getContext ()));
//        matkaName ( );

        if (ConnectivityReceiver.isConnected())
        {
            if (type.equalsIgnoreCase("matka")||type.equalsIgnoreCase("matka_win"))
            {
                getMatkaData();
            }else if (type.equalsIgnoreCase("starline")||type.equalsIgnoreCase("starline_win"))
            {
                getStarlineData();
            }else if (type.equalsIgnoreCase("jackpot")||type.equalsIgnoreCase("jackpot_win")){
                matkaName();
            }
        }else {
            module.noInternet();
        }


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    if (ConnectivityReceiver.isConnected())
                    {
                        if (type.equalsIgnoreCase("matka")||type.equalsIgnoreCase("matka_win"))
                        {
                            getMatkaData();
                        }else if (type.equalsIgnoreCase("starline")||type.equalsIgnoreCase("starline_win"))
                        {
                            getStarlineData();
                        }else if (type.equalsIgnoreCase("jackpot")||type.equalsIgnoreCase("jackpot_win")){
                            matkaName();
                        }
                    }else {
                        module.noInternet();
                    }
                    swipe.setRefreshing(false);
                }
            }
        });

        rec_mname.addOnItemTouchListener(new RecyclerTouchListener (getContext (), rec_mname, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               Bundle args = new Bundle();
                Fragment fm =new AllHistoryFragment ();
                args.putString("name",mList.get(position).getName());
                args.putString("type",type);
                args.putString("matka_id",mList.get(position).getId());
                fm.setArguments(args);
                FragmentManager fragmentManager1=getFragmentManager ();
                    fragmentManager1.beginTransaction ().add (R.id.frame,fm).addToBackStack (null)
                            .commit ();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }


    private void initview(View view) {
        type=getArguments().getString("type");

        module = new Module(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        loadingBar = new LoadingBar(getActivity());
        rec_mname=view.findViewById (R.id.rec_mname);
        swipe = view.findViewById(R.id.swipe);
        mList=new ArrayList<> (  );

    }

    private void matkaName() {
        mList.clear();

        loadingBar.show();
        HashMap<String,String> params = new HashMap<> (  );
        module.postRequest(URL_JackpotMatka, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i=0; i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        MatkahistoryModel model = new MatkahistoryModel();
                        model.setName(object.getString("name"));
                        model.setId(object.getString("id"));
                        model.setStart_time (object.getString ("start_time"));
                        mList.add(model);

                    }
                    if (mList.size()>0)
                    {
                        jackpotMatkaAdapter = new JackpotMatkaAdapter (getActivity (), mList);
                        jackpotMatkaAdapter.notifyDataSetChanged();
                        rec_mname.setAdapter(jackpotMatkaAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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


    public void getMatkaData()
    {
        loadingBar.show();
        HashMap<String,String> params = new HashMap<> (  );
        module.postRequest(URL_Matka, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i=0; i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        MatkahistoryModel model = new MatkahistoryModel();
                       model.setName(object.getString("name"));
                        model.setId(object.getString("id"));

                        mList.add(model);

                    }
                    if (mList.size()>0)
                    {
                        matkaHistoryAdapter = new MatkaHistoryAdapter (getActivity (), mList);
                        matkaHistoryAdapter.notifyDataSetChanged();
                        rec_mname.setAdapter(matkaHistoryAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
    private void getStarlineData()
        {
            mList.clear ();
            loadingBar.show();

            final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_STARLINE, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.e("starline_response",response.toString());
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            MatkahistoryModel starlineModel = new MatkahistoryModel();

                            starlineModel.setName(jsonObject.getString("s_game_time"));
                            starlineModel.setId(jsonObject.getString("id"));
//                            starlineModel.setS_game_time(jsonObject.getString("s_game_time"));
                            mList.add(starlineModel);
                            loadingBar.dismiss();
                            if (mList.size()>0)
                            {
                                matkaHistoryAdapter = new MatkaHistoryAdapter (getActivity (), mList);
                                matkaHistoryAdapter.notifyDataSetChanged();
                                rec_mname.setAdapter(matkaHistoryAdapter);
                            }

                        }
                    }
                        catch (Exception ex)
                        {
                            loadingBar.dismiss();

                            return;
                        }
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
}