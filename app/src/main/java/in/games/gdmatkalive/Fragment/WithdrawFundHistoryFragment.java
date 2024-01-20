package in.games.gdmatkalive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.FundHistoryAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.FundHistoryModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_FUND_HISTORY;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;

public class WithdrawFundHistoryFragment extends Fragment {
RecyclerView rec_wHistory;
SwipeRefreshLayout swipe;
ArrayList<FundHistoryModel> fList,aList;
FundHistoryAdapter fundHistoryAdapter;
LoadingBar loadingBar;
Module module;
SessionMangement sessionMangement;
String name;
    public WithdrawFundHistoryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraw_fund_history, container, false);


        name = getArguments().getString("name");
        sessionMangement = new SessionMangement(getActivity());
        module = new Module(getActivity());
        loadingBar = new LoadingBar(getActivity());
        fList = new ArrayList<>();
        aList = new ArrayList<>();
        rec_wHistory = view.findViewById(R.id.rec_wHistory);
        swipe = view.findViewById(R.id.swipe);
        if (name.equalsIgnoreCase("w"))
        {
            ((MainActivity)getActivity()).setTitles("Withdrawal Fund History");
        }else if (name.equalsIgnoreCase("a"))
        {
            ((MainActivity)getActivity()).setTitles("Add Fund History");
        }

//        fundHistory();
        getFundHistry(sessionMangement.getUserDetails().get(KEY_ID));
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 if (swipe.isRefreshing())
                 {
//                     fundHistory();
                     getFundHistry(sessionMangement.getUserDetails().get(KEY_ID));
                     swipe.setRefreshing(false);
                 }
             }
         });

        return view;
    }

    private void fundHistory() {
        fList.add(new FundHistoryModel());
        fList.add(new FundHistoryModel());
        fList.add(new FundHistoryModel());
        fList.add(new FundHistoryModel());
        rec_wHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        fundHistoryAdapter = new FundHistoryAdapter(getContext(),fList);
        fundHistoryAdapter.notifyDataSetChanged();
        rec_wHistory.setAdapter(fundHistoryAdapter);

    }


    private void getFundHistry(String user_id) {
        fList.clear ( );
        aList.clear ();
        loadingBar.show ( );

        HashMap<String, String> params = new HashMap<String, String>( );
        params.put ("user_id", user_id);
        module.postRequest(URL_FUND_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e ("fund_history", response.toString ( ));
                loadingBar.dismiss ( );
                try {
                    JSONObject  object = new JSONObject(response);
                    String res = object.getString ("status");
                    if (res.equals ("success")) {
                        JSONArray data = object.getJSONArray ("data");

                        if (data.equals("[]"))
                        {
                            module.errorToast ("No History Available");
                        }

                        for (int i = 0; i < data.length ( ); i++) {
                            FundHistoryModel model = new FundHistoryModel ( );
                            JSONObject obj = data.getJSONObject (i);
                            model.setRequest_id (obj.getString ("request_id"));
                            model.setRequest_points (obj.getString ("request_points"));
                            model.setRequest_status (obj.getString ("request_status"));
                            model.setUser_id (obj.getString ("user_id"));
                            model.setType (obj.getString ("type"));
                            model.setTime (obj.getString ("time"));
                          //  model.setWithdraw_type(obj.getString("withdraw_type"));
                            if (obj.getString ("type").equalsIgnoreCase("withdrawal"))
                            {
                                fList.add (model);
                            }else if (obj.getString ("type").equalsIgnoreCase("Add"))
                            {
                                aList.add(model);
                            }else {
                               // module.showToast("error");
                            }

                        }
                        Log.d ("req_list", String.valueOf (fList.size ( )));

                        if (name.equalsIgnoreCase("w"))
                        {
                            if (fList.size ( ) > 0) {

                                rec_wHistory.setLayoutManager(new LinearLayoutManager(getContext()));
                                fundHistoryAdapter = new FundHistoryAdapter(getContext(),fList);
                                fundHistoryAdapter.notifyDataSetChanged();
                                rec_wHistory.setAdapter(fundHistoryAdapter);

                            } else {

                                module.errorToast ("No History Available");
                            }
                        }else if (name.equalsIgnoreCase("a"))
                        {
                            if (aList.size ( ) > 0) {

                                rec_wHistory.setLayoutManager(new LinearLayoutManager(getContext()));
                                fundHistoryAdapter = new FundHistoryAdapter(getContext(),aList);
                                fundHistoryAdapter.notifyDataSetChanged();
                                rec_wHistory.setAdapter(fundHistoryAdapter);

                            } else {

                                module.errorToast ("No History Available");
                            }
                        }

                    } else {
                        module.errorToast ("something went wrong");
                    }
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


}