package in.games.gdmatkalive.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_INDEX;

public class SplashActivity extends AppCompatActivity {
    private final String TAG=SplashActivity.class.getSimpleName();
    int limit=4000;
    TextView tv_vername;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;
    Module module;
    public  int ver_code=0;
    SessionMangement sessionMangement;
    public  String home_text ="",dialog_image="",message="",admin_email="",whatsapp_msg="",whatsapp_no="", withdraw_text="",tagline= "" ,min_add_amount="",link = "" ,app_link="",share_link="",msg_status="",withdraw_no="",
            indexId="",indexMinAmount="",index_w_saturday="",index_w_sunday="",index_w_amount="",index_withdraw_limit="",index_min_wallet_msg="",index_device_config="",index_notice="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initview();

        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String ver = pkgInfo.versionName;
      // tv_vername.setText("Version " +ver);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ConnectivityReceiver.isConnected()) {
                    getApiData();
                } else {
                    module.noInternet ();
                }
            }
        },limit);

    }
    private void initview() {
        tv_vername = findViewById(R.id.tv_vername);
        module=new Module (SplashActivity.this);
        sessionMangement =new SessionMangement (SplashActivity.this);
    }


    public void getApiData() {
        HashMap<String,String> params = new HashMap<String, String> ( );
       module.postRequest (URL_INDEX, params, new Response.Listener<String> ( ) {
           @Override
           public void onResponse(String response) {
               Log.e ("fg", "onResponse: " + response.toString ( ));
               try {
                   JSONObject jsonObject = new JSONObject (response);
                   Boolean result = Boolean.valueOf (jsonObject.getString ("responce"));
                   if (result) {
                       JSONArray arr=jsonObject.getJSONArray("data");
                       JSONObject dataObj=arr.getJSONObject(0);

                       indexId = dataObj.getString("id");
                       indexMinAmount = dataObj.getString("min_amount");
                       index_w_saturday = dataObj.getString("w_saturday");
                       index_w_sunday = dataObj.getString("w_sunday");
                       index_w_amount = dataObj.getString("w_amount");
                       index_withdraw_limit = dataObj.getString("withdraw_limit");
                       index_min_wallet_msg = dataObj.getString("min_wallet_msg");
                       index_device_config = dataObj.getString("device_config");
                       tagline = dataObj.getString ("tag_line");
                       withdraw_text = dataObj.getString ("withdraw_text");
                       withdraw_no = dataObj.getString ("withdraw_no");
                       whatsapp_no= dataObj.getString ("mobile");
                       home_text = dataObj.getString ("home_text").toString ( );
                       min_add_amount = dataObj.getString ("min_wallet");
                       msg_status = dataObj.getString ("msg_status");
                       app_link = dataObj.getString ("app_link");
                       share_link = dataObj.getString ("share_link");
                       ver_code = Integer.parseInt (dataObj.getString ("version"));
                       message = dataObj.getString ("message");
                       index_notice = dataObj.getString("notice");
                       admin_email = dataObj.getString("adm_email");

                       checkAppPermissions();

                   } else {
                       module.errorToast ("Something Went Wrong");
                   }




               } catch (JSONException e) {
                   e.printStackTrace ( );
               }
           }
       }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = module.VolleyErrorMessage (error);
                if (!msg.isEmpty ( )) {
                   module.errorToast ("" + msg);
                }
            }
        });
    }

    public void checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED  ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_NETWORK_STATE)) {
                go_next();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_NETWORK_STATE
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
            }
        } else {
            go_next();
        }
    }
    public void go_next() {
        if(sessionMangement.isLoggedIn())
        {
            sessionMangement.updateDilogStatus(false);
            Intent intent = null;
//            Log.e("dfgh",sessionMangement.getUserDetails().get(KEY_MPIN));
//            if (!sessionMangement.getUserDetails().get(KEY_MPIN).equalsIgnoreCase("null") && !sessionMangement.getUserDetails().get(KEY_MPIN).equals("") )
//            {

              intent = new Intent(SplashActivity.this,MpinLoginActivity.class);
                startActivity(intent);
                finish();
//            }else {
//              intent = new Intent(SplashActivity.this,MainActivity.class);
//
//            }
//            if (intent!=null)
//            {
//                startActivity(intent);
//                finish();
//            }

        }
        else
        {
            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }
    }


}