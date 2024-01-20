package in.games.gdmatkalive.Fragment;

import android.annotation.SuppressLint;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.SelectGameAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Fragment.AllTable.JOdiFragment;
import in.games.gdmatkalive.Fragment.AllTable.SingleDigirtFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.ChoicePannaFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.DigitBasedJodiFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.FullSangamFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.HalfSangamFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.LeftRightDigitFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.SpDpTpFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.SpMotorFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.TwoDigitPannaFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.JodiFamilyFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.OddEvenFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.PanaFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.PannaFamilyFragment;
import in.games.gdmatkalive.Fragment.GamesFragment.RedBracketFragment;
import in.games.gdmatkalive.Model.SelectGameModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.RecyclerTouchListener;

import static in.games.gdmatkalive.Config.BaseUrls.URL_MATKAGAMES;


public class SelectGameFragment extends Fragment {
    RecyclerView rec_selectGame;
    ArrayList<SelectGameModel> sList, starline_list;
    SelectGameAdapter selectGameAdapter;
    SwipeRefreshLayout swipe;
    Module module;
    LoadingBar loadingBar;
    String matka_name = "", game_id, game_name, matka_id = "", start_time = "", end_time = "", s_num = "", num = "", e_num = "", type = "";

    public SelectGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_select_game, container, false);

        matka_name = getArguments ( ).getString ("matka_name");
        matka_id = getArguments ( ).getString ("m_id");
        start_time = getArguments ( ).getString ("start_time");
        end_time = getArguments ( ).getString ("end_time");
        s_num = getArguments ( ).getString ("s_num");
        num = getArguments ( ).getString ("num");
        e_num = getArguments ( ).getString ("e_num");
        type = getArguments ( ).getString ("type");

        if (Integer.parseInt(matka_id)<20) {
            ((MainActivity) getActivity ( )).setTitles (matka_name);
        } else if (Integer.parseInt(matka_id)>20 && Integer.parseInt(matka_id)<100){
            ((MainActivity) getActivity ( )).setTitles ("Starline Games"+"("+matka_name+")");
        }else {
            ((MainActivity) getActivity ( )).setTitles (matka_name);
        }


        module = new Module (getActivity ( ));
        loadingBar = new LoadingBar (getActivity ( ));
        sList = new ArrayList<> ( );
        rec_selectGame = view.findViewById (R.id.rec_selectGame);
        swipe = view.findViewById (R.id.swipe);

        swipe.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener ( ) {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing ( )) {
                    if (ConnectivityReceiver.isConnected ( )) {
                        if (type.equalsIgnoreCase ("starline")) {
                            getAllSatrlineGames ( );
                        } else if (type.equalsIgnoreCase ("matka")) {
                            getAllGames ( );
                        } else {

//                            allGames ( );
                            getJackpotGmaes();
                        }
                    } else {
                        module.noInternet ( );
                    }
                    swipe.setRefreshing (false);
                }
            }
        });

        Log.e ("type", type);
        if (ConnectivityReceiver.isConnected ( )) {
            if (type.equalsIgnoreCase ("starline")) {
                getAllSatrlineGames ( );
            } else if (type.equalsIgnoreCase ("matka")) {
                getAllGames ( );
            } else {
//                allGames ( );
                getJackpotGmaes();
            }
        } else {
            module.noInternet ( );
        }

        rec_selectGame.addOnItemTouchListener (new RecyclerTouchListener (getActivity ( ), rec_selectGame, new RecyclerTouchListener.OnItemClickListener ( ) {
            @Override
            public void onItemClick(View view, int position) {
                SelectGameModel model = sList.get (position);
                String gameNAme = sList.get (position).getName ( );
                String name = "";
               // Toast.makeText (getContext (), "gh"+gameNAme, Toast.LENGTH_SHORT).show ( );
                Bundle bundle = new Bundle ( );
                Fragment fragment = null;
                if (model.getIs_close ( ).equals ("1")) {
                    module.marketClosed ("Biding is closed for today");
                } else if (type.equals ("jackpot")) {
                    if (gameNAme.equals ("LEFT DIGIT")) {
                        fragment = new LeftRightDigitFragment ( );
                        name = "LEFT";
                        bundle.putString ("jackpot", "LEFT");
                    }
                    else if (gameNAme.equals ("JODI DIGIT")) {
                        fragment = new LeftRightDigitFragment ( );
                        name = "JODI";
                        bundle.putString ("jackpot", "JODI");
                    }else  if (gameNAme.equals ("RIGHT DIGIT")) {
                        fragment = new LeftRightDigitFragment ( );
                        name = "RIGHT";
                        bundle.putString ("jackpot", "RIGHT");
                    }

                } else {

                    switch (gameNAme) {

                        case "SINGLE DIGIT":
                            fragment=new SingleDigirtFragment ();
                           // fragment = new DigitFragment ( );
                            bundle.putString ("type", "SINGLE");

                            break;

                        case "JODI DIGIT":
                            fragment=new JOdiFragment ();

                            //fragment = new DigitFragment ( );
                            bundle.putString ("type", "JODI");
                            break;

                        case "Odd Even":
                            fragment = new OddEvenFragment ( );
                            break;

                        case "SINGLE PANA":
                            fragment = new PanaFragment ( );
                            bundle.putString ("panna", "SINGLE PANNA");
                            break;

                        case "DOUBLE PANA":
                            fragment = new PanaFragment ( );
                            bundle.putString ("panna", "DOUBLE PANNA");

                            break;

                        case "TRIPLE PANA":
                            fragment = new PanaFragment ( );
                            bundle.putString ("panna", "TRIPLE PANNA");

                            break;

                        case "Jodi Family":
                            fragment = new JodiFamilyFragment ( );
                            break;

                        case "Panna Family":
                            fragment = new PannaFamilyFragment ( );
                            break;

                        case "RED BRACKET":
                            fragment = new RedBracketFragment ( );
                            break;
                        case "HALF SANGAM":
                            fragment = new HalfSangamFragment ( );
                            break;
                        case "FULL SANGAM":
                            fragment = new FullSangamFragment ( );
                            break;
                        case "SP DP TP":
                            fragment = new SpDpTpFragment ( );
                            break;
                        case "Choice Panna":
                            fragment = new ChoicePannaFragment ( );
                            break;
                        case "SP MOTOR":
                            fragment = new SpMotorFragment ( );
                            name = "SP MOTOR";
                            break;
                        case "DP MOTOR":
                            fragment = new SpMotorFragment ( );
                            name = "DP MOTOR";
                            break;
                        case "Two Digit Panna":
                            fragment = new TwoDigitPannaFragment ( );
                            break;
                        case "Digit Based Jodi":
                            fragment = new DigitBasedJodiFragment ( );
                            break;

                        case "Left Digits":
                            fragment = new LeftRightDigitFragment ( );
                            name = "left";
                            bundle.putString ("jackpot", "LEFT");
                            break;
                        case "Jackpot Jodi Digits":
                            fragment = new LeftRightDigitFragment ( );
                            name = " jJodi";
                            bundle.putString ("jackpot", "JODI");
                            break;
                        case "Right Digits":
                            fragment = new LeftRightDigitFragment ( );
                            name = "right";
                            bundle.putString ("jackpot", "RIGHT");
//                            fragment.setArguments (bundle);
                            break;

                    }
                }


                if (fragment != null) {

                    ((MainActivity) getActivity ( )).setTitles (gameNAme + " (" + matka_name + ")");
                    if (type.equalsIgnoreCase ("matka")) {
                        bundle.putString ("matka_name", matka_name);
                        bundle.putString ("game_name", model.getGame_name ( ));
                        bundle.putString ("m_id", matka_id);
                        bundle.putString ("game_id", model.getGame_id ( ));
                        bundle.putString ("start_time", start_time);
                        bundle.putString ("end_time", end_time);
                        bundle.putString ("title", gameNAme);

                    } else if (type.equalsIgnoreCase ("starline")) {
                        bundle.putString ("matka_name", matka_name);
                        bundle.putString ("game_name", model.getGame_name ( ));
                        bundle.putString ("m_id", matka_id);
                        bundle.putString ("game_id", model.getGame_id ( ));
                        bundle.putString ("start_time", start_time);
                        bundle.putString ("end_time", end_time);
                        bundle.putString ("title", gameNAme);
                    } else {
                        bundle.putString ("matka_name", matka_name);
                        bundle.putString ("game_name",name);
                        bundle.putString ("m_id", matka_id);
                        bundle.putString ("game_id", model.getGame_id ( ));
                        bundle.putString ("start_time", start_time);
                        bundle.putString ("end_time", end_time);
                        bundle.putString ("title", gameNAme);
                    }


                    bundle.putString ("name", name);

                    Log.e ("selectGame_bundle", bundle.toString ( ));
                    fragment.setArguments (bundle);
                    FragmentManager fragmentManager = getActivity ( ).getSupportFragmentManager ( );
                    fragmentManager.beginTransaction ( ).replace (R.id.frame, fragment).addToBackStack (null).commit ( );
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }

private void getJackpotGmaes()
{
    sList.clear();
    sList.add(new SelectGameModel("2","single_digit","LEFT DIGIT","9.5","10","0","0","0","0","0",R.drawable.img_left_digit));
    sList.add(new SelectGameModel("3","jodi_digits","JODI DIGIT","95","10","0","0","1","0","0",R.drawable.jodi_digit_img));
    sList.add(new SelectGameModel("2","single_digit","RIGHT DIGIT","9.5","10","0","0","0","0","0",R.drawable.img_right_digit));
    if (sList.size()>0) {
        rec_selectGame.setLayoutManager(new GridLayoutManager (getActivity (),2));
        selectGameAdapter = new SelectGameAdapter(getActivity(),sList);
        selectGameAdapter.notifyDataSetChanged();
        rec_selectGame.setAdapter(selectGameAdapter);
    }
}

    public void getAllGames()
    {
        sList.clear();
        loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        module.postRequest(URL_MATKAGAMES, params, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e("matka_games",response.toString());

         try {
                JSONArray array = new JSONArray(response);
                for (int i=0;i<array.length();i++)
                {
                    int betType;

                    JSONObject object = array.getJSONObject(i);
                    SelectGameModel selectGameModel = new SelectGameModel();
                    selectGameModel.setGame_id(object.getString("game_id"));
                    selectGameModel.setGame_name(object.getString("game_name"));
                    selectGameModel.setName(object.getString("name"));
                    selectGameModel.setPoints(object.getString("points"));
                    selectGameModel.setStarline_points(object.getString("starline_points"));
                    selectGameModel.setIs_close(object.getString("is_close"));
                    selectGameModel.setIs_disabled(object.getString("is_disabled"));
                    selectGameModel.setIs_starline_disable(object.getString("is_starline_disable"));
                    selectGameModel.setIs_deleted(object.getString("is_deleted"));
                    String game_id = object.getString("game_id");
                    if (game_id.equals("2")){
                            selectGameModel.setGame_logo (R.drawable.single_digit_img);
                        } else if (game_id.equals("3")){
                            selectGameModel.setGame_logo (R.drawable.jodi_digit_img);
                        } else if (game_id.equals("7")){
                            selectGameModel.setGame_logo (R.drawable.single_panna_img);
                        }else if (game_id.equals("8")){
                            selectGameModel.setGame_logo (R.drawable.double_panna_img);
                        }else if (game_id.equals("9")){
                            selectGameModel.setGame_logo (R.drawable.triple_panna_img);
                        } else if (game_id.equals("12")){
                            selectGameModel.setGame_logo ( R.drawable.half_sangam_img);
                        } else if (game_id.equals("13")){
                            selectGameModel.setGame_logo ( R.drawable.full_sangam_img);
                        }else if (game_id.equals("10")){
                            selectGameModel.setGame_logo ( R.drawable.sp_motor);
                        }else if (game_id.equals("11")){
                            selectGameModel.setGame_logo ( R.drawable.dp_motor);
                        }else if (game_id.equals("4")){
                            selectGameModel.setGame_logo ( R.drawable.img_red_bracket);
                        }else {
                        selectGameModel.setGame_logo ( R.drawable.app_logo);
                    }

                    if (object.getString("is_disabled").equals("1")) {

                    } else {
                        betType=getBetType(getASandC(start_time,end_time));
                        if(object.getString("name").equals ("SINGLE DIGIT")){
                            sList.add (selectGameModel);
                            }
                            if(object.getString("name").equals ("JODI DIGIT")){
                                if (betType==1) {
                                } else {
                                    sList.add (selectGameModel);
                                }
                            }
                            if(object.getString("name").equals ("SINGLE PANA")){
                                    sList.add (selectGameModel);
                            }
                            if(object.getString("name").equals ("DOUBLE PANA")){
                                    sList.add (selectGameModel);
                            }
                            if(object.getString("name").equals ("TRIPLE PANA")){
                                    sList.add (selectGameModel);
                            }
                            if(object.getString("name").equals ("HALF SANGAM")){
                                    sList.add (selectGameModel);
                            }
                            if(object.getString("name").equals ("FULL SANGAM")){
                                    sList.add (selectGameModel);
                            }
                            if(object.getString("name").equals ("SP MOTOR")){
                                    sList.add (selectGameModel);
                            }
                            if(object.getString("name").equals ("DP MOTOR")){
                                    sList.add (selectGameModel);
                            }
                            if(object.getString("name").equals ("RED BRACKET")){
                                if (betType==1) {
                                } else {
                                    sList.add (selectGameModel);
                                }
                            }
                    }
                }
             if (sList.size()>0)
             {
                 rec_selectGame.setLayoutManager(new GridLayoutManager(getActivity(),2));
                 selectGameAdapter = new SelectGameAdapter(getActivity(),sList);
                 selectGameAdapter.notifyDataSetChanged();
                 rec_selectGame.setAdapter(selectGameAdapter);
             }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadingBar.dismiss();
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            module.showVolleyError(error);
            loadingBar.dismiss();
        }
    });

    }

    public void getAllSatrlineGames()
    {
        loadingBar.show();
        sList.clear();
        HashMap<String,String> params = new HashMap<>();
        module.postRequest(URL_MATKAGAMES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("starlinGame",response.toString());

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        SelectGameModel selectGameModel = new SelectGameModel();
                        selectGameModel.setGame_id(object.getString("game_id"));
                        selectGameModel.setGame_name(object.getString("game_name"));
                        selectGameModel.setName(object.getString("name"));
                        selectGameModel.setPoints(object.getString("points"));
                        selectGameModel.setStarline_points(object.getString("starline_points"));
                        selectGameModel.setIs_close(object.getString("is_close"));
                        selectGameModel.setIs_disabled(object.getString("is_disabled"));
                        selectGameModel.setIs_starline_disable(object.getString("is_starline_disable"));
                        selectGameModel.setIs_deleted(object.getString("is_deleted"));
                        String game_id = object.getString("game_id");
                        if (game_id.equals("2")){
                            selectGameModel.setGame_logo (R.drawable.single_digit_img);
                        } else if (game_id.equals("3")){
                            selectGameModel.setGame_logo (R.drawable.jodi_digit_img);
                        } else if (game_id.equals("7")){
                            selectGameModel.setGame_logo (R.drawable.single_panna_img);
                        }else if (game_id.equals("8")){
                            selectGameModel.setGame_logo (R.drawable.double_panna_img);
                        }else if (game_id.equals("9")){
                            selectGameModel.setGame_logo (R.drawable.triple_panna_img);
                        } else if (game_id.equals("12")){
                            selectGameModel.setGame_logo ( R.drawable.half_sangam_img);
                        } else if (game_id.equals("13")){
                            selectGameModel.setGame_logo ( R.drawable.full_sangam_img);
                        }else if (game_id.equals("10")){
                            selectGameModel.setGame_logo ( R.drawable.sp_motor);
                        }else if (game_id.equals("11")){
                            selectGameModel.setGame_logo ( R.drawable.dp_motor);
                        }else if (game_id.equals("4")){
                            selectGameModel.setGame_logo ( R.drawable.img_red_bracket);
                        }else {
                            selectGameModel.setGame_logo ( R.drawable.app_logo);
                        }

                        if (object.getString("is_starline_disable").equals("0"))
                        {
                            sList.add(selectGameModel);
                        }

                    }
                    if (sList.size()>0)
                    {
                        rec_selectGame.setLayoutManager(new GridLayoutManager (getActivity (),2));
                        selectGameAdapter = new SelectGameAdapter(getActivity(),sList);
                        selectGameAdapter.notifyDataSetChanged();
                        rec_selectGame.setAdapter(selectGameAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
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