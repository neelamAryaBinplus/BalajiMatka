package in.games.gdmatkalive.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.ForgetWhatsappModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;

import static in.games.gdmatkalive.Config.BaseUrls.URL_FORGET_PASSWORD_WHATSAPP;

public class ForgotPasswordWhatsppActivity extends AppCompatActivity implements View.OnClickListener {
SwipeRefreshLayout swipe;
TextView tv_text,tv_mob;
Button btn_forgot;
LoadingBar loadingBar;
LinearLayout lin_whatsapp;
Module module;
ArrayList<ForgetWhatsappModel> wList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_whatspp);
        initView();
        whatsappforgot();
    }

    private void whatsappforgot() {

            loadingBar.show();
            HashMap<String,String> params = new HashMap<>();
            module.postRequest(URL_FORGET_PASSWORD_WHATSAPP, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loadingBar.dismiss();
                    Log.e("whatsappvv",response.toString());
                    try {
                        JSONArray array = new JSONArray(response);
                        ForgetWhatsappModel whatsappModel = new ForgetWhatsappModel();
                        JSONObject object = array.getJSONObject(0);
                        whatsappModel.setId(object.getString("id"));
                        whatsappModel.setWhatsapp_no(object.getString("whatsapp_no"));
                        whatsappModel.setWhatsapp_msg(object.getString("whatsapp_msg"));
                        whatsappModel.setInfo_text(object.getString("info_text"));
                        whatsappModel.setNo_visible(object.getString("no_visible"));
                        whatsappModel.setStatus(object.getString("status"));
                        wList.add(whatsappModel);


                        btn_forgot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent (Intent.ACTION_VIEW, Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s&text=%s",wList.get(0).getWhatsapp_no(),wList.get(0).getWhatsapp_msg()))));
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingBar.dismiss();
                }
            });
        }


    private void initView() {
        wList=new ArrayList<> (  );
        swipe = findViewById(R.id.swipe) ;
        tv_text = findViewById(R.id.tv_text) ;
        tv_mob = findViewById(R.id.tv_mob) ;
        btn_forgot = findViewById(R.id.btn_forgot) ;
        lin_whatsapp = findViewById(R.id.lin_whatsapp) ;
        module=new Module (ForgotPasswordWhatsppActivity.this);
        loadingBar=new LoadingBar (ForgotPasswordWhatsppActivity.this);
       }

    @Override
    public void onClick(View v) {

    }
}