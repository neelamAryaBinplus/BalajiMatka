package in.games.gdmatkalive.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Config.SmsReceiver;
import in.games.gdmatkalive.Interfaces.GetAppSettingData;
import in.games.gdmatkalive.Interfaces.SmsListener;
import in.games.gdmatkalive.Model.IndexResponse;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;
import static in.games.gdmatkalive.Config.BaseUrls.URL_GENERATE_OTP;
import static in.games.gdmatkalive.Config.BaseUrls.URL_VERIFICATION;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG=ForgetActivity.class.getSimpleName();
EditText et_number,et_otp;
Button btn_mobile,btn_otp,btn_resend;
Module module;
LoadingBar loadingBar;
TextView tv_msg,tv_email,tv_timer;
String myOtp="";
public static final String OTP_REGEX = "[0-9]{3,6}";
CountDownTimer countDownTimer,cTimer ;
LinearLayout lin_otp,lin_mobile;
String otp="",mobile="",type="" ;
String admin_email,message,msg_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_forget);
        initview();

    }

    private void initview() {
        et_number=findViewById (R.id.et_number);
        et_otp=findViewById (R.id.et_otp);
        btn_mobile=findViewById (R.id.btn_mobile);
        btn_mobile.setOnClickListener (this);
        btn_otp=findViewById (R.id.btn_otp);
        btn_resend=findViewById (R.id.btn_resend);
        lin_mobile=findViewById (R.id.lin_mobile);
        lin_otp=findViewById (R.id.lin_otp);
        tv_email=findViewById (R.id.tv_email);
        tv_msg=findViewById (R.id.tv_msg);
        tv_timer=findViewById (R.id.tv_timer);
        btn_otp.setOnClickListener (this);
        btn_resend.setOnClickListener (this);
        loadingBar=new LoadingBar (this);
        setCounterTimer(120000,tv_timer);
        type=getIntent().getStringExtra("type");
        module=new Module (ForgetActivity.this);
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                admin_email = model.getAdm_email();
                message = model.getMessage();
                msg_status = model.getMsg_status();
                tv_email.setText ("Email : "+admin_email);
                tv_msg.setText (message);
                tv_msg.setVisibility (View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        mobile=et_number.getText ().toString ();
        otp=module.getRandomKey(6);
        if(v.getId ()==R.id.btn_mobile){

            if(et_number.getText ().toString ().isEmpty ()){
                et_number.setError ("Mobile No. Required");
                et_number.requestFocus ();

            }
            else if (mobile.length()!=10) {
                et_number.setError ("Invalid Mobile No.");
                et_number.requestFocus ( );
            }
            else if (Integer.parseInt (String.valueOf (mobile.charAt (0))) < 6) {
                et_number.setError ("Invalid Mobile No.");
                et_number.requestFocus ( );
            }
            else {
                otp=module.getRandomKey(6);
                if (type.equalsIgnoreCase("f")) {
                    sendOtpforPass(mobile, otp, URL_GENERATE_OTP);
                } else {
                    sendOtpforPass(mobile, otp, URL_VERIFICATION);
                }
            }
        }
        else if(v.getId ()==R.id.btn_resend){

            if(et_number.getText ().toString ().isEmpty ()){
                et_number.setError ("Mobile No. Required");
                et_number.requestFocus ();

            }
            else {
                otp=module.getRandomKey(6);
                if (type.equalsIgnoreCase("f")) {
                    sendOtpforPass(mobile, otp, URL_GENERATE_OTP);
                } else {
                    sendOtpforPass(mobile, otp, URL_VERIFICATION);
                }
            }
        }
       else if(v.getId ()==R.id.btn_otp){

           String etOtp = et_otp.getText ().toString ();
            if(etOtp.isEmpty ()){
                et_otp.setError ("Otp Required");
                et_otp.requestFocus ();

            }
            else if(etOtp.length()<4)
            {
                module.errorToast ("OTP is too short");
                et_otp.requestFocus();
            }
            else {
//                if(tv_timer.getText().toString().equalsIgnoreCase("timeout"))
//                {
//                    common.showToast("Timeout");
//                }else{}

                if (etOtp.equals(myOtp)) {

//                        module.showToast("forget password otp");
                        Intent intent=new Intent(ForgetActivity.this,PasschangeActivity.class);
                        intent.putExtra("mobile",mobile);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();
                }
                else
                {
                    module.errorToast("Wrong Otp Entered");
                }

            }
        }
    }

    private void sendOtpforPass(String mobile, String otp,String url) {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("mobile",mobile);
        params.put("otp",otp);
        if(!otp.isEmpty ()){
            et_otp.getText ().clear ();
        }
        Log.e ( "sendOtpforPass: ",params.toString () );
         module.postRequest (url, params, new Response.Listener<String> ( ) {
             @Override
             public void onResponse(String response) {
                 loadingBar.dismiss();
                 Log.e (TAG, "forrgte"+response );
                 loadingBar.dismiss();
                 JSONObject jsonObject= null;
                 try
                 {
                     jsonObject = new JSONObject (String.valueOf (response));
                     String res= jsonObject.getString ("status");

                     if(res.equalsIgnoreCase("success"))
                     {

                         lin_otp.setVisibility(View.VISIBLE);
                         btn_otp.setVisibility (View.VISIBLE);
                         Log.e ("check_otp", "onResponse: "+msg_status+" :: "+otp+" :: "+mobile+" :: "+type );
                         if(msg_status.equals("0"))
                         {
                             if(countDownTimer!=null){
                                 countDownTimer.cancel ();
                             }
                             countDownTimer=new CountDownTimer (5000,1000) {
                                 @Override
                                 public void onTick(long l) {

                                 }

                                 @Override
                                 public void onFinish() {
                                     //  et_otp.setOtp (otp);
                                     et_otp.setText (otp);
                                     myOtp=otp;
                                 }
                             }.start ();

                         }
                         else
                         {
                             getSmsOtp();
                         }
                         lin_mobile.setVisibility(View.GONE);
                         setCounterTimer(120000,tv_timer);
                         module.successToast(jsonObject.getString("message"));
                         loadingBar.dismiss();
                     }
                     else
                     {

                         module.errorToast (jsonObject.getString("message"));
                     }
                 }
                 catch (Exception ex)
                 {
                     ex.printStackTrace();
                     module.showToast ("Something went wrong");
                 }
             }

         }, new Response.ErrorListener ( ) {
             @Override
             public void onErrorResponse(VolleyError error) {
                 loadingBar.dismiss();
                 error.printStackTrace();
                 module.showVolleyError(error);
             }
         });
    }
    public void getSmsOtp()
    {
        try
        {


            SmsReceiver.bindListener(new SmsListener () {
                @Override
                public void messageReceived(String messageText) {

                    //From the received text string you may do string operations to get the required OTP
                    //It depends on your SMS format
                    Log.e("Message",messageText);
                    // Toast.makeText(SmsVerificationActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();

                    // If your OTP is six digits number, you may use the below code

                    Pattern pattern = Pattern.compile(OTP_REGEX);
                    Matcher matcher = pattern.matcher(messageText);
                    String otp="";
                    while (matcher.find())
                    {
                        otp = matcher.group();
                    }

                    if(!(otp.isEmpty() || otp.equals("")))
                    {
//                        et_otp.setOtp (otp);
                        et_otp.setText (otp);


                    }

                    //           Toast.makeText(SmsVerificationActivity.this,"OTP: "+ otp ,Toast.LENGTH_LONG).show();

                }
            });
        }
        catch (Exception ex)
        {
            // Toast.makeText(SmsVerificationActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void  setCounterTimer(long diff,final TextView txt_timer)
    {
        txt_timer.setTextColor(getResources().getColor(R.color.red));

        if(cTimer!=null){
            cTimer.cancel ();
        }
        cTimer = new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), " %02d : %02d : %02d ",

                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                txt_timer.setText(text);

            }

            @Override
            public void onFinish() {
                //otp="";
                txt_timer.setText("Timeout");
                txt_timer.setTextColor(getResources().getColor(R.color.white));
                if(btn_resend.getVisibility() == View.GONE)
                {   // btn_otp.setVisibility (View.GONE);
                    btn_otp.setVisibility (View.VISIBLE);
                    btn_resend.setVisibility(View.VISIBLE);
                }

            }
        }.start();

    }
}