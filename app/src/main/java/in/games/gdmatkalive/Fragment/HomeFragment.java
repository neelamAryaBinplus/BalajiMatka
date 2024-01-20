package in.games.gdmatkalive.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import in.games.gdmatkalive.Adapter.MatkaAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Interfaces.GetAppSettingData;
import in.games.gdmatkalive.Model.IndexResponse;
import in.games.gdmatkalive.Model.MatkaModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;
import static in.games.gdmatkalive.Config.BaseUrls.URL_Matka;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private final String TAG= HomeFragment.class.getSimpleName();
SwipeRefreshLayout swipe;
LinearLayout lin_home,lin_whatsapp,lin_play;
CircleImageView civ_logo;
LoadingBar loadingBar;
TextView tv_whatsapp,tv_msg;
Button btn_starline,btn_jackpot,btn_addFund,btn_withdrawFund;
RelativeLayout rel_matka;
RecyclerView rec_matka;
ArrayList<MatkaModel> mList;
MatkaAdapter matkaAdapter;
Module module;
float version_code ;
SessionMangement sessionMangement;
public String app_link="",home_text="",message="",whatsapp_msg="",whatsapp_no="",withdraw_no="";
int ver_code;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initView(root);
        module.getWalletAmount();


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    if (ConnectivityReceiver.isConnected())
                    {
                        allMatkaGames ();
                    }else {
                        module.noInternet ();
                    }
                    module.getWalletAmount();
                    swipe.setRefreshing(false);
                }
            }
        });

        return root;
    }


    private void initView(View root) {
        mList = new ArrayList<>();
        swipe = root.findViewById(R.id.swipe);
        lin_home = root.findViewById(R.id.lin_home);
        lin_whatsapp = root.findViewById(R.id.lin_whatsapp);
        lin_play = root.findViewById(R.id.lin_play);
        civ_logo = root.findViewById(R.id.civ_logo);
        tv_whatsapp = root.findViewById(R.id.tv_whatsapp);
        tv_whatsapp.setOnClickListener (this);
        tv_msg = root.findViewById(R.id.tv_msg);
        btn_starline = root.findViewById(R.id.btn_starline);
        btn_jackpot = root.findViewById(R.id.btn_jackpot);
        rel_matka = root.findViewById(R.id.rel_matka);
        rec_matka = root.findViewById(R.id.rec_matka);
        btn_addFund = root.findViewById(R.id.btn_addFund);
        btn_withdrawFund = root.findViewById(R.id.btn_withdrawFund);

        btn_jackpot.setOnClickListener(this);
        btn_starline.setOnClickListener(this);
        lin_whatsapp.setOnClickListener(this);
        btn_withdrawFund.setOnClickListener(this);
        btn_addFund.setOnClickListener(this);
        module=new Module (getContext ());
        loadingBar=new LoadingBar (getContext ());
        sessionMangement =new SessionMangement (getContext ());
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                tv_msg.setText (model.getHome_text());
                tv_whatsapp.setText (model.getMobile());
                app_link = model.getApp_link();
                home_text =model.getHome_text();
                message = model.getMessage();
                whatsapp_no = model.getMobile();
                withdraw_no = model.getWithdraw_no();
                ver_code = Integer.parseInt(model.getVersion());
                try {
                    PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    version_code = pInfo.versionCode;
                    Log.e("Homefragment",""+ver_code+" - "+version_code);

                    if(version_code==ver_code) {

                    } else {
                        showUpdateDialog();
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Dialog dialog = new Dialog (getActivity());
                    dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable (new ColorDrawable(0));
                    dialog.setContentView (R.layout.dialog_exit);
                    dialog.show();

                    TextView tv_msg;
                    RelativeLayout rel_ok, rel_close;

                    tv_msg = dialog.findViewById (R.id.tv_msg);
                    rel_ok = dialog.findViewById (R.id.rel_ok);
                    rel_close = dialog.findViewById (R.id.rel_close);

                    tv_msg.setText ("Are you sure want to exit?");

                    rel_ok.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View v) {
                           getActivity().finishAffinity();
                        }
                    });
                    rel_close.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss ( );
                        }
                    });


                    dialog.setCanceledOnTouchOutside (false);
                    return true;
                }
                return false;
            }
        });


    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        Fragment fragment = null;
      switch (v.getId())
      {
          case R.id.btn_jackpot:
              fragment = new JackpotGameFragment();
              break;

          case R.id.btn_starline:
               fragment = new StarlineGamesFragment();
              break;

          case R.id.btn_addFund:
              fragment = new AddFundFragment();
              break;

          case R.id.btn_withdrawFund:
              fragment = new WithdrawalFundFragment();
              break;

          case R.id.lin_whatsapp:
              module.whatsapp(whatsapp_no,"Hi! Admin");
              break;
      }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart ( );
        if (ConnectivityReceiver.isConnected()) {
            module.loginStatus (getActivity());
            allMatkaGames ();
        }else {
            module.noInternet ();
        }

    }

    private void allMatkaGames() {
        mList.clear();
        loadingBar.show ();
        HashMap<String,String> params = new HashMap<> (  );
        module.postRequest (URL_Matka, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e (TAG, "onResponse: "+response );
                loadingBar.dismiss();

                    try
                    {
                        JSONArray datay=new JSONArray(response);
                        for(int i=0; i<datay.length();i++) {

                            JSONObject jsonObject = datay.getJSONObject(i);
                            MatkaModel matkasObjects = new MatkaModel();
                            matkasObjects.setId(jsonObject.getString("id"));
                            matkasObjects.setName(jsonObject.getString("name"));
                            matkasObjects.setStart_time(jsonObject.getString("start_time"));
                            matkasObjects.setEnd_time(jsonObject.getString("end_time"));
                            matkasObjects.setStarting_num(jsonObject.getString("starting_num"));
                            matkasObjects.setNumber(jsonObject.getString("number"));
                            matkasObjects.setEnd_num(jsonObject.getString("end_num"));
                            matkasObjects.setBid_start_time(jsonObject.getString("bid_start_time"));
                            matkasObjects.setBid_end_time(jsonObject.getString("bid_end_time"));
                            matkasObjects.setCreated_at(jsonObject.getString("created_at"));
                            matkasObjects.setUpdated_at(jsonObject.getString("updated_at"));
                            matkasObjects.setSat_start_time(jsonObject.getString("sat_start_time"));
                            matkasObjects.setSat_end_time(jsonObject.getString("sat_end_time"));
                            matkasObjects.setStatus(jsonObject.getString("status"));
                           // matkasObjects.setLoader(jsonObject.getString("loader"));
                           // matkasObjects.setText(jsonObject.getString("text"));
                            //matkasObjects.setText_status(jsonObject.getString("text_status"));
                            mList.add(matkasObjects);
                        }
                        if(mList.size()>0)
                        { rec_matka.setLayoutManager(new GridLayoutManager (getActivity(),2));
                            matkaAdapter = new MatkaAdapter(getActivity(),mList);
                            matkaAdapter.notifyDataSetChanged();
                            rec_matka.setAdapter(matkaAdapter);
                            matkaAdapter.notifyDataSetChanged ();
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                      loadingBar.dismiss();
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
    void showUpdateDialog()
    {
        final Dialog dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_updation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width= WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button btnCancel = dialog.findViewById(R.id.cancel);
        Button btnUpdate = dialog.findViewById(R.id.update);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().finishAffinity();

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = null;
                if (app_link==null || app_link.isEmpty()) {
                    url ="https://play.google.com/store?hl=en_IN";
                } else {
                    url = app_link;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        dialog.setCancelable (false);
        dialog.show();
    }
}