package in.games.gdmatkalive.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.CREATE_MPIN;
import static in.games.gdmatkalive.Config.BaseUrls.FORGOT_MPIN;
import static in.games.gdmatkalive.Config.BaseUrls.MPIN_LOGIN;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.Constants.KEY_MOBILE;

public class MpinLoginActivity extends AppCompatActivity implements View.OnClickListener {

EditText et_mpin;
Button btn_login,btn_gen_mpin;
LinearLayout lin_forgot,lin_logout;
LoadingBar loadingBar;
SessionMangement sessionMangement;
Module module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin_login);
        initView();
    }

    private void initView() {
        loadingBar = new LoadingBar(MpinLoginActivity.this);
        module = new Module(MpinLoginActivity.this);
        sessionMangement = new SessionMangement(MpinLoginActivity.this);
        lin_forgot = findViewById(R.id.lin_forgot);
        et_mpin = findViewById(R.id.et_mpin);
        btn_login = findViewById(R.id.btn_login);
        lin_logout = findViewById(R.id.lin_logout);
        btn_gen_mpin = findViewById(R.id.btn_gen_mpin);

        btn_login.setOnClickListener(this);
        lin_forgot.setOnClickListener(this);
        lin_logout.setOnClickListener(this);
        btn_gen_mpin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_login:
                onValidation();
                break;
            case R.id.lin_forgot:
                    Dialog dialog ;
                    dialog=new Dialog(MpinLoginActivity.this);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_forgot_mpin);
                    dialog.show();

                    Button btn_gen = (Button) dialog.findViewById (R.id.btn_gen);
                    EditText et_mpin=(EditText)dialog.findViewById (R.id.et_mpin);
                    EditText et_mob=(EditText)dialog.findViewById (R.id.et_mob);
                    et_mob.setText(sessionMangement.getUserDetails().get(KEY_MOBILE));
                    btn_gen.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View v) {
                            String mpin = et_mpin.getText ().toString ();
                            String mob = et_mob.getText().toString();
                            if (mob.isEmpty())
                            {
                                et_mob.setError ("MPIN Required");
                            } else if(mpin.isEmpty ()){
                                et_mpin.setError ("MPIN Required");
                                et_mpin.requestFocus ();
                            }else if (mpin.length()!=4)
                            {
                                et_mpin.setError ("4 Digit MPIN Required");
                                et_mpin.requestFocus ();
                            } else {
                                getForgteMPIN(mob,mpin);
                                dialog.dismiss();
                            }

                        }

                    });

                    dialog.setCanceledOnTouchOutside (true);

                break;
            case R.id.lin_logout:
                sessionMangement.logoutSession (MpinLoginActivity.this);
                break;
            case R.id.btn_gen_mpin:

                generateMpin();
        break;
        }

    }

    private void generateMpin() {
        Dialog dialog ;
        dialog=new Dialog(MpinLoginActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mpin);
        dialog.show();


        Button btn_gen = (Button) dialog.findViewById (R.id.btn_gen);
        EditText et_mpin=(EditText)dialog.findViewById (R.id.et_mpin);
        btn_gen.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                String mpin = et_mpin.getText ().toString ();
                if(et_mpin.getText ().toString ().isEmpty ()){
                    et_mpin.setError ("MPIN Required");
                    et_mpin.requestFocus ();
                }else if (mpin.length()!=4)
                {
                    et_mpin.setError ("4 Digit MPIN Required");
                    et_mpin.requestFocus ();
                } else {
//                        Toast.makeText (getContext (), "Success", Toast.LENGTH_SHORT).show ( );
//                       dialog.dismiss ();
                    getMPINData(mpin,et_mpin);
                    dialog.dismiss();
                }
            }
        });

        dialog.setCanceledOnTouchOutside (true);

    }

    private void onValidation() {
        String mpin = et_mpin.getText ().toString ();
        if(et_mpin.getText ().toString ().isEmpty ()){
            et_mpin.setError ("MPIN Required");
            et_mpin.requestFocus ();
        }else if (mpin.length()!=4)
        {
            et_mpin.setError ("4 Digit MPIN Required");
            et_mpin.requestFocus ();
        } else {

            mpinLogin(mpin);
        }
    }

    private void mpinLogin(final String mpin) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",mpin);
//        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        params.put("mobile",sessionMangement.getUserDetails().get(KEY_MOBILE));

        module.postRequest(MPIN_LOGIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create_mpin",response.toString());
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("success"))
                    {
                        Intent i = new Intent(MpinLoginActivity.this,MainActivity.class);
                        startActivity(i);
                    }else {
                        module.errorToast(object.getString("data"));
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
    private void getForgteMPIN(String mob, String mpin) {
            loadingBar.show();
            HashMap<String, String> params = new HashMap<>();
            params.put("mpin",mpin);
            params.put("mobile",mob);

            module.postRequest(FORGOT_MPIN, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("create_mpin",response.toString());
                    loadingBar.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("success"))
                        {
                            module.successToast(object.getString("message"));
//                        sessionMangement.updateMpin(mpin);
                        }else {
                            module.errorToast("Something went wrong");
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

    private void getMPINData(final String mpin, EditText et_mpin) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",mpin);
        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        params.put("mobile",sessionMangement.getUserDetails().get(KEY_MOBILE));

        module.postRequest(CREATE_MPIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create_mpin",response.toString());
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("success"))
                    {
                        module.successToast(object.getString("message"));
//                        sessionMangement.updateMpin(mpin);
                    }else {
                        module.errorToast("Something went wrong");
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
}