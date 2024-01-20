package in.games.gdmatkalive.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.gdmatkalive.Adapter.MenuAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Fragment.AccountStatementtFragment;
import in.games.gdmatkalive.Fragment.FundFragment;
import in.games.gdmatkalive.Fragment.GameRateFragment;
import in.games.gdmatkalive.Fragment.GenerateMPIN_Fragment;
import in.games.gdmatkalive.Fragment.HistoryFragment;
import in.games.gdmatkalive.Fragment.HomeFragment;
import in.games.gdmatkalive.Fragment.HowtoPlayFragment;
import in.games.gdmatkalive.Fragment.NoticeboardFragment;
import in.games.gdmatkalive.Fragment.NotificationFragment;
import in.games.gdmatkalive.Fragment.ProfileFragment;
import in.games.gdmatkalive.Model.MenuModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.RecyclerTouchListener;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_MENU;
import static in.games.gdmatkalive.Config.Constants.KEY_NAME;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
DrawerLayout drawer;
ActionBarDrawerToggle toggle;
Toolbar toolbar;
SessionMangement sessionMangement;
TextView tv_title,tv_wallet;
RecyclerView rec_menu;
ImageView img_notification;
ArrayList<MenuModel> mList;
MenuAdapter menuAdapter;
LinearLayout lin_notification,lin_wallet;
Module module;
TextView tv_name;
LoadingBar loadingBar;

ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseMessaging.getInstance().subscribeToTopic("notification");
        initView();

        module.getWalletAmount();
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame,fragment).addToBackStack(null).commit();

        toggle = new ActionBarDrawerToggle(MainActivity.this,drawer,R.string.drawer_open,R.string.drawer_close);
        toggle.getDrawerArrowDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        setTitles(MainActivity.this.getResources().getString(R.string.app_name));
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
                String frgmentName = fragment.getClass().getSimpleName();
                Log.e("fragment",frgmentName);
                if (frgmentName.contains("HomeFragment")) {
                    img_back.setVisibility(View.GONE);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setTitles(MainActivity.this.getResources().getString(R.string.app_name));
                    toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
                    toggle.getDrawerArrowDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    drawer.setDrawerListener(toggle);
                    toggle.syncState();
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.toggle_align_left);
                }else {
                    img_back.setVisibility(View.VISIBLE);
                    toggle.setDrawerIndicatorEnabled(false);
                    toggle.syncState();
                    img_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                }
                if (ConnectivityReceiver.isConnected()) {
                    module.loginStatus(MainActivity.this);
                }else {
                    module.noInternet();
                }
            }
        });


        rec_menu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        getMenu();

        rec_menu.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, rec_menu, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                    module.showToast(mList.get(position).getTitle());
                    String title = mList.get(position).getTitle();
                    Fragment fm = null;
                    Bundle args = new Bundle();

                    switch (title)
                    {
                        case "My Profile":
                            fm=new ProfileFragment ();
                            break;
                        case "Generate MPIN":
                            fm=new GenerateMPIN_Fragment ();
                            break;

                        case "My History":
                            fm=new HistoryFragment ();
                            break;
                        case "Fund":
                            fm = new FundFragment();
                            break;
                        case "Account Statement":
                            fm = new AccountStatementtFragment();
                            break;
                        case "Notification":
                            fm = new NotificationFragment ();
                            break;
                        case "How to Play":
                            fm = new HowtoPlayFragment ();
                            break;
                        case "Game Rate":
                            fm = new GameRateFragment ();
                            break;
                        case "Noticeboard/Rules":
                            fm = new NoticeboardFragment ();
                            break;

//                        case "Setting":
//                            fm = new SettingFragment ();
//                            break;
                        case "Sign Out":
                          sessionMangement.logoutSession (MainActivity.this);
                            break;
                    }

                    if (fm!=null)
                    {
                        setTitles(title);
                        args.putString("title",title);
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame,fm).addToBackStack(null).commit();

                    }

                    drawer.closeDrawer(GravityCompat.START);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initView() {
        module = new Module(MainActivity.this);
        loadingBar = new LoadingBar(MainActivity.this);
        mList = new ArrayList<>();
        tv_name = findViewById(R.id.tv_name);
        img_back = findViewById(R.id.img_back);
        tv_title = findViewById(R.id.tv_title);
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        rec_menu = findViewById(R.id.rec_menu);
        lin_notification = findViewById(R.id.lin_notification);
        tv_wallet = findViewById(R.id.tv_wallet);
        lin_wallet = findViewById(R.id.lin_wallet);
        img_notification=findViewById (R.id.img_notification);
        img_notification.setOnClickListener (this);
        sessionMangement=new SessionMangement (this);

        tv_name.setText(sessionMangement.getUserDetails().get(KEY_NAME));
    }

    void getMenu(){
        loadingBar.show();
        mList.clear();
        HashMap<String,String> params = new HashMap<>();
         module.postRequest(URL_MENU, params, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
             Log.e("menu",response);
             loadingBar.dismiss();
             try {
                 JSONArray array = new JSONArray(response);
                 for (int i=0;i<array.length();i++)
                 {
                     JSONObject object = array.getJSONObject(i);
                     MenuModel menuModel = new MenuModel();
                     menuModel.setId(object.getString("id"));
                     menuModel.setTitle(object.getString("title"));
                     menuModel.setStatus(object.getString("status"));

                     if (object.getString("status").equals("1")){
                         mList.add(menuModel);
                     }
                 }
                 if (mList.size()>0) {
                     menuAdapter = new MenuAdapter(MainActivity.this, mList);
                     menuAdapter.notifyDataSetChanged();
                     rec_menu.setAdapter(menuAdapter);
                 }

             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             loadingBar.dismiss();
             module.showVolleyError(error);
         }
     });
    }

    public void setTitles(String str) {
        tv_title.setText(str);
        tv_title.setSelected(true);
    }

    public void setWallet_Amount(String wallet) {
        try {
            tv_wallet.setText(wallet);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String getWallet() {
        String s = tv_wallet.getText().toString().trim();
        return s;
    }

    @Override
    public void onClick(View v) {
        if(v.getId ()==R.id.img_notification){
            Fragment fm = null;
            fm=new NotificationFragment ();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fm).addToBackStack(null).commit();

        }
    }
}