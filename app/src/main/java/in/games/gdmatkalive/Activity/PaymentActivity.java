package in.games.gdmatkalive.Activity;


import static in.games.gdmatkalive.Config.BaseUrls.URL_RAZORPAY_PAYMENT;
import static in.games.gdmatkalive.Config.Constants.KEY_EMAIL;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.Constants.KEY_MOBILE;
import static in.games.gdmatkalive.Fragment.AddFundFragment.desc;
import static in.games.gdmatkalive.Fragment.AddFundFragment.razor_pay;
import static in.games.gdmatkalive.Fragment.AddFundFragment.razorpay_email;
import static in.games.gdmatkalive.Fragment.AddFundFragment.s_amount;
import static in.games.gdmatkalive.Fragment.AddFundFragment.s_transaction_id;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.database.annotations.NotNull;
import com.razorpay.PaymentMethodsCallback;
import com.razorpay.PaymentResultListener;
import com.razorpay.Razorpay;
import com.razorpay.RazorpayWebViewClient;
import com.razorpay.ValidationListener;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.wangsun.upi.payment.UpiPayment;
import com.wangsun.upi.payment.model.PaymentDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;


public class PaymentActivity extends AppCompatActivity implements View.OnClickListener , PaymentResultListener {

    ImageView img_logo,img_close,img_edit,iv_gpay;
    TextView tv_name,tv_amount,tv_mob,tv_email;
    LinearLayout lin_Gpay,lin_PhnPe,lin_paytm,lin_other;
    LinearLayout lin_bg_Gpay,lin_bg_phnpe,lin_bg_paytm,lin_bg_other;
    CheckBox check_Gpay,check_PhnPe,check_paytm,check_other;
    String paymentMode="";
    Button btn_pay;
    private JSONObject payload;
    private Razorpay razorpay;
    boolean upi_flag=false;
    private EasyUpiPayment mEasyUpiPayment;
    Module module;
    LoadingBar loadingBar;
    SessionMangement sessionMangement;
    WebView payment_webview;
    LinearLayout lin_main;
    String order_id_value="";
    ArrayList<String> newList = new ArrayList<String>();
    public static String trans_id="",amount="",u_email="",u_phone="",u_transaction_status="",u_name="",company_logo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();

        initRazorpay();
        createWebView();

        newList.addAll(UpiPayment.getExistingUpiApps(PaymentActivity.this));
//        if(!newList.contains ("phonepe"))
//        {
//            lin_PhnPe.setVisibility (View.GONE);
//        }
//        if(!newList.contains ("gpay"))
//        {
//            lin_Gpay.setVisibility (View.GONE);
//        }
//        if(!newList.contains ("paytm"))
//        {
//            lin_paytm.setVisibility (View.GONE);
//        }



    }
    private void initRazorpay() {
        razorpay = new Razorpay (PaymentActivity.this);

        razorpay.getPaymentMethods(new PaymentMethodsCallback () {
            @Override
            public void onPaymentMethodsReceived(String result) {

                /**
                 * This returns JSON data
                 * The structure of this data can be seen at the following link:
                 * https://api.razorpay.com/v1/methods?key_id=rzp_test_1DP5mmOlF5G5ag
                 *
                 */
                Log.d("Result", "" + result);
                // inflateLists(result);
            }

            @Override
            public void onError(String error) {
                Log.d("Get Payment error",error);
            }
        });

//        razorpay.isValidVpa(razor_pay, new ValidateVpaCallback () {
//
//
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                Toast.makeText(PaymentActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFailure() {
//                Toast.makeText(PaymentActivity.this, "Error in validating", Toast.LENGTH_LONG).show();
//            }
//        });

    }
    public void initView() {
        payment_webview=(WebView) findViewById (R.id.payment_webview);
        lin_main=findViewById (R.id.lin_main);


        sessionMangement = new SessionMangement(PaymentActivity.this);
        module = new Module(PaymentActivity.this);
        loadingBar = new LoadingBar(PaymentActivity.this);
        img_logo = findViewById(R.id.img_logo);
        img_close = findViewById(R.id.img_close);
        img_edit = findViewById(R.id.img_edit);
        tv_name = findViewById(R.id.tv_name);
        tv_name = findViewById(R.id.tv_name);
        iv_gpay=findViewById (R.id.iv_gpay);

        tv_amount = findViewById(R.id.tv_amount);
        tv_mob = findViewById(R.id.tv_mob);
        tv_email = findViewById(R.id.tv_email);
        lin_Gpay = findViewById(R.id.lin_Gpay);
        lin_PhnPe = findViewById(R.id.lin_PhnPe);
        lin_paytm = findViewById(R.id.lin_paytm);
        lin_other = findViewById(R.id.lin_other);

        lin_bg_Gpay = findViewById(R.id.lin_bg_Gpay);
        lin_bg_phnpe = findViewById(R.id.lin_bg_phnpe);
        lin_bg_paytm = findViewById(R.id.lin_bg_paytm);
        lin_bg_other = findViewById(R.id.lin_bg_other);

        check_Gpay = findViewById(R.id.check_Gpay);
        check_PhnPe = findViewById(R.id.check_PhnPe);
        check_paytm = findViewById(R.id.check_paytm);
        check_other = findViewById(R.id.check_other);
        btn_pay = findViewById(R.id.btn_pay);
        iv_gpay.setOnClickListener (this);
        lin_Gpay.setOnClickListener(this);
        lin_bg_Gpay.setOnClickListener(this);
        lin_bg_phnpe.setOnClickListener(this);
        lin_PhnPe.setOnClickListener(this);
        lin_paytm.setOnClickListener(this);
        lin_other.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        img_close.setOnClickListener(this);
        img_edit.setOnClickListener(this);
        order_id_value=getIntent ().getStringExtra ("order_id_value");
        amount=s_amount;
        if (getIntent().getStringExtra("mob")!=null){
            Log.e("mob",getIntent().getStringExtra("mob"));
            Log.e("code",getIntent().getStringExtra("code"));
            String phoneNumber =  getIntent().getStringExtra("mob");
            u_phone=phoneNumber;
            String strLastFourDi = phoneNumber.length() >= 10 ? phoneNumber.substring(phoneNumber.length() - 10): "0000000000";
            tv_mob.setText("+"+getIntent().getStringExtra("code")+strLastFourDi);
            tv_email.setText(getIntent().getStringExtra("email"));
            tv_amount.setText("Rs."+s_amount);
            tv_name.setText(u_name);
//            Picasso.with(this)
//                    .load(company_logo).error(R.drawable.app_logo)
//                    .into(img_logo);
        }else{
            if(ConnectivityReceiver.isConnected()){
//                getUserDetail(s_transaction_id);
            }else {
                module.noInternet ();
            }
        }

        check_Gpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    paymentMode= "GOOGLE_PAY";
                    findCheckMode("GOOGLE_PAY");
                }

            }
        });
        check_PhnPe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    paymentMode= "PHONE_PE";
                    findCheckMode("PHONE_PE");
                }

            }
        });
        check_paytm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    paymentMode= "PAYTM";
                    findCheckMode("PAYTM");
                }

            }
        });
        check_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    paymentMode= "OTHER";
                    findCheckMode("OTHER");
                }

            }
        });

//        getTransectionID("","","","");
//        amount="10";

    }
    private void createWebView() {
        /**
         * You need to pass the webview to Razorpay
         */
        razorpay.setWebView(payment_webview);

        /**
         * Override the RazorpayWebViewClient for your custom hooks
         */
//        razorpay.setWebviewClient(new RazorpayWebViewClient (razorpay) {
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                Log.d("mn", "Custom client onPageStarted");
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                Log.d("jjj", "Custom client onPageFinished");
//            }
//        });
    }

    private void sendRequest() {
        razorpay.validateFields(payload, new ValidationListener () {
            @Override
            public void onValidationSuccess() {
                try {
                    payment_webview.setVisibility(View.VISIBLE);
                    lin_main.setVisibility(View.GONE);
                    btn_pay.setVisibility (View.GONE);
                    razorpay.submit (payload, PaymentActivity.this);
                } catch (Exception e) {
                    Log.e("com.example", "Exception: ", e);
                }
            }


            public void onValidationError(Map<String, String> error) {
                Log.d("com.example", "Validation failed: " + error.get("field") + " " + error.get("description"));
                // Toast.makeText(PaymentActivity.this, "Validation: " + error.get("field") + " " + error.get("description"), Toast.LENGTH_SHORT).show();
            }
        });
        razorpay.submit (payload, new PaymentResultListener ( ) {
            @Override
            public void onPaymentSuccess(String s) {
                Log.d ("successpay_msg", "onPaymentError: "+s);
                payment_webview.setVisibility(View.GONE);
                module.paysuccessToast ("Added Successfully");
                Intent i = new Intent(PaymentActivity.this,MainActivity.class);
                startActivity(i);
                finish ();
            }

            @Override
            public void onPaymentError(int i, String s) {
                Log.d ("errorpay", "onPaymentError: "+s);
                payment_webview.setVisibility(View.GONE);
                module.payerrorToast ("Something went wrong");
                Intent in = new Intent(PaymentActivity.this,MainActivity.class);
                startActivity(in);
                finish ();
            }
        });
    }



    @Override
    public void onPaymentSuccess(String s) {
        module.paysuccessToast ("Success");
        payment_webview.setVisibility(View.GONE);
        Intent i = new Intent(PaymentActivity.this,MainActivity.class);
        startActivity(i);
        finish ();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d ("error_msg", "onPaymentError: "+s);
        payment_webview.setVisibility(View.GONE);
        module.payerrorToast ("Error");
        Intent in = new Intent(PaymentActivity.this,MainActivity.class);
        startActivity(in);
        finish ();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        razorpay.onActivityResult (requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close:
                showExistDialog();
//                finishAffinity();
                break;
            case R.id.img_edit:
                Intent intent = new Intent(PaymentActivity.this,EditUserDetailsActivity.class);
                intent.putExtra("mob",u_phone);
                intent.putExtra("email",u_email);
                intent.putExtra("amount",amount);
                intent.putExtra("name",u_name);
                startActivity(intent);
                break;
            case R.id.lin_Gpay:
            case R.id.lin_bg_Gpay:
            case R.id.iv_gpay:
                managePaymentMethod(lin_bg_Gpay,check_Gpay,"GOOGLE_PAY");
                break;
            case R.id.lin_PhnPe:
            case R.id. lin_bg_phnpe:
                managePaymentMethod(lin_bg_phnpe,check_PhnPe,"PHONE_PE");
                break;
            case R.id.lin_paytm:
            case R.id.lin_bg_paytm:
                managePaymentMethod(lin_bg_paytm,check_paytm,"PAYTM");
                break;
            case R.id.lin_other:
            case R.id.lin_bg_other:
                managePaymentMethod(lin_bg_other,check_other,"OTHER");
                break;
            case R.id.btn_pay:
                if (!check_Gpay.isChecked()&&!check_PhnPe.isChecked()&&!check_paytm.isChecked()&&!check_other.isChecked()){
                    Toast.makeText(PaymentActivity.this, "Please select atleast one payment mode", Toast.LENGTH_SHORT).show();
                }
                else {

                    //GenrateOderId
                    String timeStamp = new SimpleDateFormat ("yyyyMMdd_HHmmss").format (Calendar.getInstance ( ).getTime ( ));
                    String order_id = "order_" + sessionMangement.getUserDetails ( ).get (KEY_ID)+ "_" +s_amount + "_" + timeStamp+ "_" + module.getRandomKey(3);
                    getOrderId (order_id_value);

                    Log.d ("odrdkj", "onClick: "+order_id_value);


                }
                //  }
//                payViaUpi("","","","","","");
                break;
        }
    }

    private void getOrderId(String order_id)
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",sessionMangement.getUserDetails ().get (KEY_ID));
        params.put("order_id",order_id);
        params.put("amount",s_amount);
        params.put("upi_name",paymentMode);

        Log.e("post_datata", "saveInfoIntoDatabase: "+params.toString() );
        module.postRequest(URL_RAZORPAY_PAYMENT, params, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                loadingBar.dismiss ();
                Log.e("add_fund",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean resp=obj.getBoolean("responce");
                    if(resp)
                    {

                        JSONObject orderRequest = new JSONObject ( );
                        try {
                            orderRequest.put ("currency", "INR");
                            orderRequest.put ("receipt", "order_rcptid_11");
                            orderRequest.put ("amount", 100); // amount in the smallest currency unit
                        } catch (JSONException e) {
                            e.printStackTrace ( );
                        }

                        Log.e ("Payment_Mode", paymentMode + "+transaction_id+" + s_transaction_id + "+phone+" + u_phone + "+email+" + u_email + "+amount+" + amount + "+name+" + u_name);

                        String amout = String.valueOf ((Integer.parseInt (String.valueOf (s_amount))) * 100);
                        String mobiles = sessionMangement.getUserDetails ( ).get (KEY_MOBILE);
                        Log.d ("fgrsgrg", "onClick: " + amout + razorpay_email + "--" + mobiles);

                        try {
                            payload = new JSONObject ("{currency: 'INR'}");
                            payload.put ("amount", amout);
                            payload.put ("contact", mobiles);
                            payload.put ("email", razorpay_email);
                            payload.put ("display_logo", false);
                        } catch (Exception e) {
                            e.printStackTrace ( );
                        }

                        try {
                            JSONArray jArray = new JSONArray ( );
                            jArray.put ("com.google.android.apps.nbu.paisa.user");
                            payload.put ("display_logo", false);
                            payload.put ("description", desc);
                            payload.put ("key_id", razor_pay);
                            payload.put ("method", "upi");
                            //   payload.put("vpa", upi);
                            payload.put ("_[flow]", "intent");
                            if (paymentMode.equals ("GOOGLE_PAY")) {
                                payload.put ("upi_app_package_name", "com.google.android.apps.nbu.paisa.user");
                            } else if (paymentMode.equals ("PHONE_PE")) {
                                payload.put ("upi_app_package_name", "com.phonepe.app");
                            } else if (paymentMode.equals ("PAYTM")) {
                                payload.put ("upi_app_package_name", "net.one97.paytm");
                            }
                            sendRequest();

                        } catch (Exception e) {
                            e.printStackTrace ( );
                        }
                    }
                    else
                    {
                        module.errorToast(""+obj.getString("message"));
                        loadingBar.dismiss();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }



    public  void managePaymentMethod(LinearLayout lin,CheckBox checkBox,String mode){
        btn_pay.setText("PAY");
        if (checkBox.isChecked()){
            checkBox.setChecked(false);
            lin.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.outline_rounded_15dp) );

        }else {
            paymentMode = mode;
            checkBox.setChecked(true);
            lin.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.green_outline_rounded_15dp) );

        }
        findCheckMode(mode);

    }
    public void findCheckMode(String mode){
        Log.e("dcfvgbh",mode);
        if (mode.equals("GOOGLE_PAY")){
            unchecked(check_PhnPe,check_paytm,check_other,lin_bg_phnpe,lin_bg_paytm,lin_bg_other);
        }else if (mode.equals("PHONE_PE")){
            unchecked(check_Gpay,check_paytm,check_other,lin_bg_Gpay,lin_bg_paytm,lin_bg_other);
        }else if (mode.equals("PAYTM")){
            unchecked(check_PhnPe,check_Gpay,check_other,lin_bg_phnpe,lin_bg_Gpay,lin_bg_other);
        }else if (mode.equals("OTHER")){
            unchecked(check_PhnPe,check_Gpay,check_paytm,lin_bg_phnpe,lin_bg_paytm,lin_bg_Gpay);
        }

        managePayButton();

    }
    public void unchecked(CheckBox cb1,CheckBox cb2,CheckBox cb3,LinearLayout lin1,LinearLayout lin2,LinearLayout lin3){
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        lin1.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.outline_rounded_15dp) );
        lin2.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.outline_rounded_15dp) );
        lin3.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.outline_rounded_15dp) );

    }
    public void managePayButton(){
        if (!check_Gpay.isChecked()&&!check_PhnPe.isChecked()&&!check_paytm.isChecked()&&!check_other.isChecked()){
//            btn_pay.setVisibility(View.GONE);
            btn_pay.setEnabled(false);
            btn_pay.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.color.light_gray) );

            paymentMode="";
        }else {
            btn_pay.setEnabled(true);
            btn_pay.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.color.razor_blue) );

        }
    }

    public void showExistDialog(){
        final Dialog dialog=new Dialog(PaymentActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_exit);
        TextView txtMessage=(TextView)dialog.findViewById(R.id.tv_msg);
        RelativeLayout btnOk=(RelativeLayout)dialog.findViewById(R.id.rel_ok);
        RelativeLayout btnNo=(RelativeLayout)dialog.findViewById(R.id.rel_close);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        txtMessage.setText("Do you really want to exit? ");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExistDialog();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}