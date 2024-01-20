package in.games.gdmatkalive.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;

import static in.games.gdmatkalive.Config.BaseUrls.URL_UPDATE_PASS;

public class PasschangeActivity extends AppCompatActivity implements View.OnClickListener {
EditText et_npass,et_cpass;
Button btn_submit;
    String mobile="";
    LoadingBar loadingBar;
    Module module;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_passchange);
        initview();
    }

    private void initview() {
        et_npass=findViewById (R.id.et_npass);
        et_cpass=findViewById (R.id.et_cpass);
        btn_submit=findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        mobile=getIntent().getStringExtra("mobile");
        module=new Module (PasschangeActivity.this);
        loadingBar=new LoadingBar (PasschangeActivity.this);
    }

    @Override
    public void onClick(View v) {
      String npass= et_npass.getText ( ).toString ( );
        String cpass= et_cpass.getText ( ).toString ( );
        if (v.getId ( ) == R.id.btn_submit) {
            if (et_npass.getText ( ).toString ( ).isEmpty ( )) {
                et_npass.setError ("Required");
                et_npass.requestFocus ( );

            } else if (et_cpass.getText ( ).toString ( ).isEmpty ( )) {
                et_cpass.setError ("Required");
                et_cpass.requestFocus ( );
            }
            else if (!(npass.equals (cpass))) {
                module.errorToast ("Password must be matched");
                et_npass.requestFocus ( );
            }
            else {

                    if (ConnectivityReceiver.isConnected()) {
                        updatePassword(mobile, npass);
                    }
                    else
                    {
                       module.noInternet ();
                    }
            }
        }
        
    }

    private void updatePassword(String mobile, String npass) {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("mobile",mobile);
        params.put("password",npass);
        module.postRequest (URL_UPDATE_PASS, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {

                Log.e("password",response.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject (String.valueOf (response));
                    String res = jsonObject.getString ("status");

                    if (res.equalsIgnoreCase ("success")) {
                        loadingBar.dismiss();
                        module.successToast(jsonObject.getString("message"));
                        Intent intent=new Intent(PasschangeActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        loadingBar.dismiss();
                        module.errorToast (jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace ( );

                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }
}