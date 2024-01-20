package in.games.gdmatkalive.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import in.games.gdmatkalive.Activity.LoginActivity;
import in.games.gdmatkalive.Config.Constants;

public class SessionMangement {

    SharedPreferences prefs;

    SharedPreferences.Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    public SessionMangement(Context context)
    {
        this.context = context;
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void createLoginSession(String id, String name, String username
            , String mobile, String email,String dob, String address, String city, String pincode, String accountno,
                                   String bank_name, String ifsc, String holder, String paytm, String tez, String phonepay,
                                   String wallet) {

        editor.putBoolean(Constants.IS_LOGIN, true);
        editor.putString(Constants.KEY_ID, id);
        editor.putString(Constants.KEY_NAME, name);
        editor.putString(Constants.KEY_USER_NAME, username);
        editor.putString(Constants.KEY_MOBILE, mobile);
        editor.putString(Constants.KEY_EMAIL, email);
        editor.putString(Constants.KEY_DOB, dob);
        editor.putString(Constants.KEY_ADDRESS, address);
        editor.putString(Constants.KEY_CITY, city);
        editor.putString(Constants.KEY_PINCODE, pincode);
        editor.putString(Constants.KEY_ACCOUNNO, accountno);
        editor.putString(Constants.KEY_BANK_NAME, bank_name);
        editor.putString(Constants.KEY_IFSC, ifsc);
        editor.putString(Constants.KEY_HOLDER, holder);
        editor.putString(Constants.KEY_PAYTM, paytm);
        editor.putString(Constants.KEY_TEZ, tez);
        editor.putString(Constants.KEY_PHONEPAY, phonepay);
        editor.putString(Constants.KEY_WALLET, wallet);
//        editor.putString(Constants.KEY_MPIN,"");
        editor.putBoolean(Constants.KEY_DIALOG, false);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(Constants.KEY_ID, prefs.getString(Constants.KEY_ID, ""));
        user.put(Constants.KEY_NAME, prefs.getString(Constants.KEY_NAME, ""));
        user.put(Constants.KEY_USER_NAME, prefs.getString(Constants.KEY_USER_NAME, ""));
        user.put(Constants.KEY_MOBILE, prefs.getString(Constants.KEY_MOBILE, ""));
        user.put(Constants.KEY_EMAIL, prefs.getString(Constants.KEY_EMAIL, ""));
        user.put(Constants.KEY_DOB, prefs.getString(Constants.KEY_DOB, ""));
        user.put(Constants.KEY_ADDRESS, prefs.getString(Constants.KEY_ADDRESS, ""));
        user.put(Constants.KEY_CITY, prefs.getString(Constants.KEY_CITY, ""));
        user.put(Constants.KEY_PINCODE, prefs.getString(Constants.KEY_PINCODE, ""));
        user.put(Constants.KEY_ACCOUNNO, prefs.getString(Constants.KEY_ACCOUNNO, ""));
        user.put(Constants.KEY_BANK_NAME, prefs.getString(Constants.KEY_BANK_NAME, ""));
        user.put(Constants.KEY_IFSC, prefs.getString(Constants.KEY_IFSC, ""));
        user.put(Constants.KEY_HOLDER, prefs.getString(Constants.KEY_HOLDER, ""));
        user.put(Constants.KEY_PAYTM, prefs.getString(Constants.KEY_PAYTM, ""));
        user.put(Constants.KEY_TEZ, prefs.getString(Constants.KEY_TEZ, ""));
        user.put(Constants.KEY_PHONEPAY, prefs.getString(Constants.KEY_PHONEPAY, ""));
        user.put(Constants.KEY_WALLET, prefs.getString(Constants.KEY_WALLET, ""));
//        user.put(Constants.KEY_MPIN, prefs.getString(Constants.KEY_MPIN, ""));

        return user;
    }


    public void updateAddressSections(String mobile, String email, String dob)
    {
        editor.putString(Constants.KEY_MOBILE, mobile);
        editor.putString(Constants.KEY_EMAIL, email);
        editor.putString(Constants.KEY_DOB, dob);
        editor.apply();
    }
    public void updateAccSection(String acc_no, String bank_name, String ifsc, String holder)
    {
        editor.putString(Constants.KEY_ACCOUNNO, acc_no);
        editor.putString(Constants.KEY_BANK_NAME, bank_name);
        editor.putString(Constants.KEY_IFSC, ifsc);
        editor.putString(Constants.KEY_HOLDER, holder);
        editor.apply();
    }
    public void updateAddressSection(String address, String city, String pincode)
    {
        editor.putString(Constants.KEY_ADDRESS, address);
        editor.putString(Constants.KEY_CITY, city);
        editor.putString(Constants.KEY_PINCODE, pincode);
        editor.apply();
    }
    public void updatePaymentSection(String tez, String paytm, String phonepay)
    {
        editor.putString(Constants.KEY_TEZ, tez);
        editor.putString(Constants.KEY_PAYTM, paytm);
        editor.putString(Constants.KEY_PHONEPAY, phonepay);
        editor.apply();
    }
    public void updateEmailSection(String email, String dob)
    {
        editor.putString(Constants.KEY_EMAIL, email);
        editor.putString(Constants.KEY_DOB, dob);
        editor.apply();
    }
    public void updateWallet(String wallet)
    {
        editor.putString(Constants.KEY_WALLET, wallet);
        editor.apply();
    }
    public void addSessionItem(String key,String value){
        editor.putString(key, value);
        editor.apply();
    }
    public String getSessionItem(String key){
        return prefs.getString(key,"");
    }
    public void updateDilogStatus(boolean flag)
    {
        editor.putBoolean(Constants.KEY_DIALOG, flag);
        editor.apply();
    }

    public void logoutSession(Activity activity) {
        editor.clear();
        editor.commit();
        Intent logout = new Intent(context, LoginActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logout);
       activity.finish();
    }
    public void logoutSessionInsert() {
        editor.clear();
        editor.commit();
        Intent logout = new Intent(context, LoginActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logout);
        ((Activity)context).finish ();

    }
//     public void updateMpin(String mpin)
//     {
//         editor.putString(Constants.KEY_MPIN, mpin);
//         editor.apply();
//     }
    public boolean isLoggedIn() {
        return prefs.getBoolean(Constants.IS_LOGIN, false);
    }
    public boolean isDialogStatus() {
        return prefs.getBoolean(Constants.KEY_DIALOG, false);
    }

}
