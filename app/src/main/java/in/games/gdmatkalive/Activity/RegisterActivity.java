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

import org.json.JSONObject;

import java.util.HashMap;

import in.games.gdmatkalive.Config.BaseUrls;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG=RegisterActivity.class.getSimpleName();
    EditText et_fname, et_lname, et_mobile, et_email, et_userid, et_password;
    Button btn_register;
    Module module;
    LoadingBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_register);
        initview ( );
    }

    private void initview() {
        et_fname = findViewById (R.id.et_fname);
        et_lname = findViewById (R.id.et_lname);
        et_mobile = findViewById (R.id.et_mobile);
        et_email = findViewById (R.id.et_email);
        et_userid = findViewById (R.id.et_userid);
        et_password = findViewById (R.id.et_password);
        module=new Module (RegisterActivity.this);
        btn_register = findViewById (R.id.btn_register);
        btn_register.setOnClickListener (this);
        loadingBar=new LoadingBar (this);
    }


    @Override
    public void onClick(View v) {
        String number=et_mobile.getText ().toString () ;
        if (v.getId ( ) == R.id.btn_register) {
            String phone_value=et_mobile.getText().toString().trim();

            int len=phone_value.length();

            if (et_fname.getText ( ).toString ( ).isEmpty ( )) {
                et_fname.setError ("First Name Required");
                et_fname.requestFocus ();
            }
           else if (et_lname.getText ( ).toString ( ).isEmpty ( )) {
                et_lname.setError ("Last Name Required");
                et_lname.requestFocus ();
            }
            else if (et_mobile.getText ( ).toString ( ).isEmpty ( )) {
                et_mobile.setError ("Mobile No. Required");
                et_mobile.requestFocus ();
            }

            else if (Integer.parseInt (String.valueOf (number.charAt (0))) < 6) {
                et_mobile.setError ("Invalid Mobile No.");
                et_mobile.requestFocus ( );
            }

           else if(len!=10)
            {

               et_mobile.setError ("Invalid Number");
                et_mobile.requestFocus ();
            }

            else if (et_email.getText ( ).toString ( ).isEmpty ( )) {
                et_email.setError ("Email Address Required");
                et_email.requestFocus ();
            } else if (et_userid.getText ( ).toString ( ).isEmpty ( )) {
                et_userid.setError ("User ID Required");
                et_userid.requestFocus ();
            }
            else if (et_password.getText ( ).toString ( ).isEmpty ( )) {
                et_password.setError ("Password Required");
                et_password.requestFocus ();
            }
            else
            {
                if(ConnectivityReceiver.isConnected ()){
                register(phone_value);
                }
                else {
                    module.noInternet ();
                }
//                Intent intent=new Intent ( RegisterActivity.this,LoginActivity.class );
//                Toast.makeText (this, "Success", Toast.LENGTH_SHORT).show ( );
//                startActivity (intent);
            }


        }
    }



    private void register(String phone_value) {

        loadingBar.show();
        final String fname=et_fname.getText().toString().trim();
        final String lname=et_lname.getText().toString().trim();
        final String fmobile=phone_value;
        final String email=et_email.getText().toString().trim();
        final String uid=et_userid.getText().toString().trim();
        final String fpass=et_password.getText().toString().trim();


        HashMap<String,String> params=new HashMap<>();
        params.put("key","1");
        params.put("username",fname);
        params.put("name",lname);
        params.put("mobile",fmobile);
        params.put("email",email);
        params.put("userid",uid);
        params.put("password",fpass);
//        params.put("user_email",email);


        module.postRequest (BaseUrls.URL_REGISTER, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    boolean resp = object.getBoolean("responce");
                    Log.e (TAG, "onResponse: "+response);
                    if (resp) {
                        loadingBar.dismiss();
                        module.successToast (object.getString("message").toString());
                        Intent intent = new Intent (RegisterActivity.this, LoginActivity.class);
                        startActivity (intent);
//                        finish ( );

                    } else {
                        loadingBar.dismiss ();
                        module.errorToast (object.getString("error").toString());
                    }
                } catch (Exception ex) {
                    loadingBar.dismiss ();
                    ex.printStackTrace ( );
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

}