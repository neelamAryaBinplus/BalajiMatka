package in.games.gdmatkalive.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.gdmatkalive.Config.BaseUrls;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Interfaces.GetAppSettingData;
import in.games.gdmatkalive.Model.IndexResponse;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG=LoginActivity.class.getSimpleName();
TextView tv_register,tv_forget,tv_email,tv_adminnumber;
Button btn_login;
EditText et_number,et_pass;
Module module;
SessionMangement sessionMangement;
LoadingBar loadingBar;
String admin_email,whatsapp_no,msg_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        initview();

    }

    private void initview() {
        module=new Module (LoginActivity.this);
        sessionMangement=new SessionMangement (LoginActivity.this);
        tv_email=findViewById (R.id.tv_email);
        tv_adminnumber=findViewById (R.id.tv_adminnumber);
        tv_register=findViewById (R.id.tv_register);
        tv_register.setOnClickListener (this);
        tv_forget=findViewById (R.id.tv_forget);
        tv_forget.setOnClickListener (this);
        btn_login=findViewById (R.id.btn_login);
        btn_login.setOnClickListener (this);
        et_number=findViewById (R.id.et_number);
        et_pass=findViewById (R.id.et_pass);
         loadingBar=new LoadingBar (LoginActivity.this);
         module.getConfigData(new GetAppSettingData() {
             @Override
             public void getAppSettingData(IndexResponse model) {
                  admin_email = model.getAdm_email();
                  whatsapp_no = model.getMobile();
                  msg_status= model.getMsg_status();
                 tv_email.setText ("Email : "+admin_email);
                 tv_adminnumber.setText ("Number : "+whatsapp_no);
             }
         });
    }

    @Override
    public void onClick(View v) {
        if(v.getId ()==R.id.tv_register){
            Intent intent=new Intent ( LoginActivity.this,RegisterActivity.class );
            startActivity (intent);

        }
        else if(v.getId ()==R.id.tv_forget){
            Log.e("status",msg_status);
            if (msg_status.equals("0"))
            {
                Intent intent=new Intent (LoginActivity.this,ForgotPasswordWhatsppActivity.class );
                startActivity (intent);
            }
            else {
                Intent intent=new Intent ( LoginActivity.this,ForgetActivity.class );
                intent.putExtra("type","f");
                startActivity (intent);
            }
        }
        else if(v.getId ()==R.id.btn_login){
            validation();

        }
    }

    private void validation() {
        String number=et_number.getText ().toString () ;
        String password=et_pass.getText ().toString () ;
        if(et_number.getText ().toString ().isEmpty ()){
            et_number.setError ("Mobile No. Required");
            et_number.requestFocus ();

        }
        else if (number.length()!=10) {
            et_number.setError ("Invalid Mobile No.");
            et_number.requestFocus ( );
        }
        else if (Integer.parseInt (String.valueOf (number.charAt (0))) < 6) {
            et_number.setError ("Invalid Mobile No.");
            et_number.requestFocus ( );
        }

        else if(et_pass.getText ().toString ().isEmpty ()){
            et_pass.setError ("Password Required");
            et_pass.requestFocus ();

        }
        else {
            if (ConnectivityReceiver.isConnected()) {
                getUserLoginRequest (number, password);
                } else {
                    module.noInternet ();
                }
        }
    }


    private void getUserLoginRequest(final String mobile, final String Pass) {
        loadingBar.show ( );

        HashMap<String, String> params = new HashMap<> ( );
        params.put ("mobileno", mobile);
        params.put ("password", Pass);
        module.postRequest (BaseUrls.URL_LOGIN, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss ( );
                Log.e (TAG, response.toString ( ));
                try {

                    JSONObject object = new JSONObject(response);

                    boolean resp=object.getBoolean("responce");
                    if (resp)
                    {
                        JSONObject jsonObject = object.getJSONObject("data");
                        String id=module.checkNull(jsonObject.getString("id").toString());
                        String name=module.checkNull(jsonObject.getString("name").toString());
                        String username=module.checkNull(jsonObject.getString("username").toString());
                        String mobile=module.checkNull(jsonObject.getString("mobileno").toString());
                        String email=module.checkNull(jsonObject.getString("email").toString());
                        String address=module.checkNull(jsonObject.getString("address").toString());
                        String city=module.checkNull(jsonObject.getString("city").toString());
                        String pincode=module.checkNull(jsonObject.getString("pincode").toString());
                        String accno=module.checkNull(jsonObject.getString("accountno").toString());
                        String bank=module.checkNull(jsonObject.getString("bank_name").toString());
                        String ifsc=module.checkNull(jsonObject.getString("ifsc_code").toString());
                        String holder=module.checkNull(jsonObject.getString("account_holder_name").toString());
                        String paytm=module.checkNull(jsonObject.getString("paytm_no").toString());
                        String tez=module.checkNull(jsonObject.getString("tez_no").toString());
                        String phonepay=module.checkNull(jsonObject.getString("phonepay_no").toString());
                        String wallet=module.checkNull(jsonObject.getString("wallet").toString());
                        String dob=module.checkNull(jsonObject.getString("dob").toString());
                        //String gender=module.checkNull(jsonObject.getString("gender").toString());
                        String p = jsonObject.getString("password");
                        if (Pass.equals(p)) {
                            sessionMangement.createLoginSession (id,name,username,mobile,email,dob,address
                                    ,city,pincode,accno,bank,ifsc,holder,paytm,tez,phonepay,wallet);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", jsonObject.getString("username").toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            module.errorToast ("Password is not correct ");
                        }

                    }
                    else {
                        module.errorToast (object.getString("error").toString());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    loadingBar.dismiss();
                }

            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                String msg = module.VolleyErrorMessage (error);
                module.showToast (msg);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}