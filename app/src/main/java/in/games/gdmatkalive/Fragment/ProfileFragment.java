package in.games.gdmatkalive.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_REGISTER;
import static in.games.gdmatkalive.Config.Constants.KEY_ACCOUNNO;
import static in.games.gdmatkalive.Config.Constants.KEY_ADDRESS;
import static in.games.gdmatkalive.Config.Constants.KEY_BANK_NAME;
import static in.games.gdmatkalive.Config.Constants.KEY_CITY;
import static in.games.gdmatkalive.Config.Constants.KEY_DOB;
import static in.games.gdmatkalive.Config.Constants.KEY_EMAIL;
import static in.games.gdmatkalive.Config.Constants.KEY_HOLDER;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.Constants.KEY_IFSC;
import static in.games.gdmatkalive.Config.Constants.KEY_MOBILE;
import static in.games.gdmatkalive.Config.Constants.KEY_NAME;
import static in.games.gdmatkalive.Config.Constants.KEY_PAYTM;
import static in.games.gdmatkalive.Config.Constants.KEY_PHONEPAY;
import static in.games.gdmatkalive.Config.Constants.KEY_PINCODE;
import static in.games.gdmatkalive.Config.Constants.KEY_TEZ;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    CardView card_address, card_bank, card_paytm, card_gpay, card_ppe, card_personal;
    TextView tv_user, tv_number;
    SessionMangement sessionMangement;
    LoadingBar loadingBar;
    Module module;
    String mobile,email,dob;
    String ifsc_code, paytm_number, account_number, phonePay_number, googlePay_number, address, city, pincode, bank_name, acc_holder_name;
    int   year,month,day;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_profile, container, false);
        initview (view);
        ((MainActivity) getActivity ( )).setTitles ("My Profile");
        return view;
    }


    @Override
    public void onStart() {
        super.onStart ( );
        // Toast.makeText(getActivity(),"Now onStart() calls", Toast.LENGTH_LONG).show(); //onStart Called
        mobile = sessionMangement.getUserDetails ( ).get (KEY_MOBILE);
        googlePay_number = sessionMangement.getUserDetails ( ).get (KEY_TEZ);
        phonePay_number = sessionMangement.getUserDetails ( ).get (KEY_PHONEPAY);
        account_number = sessionMangement.getUserDetails ( ).get (KEY_ACCOUNNO);
        paytm_number = sessionMangement.getUserDetails ( ).get (KEY_PAYTM);
        ifsc_code = sessionMangement.getUserDetails ( ).get (KEY_IFSC);

        address = sessionMangement.getUserDetails ( ).get (KEY_ADDRESS);
        city = sessionMangement.getUserDetails ( ).get (KEY_CITY);
        pincode = sessionMangement.getUserDetails ( ).get (KEY_PINCODE);
        bank_name = sessionMangement.getUserDetails ( ).get (KEY_BANK_NAME);
        acc_holder_name = sessionMangement.getUserDetails ( ).get (KEY_HOLDER);

        dob=sessionMangement.getUserDetails ().get (KEY_DOB);
        email=sessionMangement.getUserDetails ().get (KEY_EMAIL);
    }

    private void initview(View view) {
        sessionMangement = new SessionMangement (getActivity ( ));
        loadingBar = new LoadingBar (getActivity ( ));
        module = new Module (getActivity ( ));
        card_address = view.findViewById (R.id.card_address);
        card_bank = view.findViewById (R.id.card_bank);
        card_paytm = view.findViewById (R.id.card_paytm);
        card_gpay = view.findViewById (R.id.card_gpay);
        card_ppe = view.findViewById (R.id.card_ppe);
        card_personal = view.findViewById (R.id.card_personal);
        tv_user = view.findViewById (R.id.tv_user);
        tv_number = view.findViewById (R.id.tv_number);

        card_address.setOnClickListener (this);
        card_bank.setOnClickListener (this);
        card_paytm.setOnClickListener (this);
        card_gpay.setOnClickListener (this);
        card_ppe.setOnClickListener (this);
        card_personal.setOnClickListener (this);

        mobile = sessionMangement.getUserDetails ( ).get (KEY_MOBILE);
        googlePay_number = sessionMangement.getUserDetails ( ).get (KEY_TEZ);
        phonePay_number = sessionMangement.getUserDetails ( ).get (KEY_PHONEPAY);
        account_number = sessionMangement.getUserDetails ( ).get (KEY_ACCOUNNO);
        paytm_number = sessionMangement.getUserDetails ( ).get (KEY_PAYTM);
        ifsc_code = sessionMangement.getUserDetails ( ).get (KEY_IFSC);

        address = sessionMangement.getUserDetails ( ).get (KEY_ADDRESS);
        city = sessionMangement.getUserDetails ( ).get (KEY_CITY);
        pincode = sessionMangement.getUserDetails ( ).get (KEY_PINCODE);
        bank_name = sessionMangement.getUserDetails ( ).get (KEY_BANK_NAME);
        acc_holder_name = sessionMangement.getUserDetails ( ).get (KEY_HOLDER);
        dob=sessionMangement.getUserDetails ().get (KEY_DOB);
        email=sessionMangement.getUserDetails ().get (KEY_EMAIL);
        tv_user.setText (sessionMangement.getUserDetails ( ).get (KEY_NAME));
        tv_number.setText (mobile);

    }

    @Override
    public void onClick(View v) {
        Dialog dialog;
        if (v.getId ( ) == R.id.card_personal) {
            dialog = new Dialog (getContext ( ));
            dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
            dialog.setContentView (R.layout.dialog_personal_layout);
            dialog.show ( );
            Button btn_submit = (Button) dialog.findViewById (R.id.btn_submit);
            EditText et_email = (EditText) dialog.findViewById (R.id.et_email);
            TextView et_dob = (TextView) dialog.findViewById (R.id.et_dob);

            et_dob.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                     year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK ,new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            year  = year;
                            month = month;
                            day   = dayOfMonth;

                            // Show selected date
                            et_dob.setText(new StringBuilder().append(day).append("-")
                                    .append(month + 1) .append("-").append(year)
                                    .append(" "));

                        }
                    },year,month,day);
                    datePickerDialog.show();
                }
            });
            String mob = mobile;

            et_email.setText (email);
            et_dob.setText (dob);


            btn_submit.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    String email = et_email.getText ( ).toString ( );
                    String dob = et_dob.getText ( ).toString ( );

                    String mob = mobile;

                    if (et_email.getText ( ).toString ( ).isEmpty ( )) {
                        et_email.setError ("Required email id");
                        et_email.requestFocus ( );
                    } else if (et_dob.getText ( ).toString ( ).isEmpty ( )) {
                        et_dob.setError ("Required DOB");
                        et_dob.requestFocus ( );
                    } else {
                      storeProfileData (dob,mob,email);

                        dialog.dismiss ( );

                    }
                }


            });


            dialog.setCanceledOnTouchOutside (true);
        } else if (v.getId ( ) == R.id.card_address) {

            dialog = new Dialog (getContext ( ));
            dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
            dialog.setContentView (R.layout.dialog_address_layout);
            dialog.show ( );
            Button btn_address = (Button) dialog.findViewById (R.id.btn_address);
            EditText et_address = (EditText) dialog.findViewById (R.id.et_address);
            EditText et_city = (EditText) dialog.findViewById (R.id.et_city);
            EditText et_pin = (EditText) dialog.findViewById (R.id.et_pin);

            et_address.setText (address);
            et_city.setText (city);
            et_pin.setText (pincode);

            btn_address.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    String a = et_address.getText ( ).toString ( );
                    String c = et_city.getText ( ).toString ( );
                    String p = et_pin.getText ( ).toString ( );
                    String mob = mobile;

                    if (et_address.getText ( ).toString ( ).isEmpty ( )) {
                        et_address.setError ("Required address");
                        et_address.requestFocus ( );
                    } else if (et_city.getText ( ).toString ( ).isEmpty ( )) {
                        et_city.setError ("Required city");
                        et_city.requestFocus ( );
                    } else if (et_pin.getText ( ).toString ( ).isEmpty ( )) {
                        et_pin.setError ("Required pin");
                        et_pin.requestFocus ( );
                    } else {

                        storeAddressData (a, c, p, mob);
                        dialog.dismiss ( );

                    }
                }
            });


            dialog.setCanceledOnTouchOutside (true);

        } else if (v.getId ( ) == R.id.card_bank) {

            dialog = new Dialog (getContext ( ));

            dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
            dialog.setContentView (R.layout.dialog_bank_layout);
            dialog.show ( );
            Button btn_bank = (Button) dialog.findViewById (R.id.btn_bank);
            EditText et_accno = (EditText) dialog.findViewById (R.id.et_accno);
            EditText et_bankname = (EditText) dialog.findViewById (R.id.et_bankname);
            EditText et_ifsc = (EditText) dialog.findViewById (R.id.et_ifsc);
            EditText et_hname = (EditText) dialog.findViewById (R.id.et_hname);
            et_accno.setText (account_number);
            et_bankname.setText (bank_name);
            et_ifsc.setText (ifsc_code);
            et_hname.setText (acc_holder_name);
            btn_bank.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {

                    String mob = sessionMangement.getUserDetails ( ).get (KEY_ID);
                    String accno = et_accno.getText ( ).toString ( );
                    String bankname = et_bankname.getText ( ).toString ( );
                    String ifsc = et_ifsc.getText ( ).toString ( );
                    String hod_name = et_hname.getText ( ).toString ( );
                    if (et_accno.getText ( ).toString ( ).isEmpty ( )) {
                        et_accno.setError ("Required account number");
                        et_accno.requestFocus ( );
                    } else if (et_bankname.getText ( ).toString ( ).isEmpty ( )) {
                        et_bankname.setError ("Required bank name");
                        et_bankname.requestFocus ( );
                    } else if (et_ifsc.getText ( ).toString ( ).isEmpty ( )) {
                        et_ifsc.setError ("Required ifsc code");
                        et_ifsc.requestFocus ( );
                    } else if (et_hname.getText ( ).toString ( ).isEmpty ( )) {
                        et_hname.setError ("Required holder name");
                        et_hname.requestFocus ( );
                    } else {

                        storeBankDetails (accno, bankname, ifsc, hod_name, mob);
                        dialog.dismiss ( );
                    }
                }
            });

            dialog.setCanceledOnTouchOutside (true);
        } else if (v.getId ( ) == R.id.card_paytm) {

            dialog = new Dialog (getContext ( ));

            dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
            dialog.setContentView (R.layout.dialog_paytm_layout);
            dialog.show ( );
            Button btn_paytm = (Button) dialog.findViewById (R.id.btn_paytm);
            EditText et_paytm = (EditText) dialog.findViewById (R.id.et_paytm);

            et_paytm.setText (paytm_number);
            btn_paytm.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    String paytmno = et_paytm.getText ( ).toString ( );

                    if (et_paytm.getText ( ).toString ( ).isEmpty ( )) {
                        et_paytm.setError ("Required paytm number");
                        et_paytm.requestFocus ( );
                    } else if (paytmno.length ( ) != 10) {
                        et_paytm.setError ("Invalid Number");
                        et_paytm.requestFocus ( );
                    } else if (Integer.parseInt (String.valueOf (paytmno.charAt (0))) < 6) {
                        et_paytm.setError ("Invalid Number");
                        et_paytm.requestFocus ( );
                    } else {
                        storeAccDetails (googlePay_number, paytmno, phonePay_number, mobile);
                        dialog.dismiss ( );

                    }


                }
            });

            dialog.setCanceledOnTouchOutside (true);
        } else if (v.getId ( ) == R.id.card_gpay) {

            dialog = new Dialog (getContext ( ));

            dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
            dialog.setContentView (R.layout.dialog_googlepay_layout);
            dialog.show ( );
            Button btn_gpay = (Button) dialog.findViewById (R.id.btn_gpay);
            EditText et_gpay = (EditText) dialog.findViewById (R.id.et_gpay);

            et_gpay.setText (googlePay_number);
            btn_gpay.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    String gpay = et_gpay.getText ( ).toString ( );
                    if (et_gpay.getText ( ).toString ( ).isEmpty ( )) {
                        et_gpay.setError ("Required google pay number");
                        et_gpay.requestFocus ( );
                    } else if (gpay.length ( ) != 10) {
                        et_gpay.setError ("Invalid Number");
                        et_gpay.requestFocus ( );
                    } else if (Integer.parseInt (String.valueOf (gpay.charAt (0))) < 6) {
                        et_gpay.setError ("Invalid Number");
                        et_gpay.requestFocus ( );
                    } else {

                        storeAccDetails (gpay, paytm_number, phonePay_number, mobile);
                        dialog.dismiss ( );
                    }
                }
            });

            dialog.setCanceledOnTouchOutside (true);
        } else if (v.getId ( ) == R.id.card_ppe) {

            dialog = new Dialog (getContext ( ));

            dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
            dialog.setContentView (R.layout.dialog_phonepe_layout);
            dialog.show ( );
            Button btn_ppe = (Button) dialog.findViewById (R.id.btn_ppe);
            EditText et_ppe = (EditText) dialog.findViewById (R.id.et_ppe);

            et_ppe.setText (phonePay_number);
            btn_ppe.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    String phonpe = et_ppe.getText ( ).toString ( );
                    if (et_ppe.getText ( ).toString ( ).isEmpty ( )) {
                        et_ppe.setError ("Required phonepe number");
                        et_ppe.requestFocus ( );
                    } else if (phonpe.length ( ) != 10) {
                        et_ppe.setError ("Invalid Number");
                        et_ppe.requestFocus ( );
                    } else if (Integer.parseInt (String.valueOf (phonpe.charAt (0))) < 6) {
                        et_ppe.setError ("Invalid Number");
                        et_ppe.requestFocus ( );
                    } else {
                        storeAccDetails (googlePay_number, paytm_number, phonpe, mobile);
                        dialog.dismiss ( );
                    }
                }
            });
            dialog.setCanceledOnTouchOutside (true);
        }


    }



    private void storeBankDetails(final String accno, final String bankname, final String ifsc, final String hod_name, final String mailid) {

        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );
        params.put ("key", "3");
//        params.put("mobile",mailid);
        params.put ("mobile", mobile);
        params.put ("user_id", sessionMangement.getUserDetails ( ).get (KEY_ID));
        params.put ("accountno", accno);
        params.put ("bankname", bankname);
        params.put ("ifsc", ifsc);
        params.put ("accountholder", hod_name);

        module.postRequest (URL_REGISTER, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss ( );
                try {
                    JSONObject jsonObject = new JSONObject (response);
                    boolean resp = jsonObject.getBoolean ("responce");
                    if (resp) {
                        sessionMangement.updateAccSection (accno, bankname, ifsc, hod_name);
//                        common.showToast(""+response.getString("message"));
                        module.successToast ("" + jsonObject.getString ("message"));
                        onStart ( );
                    } else {
                        module.errorToast ("" + jsonObject.getString ("error"));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace ( );
                    module.showToast ("wrong");
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                module.showVolleyError (error);
            }
        });

    }

    private void storeAccDetails(final String teznumber, final String paytmno, final String phonepay, final String mailid) {

        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );

        params.put ("key", "4");
//        params.put("mobile",sessionMangement.getUserDetails().get(KEY_MOBILE));
        params.put ("user_id", sessionMangement.getUserDetails ( ).get (KEY_ID));
        params.put ("tez_no", teznumber);
        params.put ("paytm_no", paytmno);
        params.put ("phonepay_no", phonepay);

        module.postRequest (URL_REGISTER, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e ("profile", "onResponse: " + response);
                loadingBar.dismiss ( );
                try {
                    JSONObject jsonObject = new JSONObject (response);
                    boolean resp = jsonObject.getBoolean ("responce");
                    if (resp) {
                        sessionMangement.updatePaymentSection (teznumber, paytmno, phonepay);

                        module.successToast ("" + jsonObject.getString ("message"));
                        onStart ( );
                    } else {
                        //module.errorToast("Something Went Wrong");
                        module.errorToast ("" + jsonObject.getString ("message"));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace ( );
                    module.showToast ("wrong");
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                module.showVolleyError (error);
            }
        });
    }

    private void storeProfileData(final String dob, String mobile, final String email) {
        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );
        params.put ("key", "5");
        params.put ("mobile", mobile);
        params.put ("user_id", sessionMangement.getUserDetails ( ).get (KEY_ID));
        params.put ("email", email);
        params.put ("dob", dob);

        module.postRequest (URL_REGISTER, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss ( );
                try {
                    JSONObject jsonObject = new JSONObject (response);
                    boolean resp = jsonObject.getBoolean ("responce");
                    if (resp) {


                        sessionMangement.updateEmailSection (email, dob);
                        module.successToast ("" + jsonObject.getString ("message"));
                        onStart ( );

                    } else {
                        module.errorToast ("" + jsonObject.getString ("error"));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace ( );
                    module.showToast ("wrong");
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                module.showVolleyError (error);
            }
        });
    }

    private void storeAddressData(final String a, final String c, final String p, final String mob) {

        loadingBar.show ( );
        String json_tag = "add_address";
        HashMap<String, String> params = new HashMap<> ( );
        params.put ("key", "2");
        params.put ("address", a);
        params.put ("user_id", sessionMangement.getUserDetails ( ).get (KEY_ID));
        params.put ("city", c);
        params.put ("pin", p);
        params.put ("mobile", mob);

        module.postRequest (URL_REGISTER, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss ( );
                try {
                    JSONObject jsonObject = new JSONObject (response);
                    boolean resp = jsonObject.getBoolean ("responce");
                    if (resp) {

                        sessionMangement.updateAddressSection (a, c, p);
                        module.successToast ("" + jsonObject.getString ("message"));
                        onStart ( );

                    } else {
                        module.errorToast ("" + jsonObject.getString ("error"));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace ( );
                    module.showToast ("wrong");
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                module.showVolleyError (error);
            }
        });
    }


}