package in.games.gdmatkalive.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.AllHistoryAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.AllHistoryModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.JackpotWIN_HISTORY;
import static in.games.gdmatkalive.Config.BaseUrls.STARLINE_WIN_HISTORY;
import static in.games.gdmatkalive.Config.BaseUrls.URL_GET_HISTORY;
import static in.games.gdmatkalive.Config.BaseUrls.URL_Jackpot_HISTORY;
import static in.games.gdmatkalive.Config.BaseUrls.URL_STARLINE_HISTORY;
import static in.games.gdmatkalive.Config.BaseUrls.WIN_HISTORY;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;


public class AllHistoryFragment extends Fragment implements View.OnClickListener {
RecyclerView rec_history;
AllHistoryAdapter allHistoryAdapter;
TextView tv_date;
Button btn_search;
DatePickerDialog.OnDateSetListener setListener;
ArrayList<AllHistoryModel> hList,wList;
LoadingBar loadingBar;
Module module;
SessionMangement sessionMangement;
String name="";
SwipeRefreshLayout swipe;
String type="",matka_id="";


    public AllHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_all_history, container, false);
        initview(view);
        ((MainActivity)getActivity()).setTitles("My History");

        rec_history.setLayoutManager (new LinearLayoutManager (getContext ()));
        if (ConnectivityReceiver.isConnected()){
            if (type.equalsIgnoreCase("matka"))
            {
                getHistry(sessionMangement.getUserDetails().get(KEY_ID));
            }
            else if (type.equalsIgnoreCase("starline"))
            {
                getStarLineHistory(sessionMangement.getUserDetails().get(KEY_ID));
            }else if (type.equalsIgnoreCase("matka_win"))
            {
                getWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
            }else if (type.equalsIgnoreCase("starline_win")){
                getStarlineWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
            }
            else if (type.equalsIgnoreCase("jackpot"))
            {
                getJackpotHistry (sessionMangement.getUserDetails().get(KEY_ID),matka_id);
            }
            else if (type.equalsIgnoreCase("jackpot_win"))
            {
                getjackpotWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
            }

//            else {
//                getJackpotHistry(sessionMangement.getUserDetails().get(KEY_ID));
//            }
        }else {
            module.noInternet();
        }


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing()) {
                    if (ConnectivityReceiver.isConnected()){
                        if (type.equalsIgnoreCase("matka"))
                        {
                            getHistry(sessionMangement.getUserDetails().get(KEY_ID));
                        }

                        else if (type.equalsIgnoreCase("starline"))
                        {
                            getStarLineHistory(sessionMangement.getUserDetails().get(KEY_ID));
                        }else if (type.equalsIgnoreCase("matka_win")||type.equalsIgnoreCase("starline_win"))
                        {
                            getWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
                        }
                        else if (type.equalsIgnoreCase("jackpot"))
                        {
                            getJackpotHistry (sessionMangement.getUserDetails().get(KEY_ID),matka_id);
                        }
                        else if (type.equalsIgnoreCase("jackpot_win"))
                        {
                            getjackpotWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
                        }


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

        type = getArguments().getString("type");
        name = getArguments().getString("name");
        matka_id = getArguments().getString("matka_id");
        Log.e("type",type);
        sessionMangement = new SessionMangement(getActivity());
        module = new Module(getActivity());
        loadingBar = new LoadingBar(getActivity());
        swipe = view.findViewById(R.id.swipe);
        rec_history = view.findViewById(R.id.rec_history);
        hList = new ArrayList<>();
        wList = new ArrayList<>();
        tv_date = view.findViewById(R.id.tv_date);
        tv_date.setOnClickListener(this);
        btn_search = view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        }
        String currentdate = simpleDateFormat.format(date);
        tv_date.setText(currentdate);

    }
    @Override
    public void onClick(View v) {
        if (v.getId ( ) == R.id.tv_date) {
         datePicker();
        }
        else if (v.getId ( ) == R.id.btn_search) {
            allHistoryAdapter.getFilter().filter(tv_date.getText().toString());

        }
    }

    private void datePicker() {
        Calendar calendar = Calendar.getInstance ( );
        final int year = calendar.get (Calendar.YEAR);
        final int month = calendar.get (Calendar.MONTH);
        final int day = calendar.get (Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog (getContext ( ), android.R.style.Theme_Material_Light_DarkActionBar, setListener, year, month, day);
        datePickerDialog.getWindow ( ).setLayout (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        datePickerDialog.getDatePicker ( ).setMaxDate (System.currentTimeMillis ( ) - 1000);
        datePickerDialog.show ( );
        datePickerDialog.getButton (DatePickerDialog.BUTTON_NEGATIVE).setTextColor (Color.BLACK);
        datePickerDialog.getButton (DatePickerDialog.BUTTON_POSITIVE).setTextColor (Color.BLACK);


        setListener = new DatePickerDialog.OnDateSetListener ( ) {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                String outputPattern = "dd/MM/yyyy";
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                Date date1 = null;
                try {
                    date1 = outputFormat.parse(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String d = outputFormat.format(date1);
                tv_date.setText (d);
            }
        };
    }
    private void getHistry(String user_id) {
        loadingBar.show ( );
        hList.clear ( );
        wList.clear();
        final HashMap<String, String> params = new HashMap<String, String>( );
        params.put ("user_id", user_id);
        params.put ("matak_id",matka_id);
        module.postRequest(URL_GET_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("AllHistory",response.toString());
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    boolean res = object.getBoolean ("responce");
                    if (res) {
                        JSONArray data = object.getJSONArray ("data");

                        for (int i = 0; i < data.length ( ); i++) {
                            AllHistoryModel model = new AllHistoryModel ( );
                            JSONObject obj = data.getJSONObject (i);
                            model.setId (obj.getString ("id"));
                            model.setMatka_id (obj.getString ("matka_id"));
                            model.setGame_id (obj.getString ("game_id"));
                            model.setUser_id (obj.getString ("user_id"));
                            model.setPoints (obj.getString ("points"));
                            model.setDigits (obj.getString ("digits"));
                            model.setDate (obj.getString ("date"));
                            model.setTime (obj.getString ("time"));
                            model.setBet_type (obj.getString ("bet_type"));
                            model.setStatus (obj.getString ("status"));
                            model.setName (obj.getString ("name"));

                            if (name.equalsIgnoreCase(obj.getString ("name")))
                            {
                                Log.e ("namess", "onResponse: "+name);
                                hList.add (model);
                            }
                        }
                        Log.d ("bid_list", String.valueOf (hList.size ( )));

                        if (hList.size ( ) > 0) {
                            allHistoryAdapter = new AllHistoryAdapter (getActivity (), hList);
                            allHistoryAdapter.notifyDataSetChanged();
                            rec_history.setAdapter(allHistoryAdapter);

                        } else {
                            module.errorToast ("No Matka History Available");
                        }
                    } else {
                        module.errorToast(object.getString("Something Went Wrong"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace ( );
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

    private void getJackpotHistry(String user_id,String matka_id) {
        loadingBar.show ( );
        hList.clear ( );
        wList.clear();
        final HashMap<String, String> params = new HashMap<String, String>( );
        params.put ("us_id",user_id);
        params.put ("matka_id",matka_id);
        Log.e ("jakparams", "getJackpotHistry: "+matka_id+user_id );
        module.postRequest(URL_Jackpot_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("jackHistory",response.toString());
                loadingBar.dismiss();
                try {

                    JSONArray array = new JSONArray(response);
                    for (int i=0; i<array.length();i++)
                    {
                        JSONObject obj = array.getJSONObject(i);


                            AllHistoryModel model = new AllHistoryModel ( );

                            model.setId (obj.getString ("id"));
                            model.setMatka_id (obj.getString ("matka_id"));
                            model.setGame_id (obj.getString ("game_id"));
                            model.setUser_id (obj.getString ("user_id"));
                            model.setPoints (obj.getString ("points"));
                            model.setDigits (obj.getString ("digits"));
                            model.setDate (obj.getString ("date"));
                            model.setTime (obj.getString ("time"));
                            model.setBet_type (obj.getString ("bet_type"));
                            model.setStatus (obj.getString ("status"));
                            model.setName ("Jackpot");

//                            if (obj.getString ("matka_id")>20))
//                            {
                        if(tv_date.getText ().toString ().equals (obj.getString ("date"))){
                            hList.add (model);
                        }
                                Log.e ("namess", "onResponse: "+name);
                                //hList.add (model);
                            //}
                        }
                        Log.d ("bid_list", String.valueOf (hList.size ( )));

                        if (hList.size ( ) > 0) {
                            allHistoryAdapter = new AllHistoryAdapter (getActivity (), hList);
                            allHistoryAdapter.notifyDataSetChanged();
                            rec_history.setAdapter(allHistoryAdapter);

                        } else {
                            module.errorToast ("No Matka History Available");
                        }

                } catch (JSONException e) {
                    e.printStackTrace ( );
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

    private void getStarLineHistory(String user_id) {
        hList.clear ();
        loadingBar.show ();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        module.postRequest(URL_STARLINE_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e ("starline_history", response.toString ( ));
                loadingBar.dismiss ( );
                try{
                    JSONObject object = new JSONObject(response);
                    boolean res = object.getBoolean ("responce");
                    if (res) {
                        JSONArray data = object.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            AllHistoryModel model = new AllHistoryModel();
                            JSONObject obj = data.getJSONObject(i);
                            model.setId(obj.getString("id"));
                            model.setMatka_id(obj.getString("matka_id"));
                            model.setGame_id(obj.getString("game_id"));
                            model.setUser_id(obj.getString("user_id"));
                            model.setPoints(obj.getString("points"));
                            model.setDigits(obj.getString("digits"));
                            model.setDate(obj.getString("date"));
                            model.setTime(obj.getString("time"));
                            model.setName(obj.getString("s_game_time"));
                            model.setStatus(obj.getString("status"));
                            model.setBet_type("");

                            if (name.equalsIgnoreCase(obj.getString("s_game_time"))) {
                                hList.add(model);
                           } else {
//                                module.showToast("error");
                            }
                        }
                        Log.d("star_bid_list", String.valueOf(hList.size()));

                        if (hList.size() > 0) {

                            allHistoryAdapter = new AllHistoryAdapter(getActivity(), hList);
                            allHistoryAdapter.notifyDataSetChanged();
                            rec_history.setAdapter(allHistoryAdapter);
                        } else {
                            module.errorToast("No Starline History Available");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ();
                module.VolleyErrorMessage (error);
            }
        });

    }

    public void getWinHistory(String user_id){
        loadingBar.show();
        hList.clear();
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",user_id);
        module.postRequest(WIN_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("WIN_HISTORY",response);
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("responce")){
                        JSONArray array = object.getJSONArray("data");
                        for (int i=0; i<array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            AllHistoryModel model = new AllHistoryModel();
                            model.setId (obj.getString ("id"));
                            model.setMatka_id (obj.getString ("matka_id"));
                            model.setUser_id (obj.getString ("user_id"));
                            model.setGame_id (obj.getString ("game_id"));
                            model.setDigits (obj.getString ("digits"));
                            model.setPoints (obj.getString ("amt"));
                            model.setBid_id(obj.getString("bid_id"));
                            model.setTime (obj.getString ("time"));
                            model.setBet_type (obj.getString ("type"));
                            model.setDate (obj.getString ("date"));
                            model.setStatus ("win");
                            model.setName (obj.getString ("name"));

                            loadingBar.dismiss();
//
                            Log.e("frty6ui",matka_id+"        "+obj.getString ("matka_id"));
                            if (matka_id.equals(obj.getString ("matka_id")))
                            {
                                hList.add(model);
                            }
                        }

                        Log.d("star_bid_list", String.valueOf(hList.size()));

                        if (hList.size() > 0) {
                            allHistoryAdapter = new AllHistoryAdapter(getActivity(), hList);
                            allHistoryAdapter.notifyDataSetChanged();
                            rec_history.setAdapter(allHistoryAdapter);
                        } else {
                            module.errorToast("No Win History Available");
                        }
                    }
                    else {
                        module.errorToast("No Win History Available");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingBar.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ();
                module.VolleyErrorMessage (error);
            }
        });
    }
    private void getStarlineWinHistory(String user_id) {
        loadingBar.show();
        hList.clear();
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",user_id);
        module.postRequest(STARLINE_WIN_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("STARLINE_WIN_HISTORY",response);
                loadingBar.dismiss ();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("responce")){
                        JSONArray array = object.getJSONArray("data");
                        for (int i=0; i<array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            AllHistoryModel model = new AllHistoryModel();
                            model.setId (obj.getString ("id"));
                            model.setMatka_id (obj.getString ("matka_id"));
                            model.setUser_id (obj.getString ("user_id"));
                            model.setGame_id (obj.getString ("game_id"));
                            model.setDigits (obj.getString ("digits"));
                            model.setPoints (obj.getString ("amt"));
                            model.setBid_id(obj.getString("bid_id"));
                            model.setTime (obj.getString ("time"));
                            model.setBet_type (obj.getString ("type"));
                            model.setDate (obj.getString ("date"));
                            model.setStatus ("win");
                            model.setName (obj.getString ("s_game_time"));
                            loadingBar.dismiss();
                            Log.e("frty6ui",matka_id+"        "+obj.getString ("matka_id"));
                            if (matka_id.equals(obj.getString ("matka_id")))
                            {
                                hList.add(model);
                            }
                        }

                        Log.d("star_bid_list", String.valueOf(hList.size()));

                        if (hList.size() > 0) {

                            allHistoryAdapter = new AllHistoryAdapter(getActivity(), hList);
                            allHistoryAdapter.notifyDataSetChanged();
                            rec_history.setAdapter(allHistoryAdapter);
                        } else {
                            module.errorToast("No Win History Available");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingBar.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ();
                module.VolleyErrorMessage (error);
            }
        });

    }
    public void getjackpotWinHistory(String user_id){
        loadingBar.show();
        hList.clear();
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",user_id);
        module.postRequest(JackpotWIN_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("JackpotWIN_HISTORY",response);
                loadingBar.dismiss ();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("responce")){
                        JSONArray array = object.getJSONArray("data");
                        for (int i=0; i<array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            AllHistoryModel model = new AllHistoryModel();
                            model.setId (obj.getString ("id"));
                            model.setMatka_id (obj.getString ("matka_id"));
                            model.setUser_id (obj.getString ("user_id"));
                            model.setGame_id (obj.getString ("game_id"));
                            model.setDigits (obj.getString ("digits"));
                            model.setPoints (obj.getString ("amt"));
                            model.setBid_id(obj.getString("bid_id"));
                            model.setTime (obj.getString ("time"));
                            model.setBet_type (obj.getString ("type"));
                            model.setDate (obj.getString ("date"));
                            model.setStatus ("win");
                            model.setName (obj.getString ("name"));

                            loadingBar.dismiss();
                            Log.e("frty6ui",matka_id+"        "+obj.getString ("matka_id"));
                            if (matka_id.equals(obj.getString ("matka_id")))
                            {
                                hList.add(model);
                            }
                        }

                        Log.d("star_bid_list", String.valueOf(hList.size()));

                        if (hList.size() > 0) {

                            allHistoryAdapter = new AllHistoryAdapter(getActivity(), hList);
                            allHistoryAdapter.notifyDataSetChanged();
                            rec_history.setAdapter(allHistoryAdapter);
                        } else {
                            module.errorToast("No Win History Available");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingBar.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ();
                module.VolleyErrorMessage (error);
            }
        });
    }
}