package in.games.gdmatkalive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.RateAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.RateModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_NOTICE;


public class GameRateFragment extends Fragment {
    SwipeRefreshLayout swipe;
RecyclerView rec_rate;
RateAdapter rateAdapter;
ArrayList<RateModel> rList,slist;
LoadingBar loadingBar;
Module module;
SessionMangement sessionMangement;

    public GameRateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_game_rate, container, false);
        initview(view);
        ((MainActivity)getActivity()).setTitles("Game Rate");
        rec_rate.setLayoutManager (new LinearLayoutManager (getContext ()));
//        rateData ( );

        if (ConnectivityReceiver.isConnected()) {
            getNotice();
        } else
        {
           module.noInternet();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    if (ConnectivityReceiver.isConnected())
                    {
                        getNotice();
                    }else {
                        module.noInternet();
                    }
                    swipe.setRefreshing(false);
                }

            }
        });

        return view;
    }


    private void initview(View view) {

        module = new Module(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        loadingBar = new LoadingBar(getActivity());
        rec_rate=view.findViewById (R.id.rec_rate);
        swipe = view.findViewById(R.id.swipe);
        rList=new ArrayList<> (  );
        slist = new ArrayList<>();
    }
    private void rateData() {
        rList.add(new RateModel ());
        rList.add(new RateModel ());

        rateAdapter = new RateAdapter (getActivity (), rList);
        rateAdapter.notifyDataSetChanged();
        rec_rate.setAdapter(rateAdapter);
    }
    private void getNotice() {

        loadingBar.show();
        rList = new ArrayList<>();
        slist = new ArrayList<>();

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
                            gameRateModel.setRate(object.getString("rate"));
                            String type = object.getString("type").toString();
                            gameRateModel.setType(type);
                            if (type.equals("0")) {
                                rList.add(gameRateModel);

                            } else {
                                slist.add(gameRateModel);
                            }
                        }

                        rateAdapter = new RateAdapter (getActivity (), rList);
                        rec_rate.setAdapter(rateAdapter);
                        rateAdapter.notifyDataSetChanged();


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