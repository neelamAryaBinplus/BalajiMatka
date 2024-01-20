package in.games.gdmatkalive.Config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.games.gdmatkalive.Activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import in.games.gdmatkalive.Adapter.TableAdapter;
import in.games.gdmatkalive.Interfaces.GetAppSettingData;
import in.games.gdmatkalive.Model.IndexResponse;
import in.games.gdmatkalive.Model.MatkaModel;
import in.games.gdmatkalive.Model.TableModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.AppController;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_GETSTATUS;
import static in.games.gdmatkalive.Config.BaseUrls.URL_INDEX;
import static in.games.gdmatkalive.Config.BaseUrls.URL_INSERT_DATA;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.Constants.KEY_WALLET;

public class Module {
    public static String home_text = "", dialog_image = "", withdrw_text = "", tagline = "", min_add_amount = "", link = "", app_link = "", share_link = "", msg_status = "", withdrw_no = "";
    public static int ver_code = 0;
    Context context;
    SessionMangement session_management;
    LoadingBar loadingBar;
    WifiManager wifiManager;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    ToastMsg toastMsg;
    public Module(Context context) {
        this.context = context;
        session_management = new SessionMangement (context);
        loadingBar = new LoadingBar (context);
        toastMsg=new ToastMsg(context);
    }


    public void noInternet() {
        Dialog dialog;
        dialog = new Dialog (context);
        if (!ConnectivityReceiver.isConnected ( )) {


            dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
            dialog.setContentView (R.layout.nointernet_layout);
            dialog.show ( );
            Button btn_wifi = (Button) dialog.findViewById (R.id.btn_wifi);
            Button btn_data = (Button) dialog.findViewById (R.id.btn_data);
            dialog.setCanceledOnTouchOutside (true);
            btn_wifi.setOnClickListener (new View.OnClickListener ( ) {

                @Override
                public void onClick(View v) {
                    // Toast.makeText (context, "wifi", Toast.LENGTH_SHORT).show ( );

                    wifiManager = (WifiManager) context.getSystemService (Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled (true);
                    dialog.dismiss ( );
                }
            });
            btn_data.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    //Toast.makeText (context, "data", Toast.LENGTH_SHORT).show ( );
                    Intent intent = new Intent (android.provider.Settings.ACTION_SETTINGS);
                    context.startActivity (intent);

                }
            });
            //dialog.show ();
        } else {
            dialog.dismiss ( );
        }

    }

    public void getConfigData(final GetAppSettingData settingData) {
        HashMap<String, String> params = new HashMap<> ( );
        postRequest (URL_INDEX, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String resp) {
                Log.e ("Common", "onResponse: " + resp.toString ( ));
                try {
                    JSONObject jsonObject = new JSONObject (resp);
                    Boolean result = Boolean.valueOf (jsonObject.getString ("responce"));
                    if (result) {
                        JSONArray arr = jsonObject.getJSONArray ("data");
//                        JSONObject dataObj=arr.getJSONObject(0);
                        List<IndexResponse> list = new ArrayList<> ( );
                        list.clear ( );
                        Gson gson = new Gson ( );
                        Type listType = new TypeToken<List<IndexResponse>> ( ) {
                        }.getType ( );
                        list = gson.fromJson (arr.toString ( ), listType);
                        settingData.getAppSettingData (list.get (0));
                        Log.e ("getConfigData", list.get (0).getMobile ( ));

                    }


                } catch (Exception ex) {
                    ex.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  showVolleyError(error);
            }
        });
    }

    public void selectDateDialog() {
        Dialog dialog;
        dialog = new Dialog (context);
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.betdate_layout);
        dialog.show ( );

        dialog.setCanceledOnTouchOutside (true);
    }

    public void exitApp(String msg) {
        Dialog dialog = new Dialog (context);
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable (0));
        dialog.setContentView (R.layout.dialog_exit);
        dialog.show ( );

        TextView tv_msg;
        RelativeLayout rel_ok, rel_close;

        tv_msg = dialog.findViewById (R.id.tv_msg);
        rel_ok = dialog.findViewById (R.id.rel_ok);
        rel_close = dialog.findViewById (R.id.rel_close);

        tv_msg.setText (msg);

        rel_ok.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

            }
        });
        rel_close.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ( );
            }
        });


        dialog.setCanceledOnTouchOutside (false);
    }


    public void marketClosed(String msg) {
        Dialog dialog;
        dialog = new Dialog (context);
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable (0));
        dialog.setContentView (R.layout.dialog_bid_close);
        dialog.show ( );

        TextView tv_msg;
        Button btn_ok;

        tv_msg = dialog.findViewById (R.id.tv_msg);
        btn_ok = dialog.findViewById (R.id.btn_ok);

        tv_msg.setText (msg);

        btn_ok.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ( );
            }
        });


        dialog.setCanceledOnTouchOutside (true);
    }

    public void fieldRequired(String msg) {
        Dialog dialog = new Dialog (context);
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable (0));
        dialog.setContentView (R.layout.dialog_required);

        dialog.show ( );
        TextView tv_msg;
        Button btn_ok;

        tv_msg = dialog.findViewById (R.id.tv_msg);
        btn_ok = dialog.findViewById (R.id.btn_ok);

        tv_msg.setText (msg);

        btn_ok.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ( );
            }
        });

        dialog.setCanceledOnTouchOutside (false);

    }


    public void betTypeDialog(TextView type) {

        Dialog dialog;
        dialog = new Dialog (context);

        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView (R.layout.bettype_layout);
        TextView tv_open, tv_close;
        dialog.show ( );

        tv_open = (TextView) dialog.findViewById (R.id.tv_open);
        tv_close = (TextView) dialog.findViewById (R.id.tv_close);
        tv_open.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                type.setText ("OPEN");
                dialog.dismiss ( );
            }
        });
        tv_close.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                type.setText ("CLOSE");
                dialog.dismiss ( );
            }
        });

        dialog.setCanceledOnTouchOutside (true);

    }


    public void addData(String no, String game, String digit, String point, String type, List<TableModel> list, TableAdapter tableAdaper, ListView list_table, Button btnSave) {

        list.add (new TableModel (no, game, digit, point, type));
        tableAdaper = new TableAdapter (list, context);
        list_table.setAdapter (tableAdaper);
        tableAdaper.notifyDataSetChanged ( );
        int we = list.size ( );
        int points = Integer.parseInt (point);
        int tot_pnt = Integer.parseInt (getSumOfPoints (list));

        // btnSave.setText("(BIDS=" + we + ")(Points=" + tot_pnt + ")");
    }

    public String getSumOfPoints(List<TableModel> list) {
        int sum = 0;
        for (int i = 0; i < list.size ( ); i++) {
            sum = sum + Integer.parseInt (list.get (i).getPoints ( ));
        }

        return String.valueOf (sum);
    }

    public void showToast(String str) {
        Toast.makeText (context, str, Toast.LENGTH_SHORT).show ( );
    }

    public void setBidsDialog(int wallet_amount, final List<TableModel> list, final String m_id, final String c, final String game_id, final String w, final String dashName, final Button btn_submit, final String start_time, final String end_time) {
        HashMap<String, String> params = new HashMap<> ( );
        params.put("user_id",session_management.getUserDetails().get(KEY_ID));
        postRequest (URL_GETSTATUS, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e ("logonstatus", "onResponse: " + response);
                    JSONObject jsonObject = new JSONObject (String.valueOf (response));
                    jsonObject.getString ("login_status");

                    if (jsonObject.getString ("login_status").equals ("1")) {
                        errorToast ("You are inactive user");
                        //session_management.logoutSessionInsert ();
                    } else {
                        insertData (list, m_id, c, game_id, w, dashName, btn_submit, start_time, end_time);
                    }
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast ("" + error);
            }
        });


    }

    public void insertData(List<TableModel> list, String m_id, String c, String game_id, String w, String dashName, Button btn_submit, final String start_time, final String end_time) {

        int er = list.size ( );
        if (er <= 0) {
            String message = "Please Add Some Bids";
            fieldRequired (message);
            return;
        } else {
            try {

                int amt = 0;
                ArrayList list_digits = new ArrayList ( );
                ArrayList list_type = new ArrayList ( );
                ArrayList list_points = new ArrayList ( );
                int rows = list.size ( );

                for (int i = 0; i < rows; i++) {


                    TableModel tableModel = list.get (i);

                    String asd = tableModel.getDigits ( ).toString ( );
                    String asd1 = tableModel.getPoints ( ).toString ( );
                    String asd2 = tableModel.getType ( ).toString ( );
                    int b = 0;
                    if (asd2.equalsIgnoreCase ("Close")) {
                        b = 1;
                    } else if (asd2.equalsIgnoreCase ("Open")) {
                        b = 0;
                    }

                    amt = amt + Integer.parseInt (asd1);

                    char quotes = '"';
                    list_digits.add (quotes + asd + quotes);
                    list_points.add (asd1);
                    list_type.add (b);

                }


                String id = session_management.getUserDetails ( ).get (KEY_ID);
                String matka_id = m_id.toString ( ).trim ( );
                JSONObject jsonObject = new JSONObject ( );
                jsonObject.put ("points", list_points);
                jsonObject.put ("digits", list_digits);
                jsonObject.put ("bettype", list_type);
                jsonObject.put ("user_id", id);
                jsonObject.put ("matka_id", matka_id);
                jsonObject.put ("game_date", c);
                jsonObject.put ("game_id", game_id);

                JSONArray jsonArray = new JSONArray ( );
                jsonArray.put (jsonObject);

                int wallet_amount = Integer.parseInt (w);
                if (wallet_amount < amt) {

                    String message = "Insufficient Amount";
                    fieldRequired (message);
                    return;

                } else {
                    btn_submit.setEnabled (false);
                    updateWalletAmount (jsonArray, dashName, m_id, start_time, end_time);

                }
            } catch (Exception ex) {
                ex.printStackTrace ( );

            }

        }

    }

    public void updateWalletAmount(final JSONArray jsonArray, final String matka_name, final String m_id, final String start_time, final String end_time) {
        loadingBar.show ( );
        final String data = String.valueOf (jsonArray);
        HashMap<String, String> params = new HashMap<String, String> ( );
        params.put ("data", data);
        Log.e ("json_arr", data);

        postRequest (URL_INSERT_DATA, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e ("insert_data", response.toString ( ));
                    JSONObject jsonObject = new JSONObject (response);
                    final String status = jsonObject.getString ("status");
                    loadingBar.dismiss ( );
                    if (status.equals ("success")) {

                        Toast.makeText (context, "Bid Added Successfully.", Toast.LENGTH_SHORT).show ( );
                        Intent intent = new Intent (context, MainActivity.class);
                        intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity (intent);
                    } else if (status.equals ("failed")) {
                        String sd = status.toString ( );
                        errorToast (sd.toString ( ));
                    } else if (status.equals ("timeout")) {
                        marketClosed ("Biding closed for this date");

                    }

                } catch (Exception ex) {
                    loadingBar.dismiss ( );
                    ex.printStackTrace ( );
                    errorToast ("Err" + ex.getMessage ( ));
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                showVolleyError (error);
            }
        });

    }

    public void errorToast(String msg) {
        try {

            BottomSheetDialog bottomSheetDialog;
            bottomSheetDialog = new BottomSheetDialog (context, R.style.BottomSheetTheme);
            View sheetView = LayoutInflater.from (context).inflate (R.layout.error_toast, null);
            TextView tv_msg = sheetView.findViewById (R.id.tv_msg);
            tv_msg.setText (msg);
            bottomSheetDialog.setContentView (sheetView);
            bottomSheetDialog.setCanceledOnTouchOutside (true);
            bottomSheetDialog.show ( );

            Handler handler = new Handler ( );

            handler.postDelayed (new Runnable ( ) {
                @Override
                public void run() {
                    bottomSheetDialog.dismiss ( );
                }
            }, 2500);
            bottomSheetDialog.setCanceledOnTouchOutside (true);
        }catch (Exception e)
        {e.printStackTrace ();}
    }

    public void paysuccessToast(String msg) {
        toastMsg.toastIconSuccess(msg);
    }

    public void payerrorToast(String msg) {
        toastMsg.toastIconError(msg);
    }

    public void successToast(String msg) {

        BottomSheetDialog bottomSheetDialog;
        bottomSheetDialog = new BottomSheetDialog (context, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from (context).inflate (R.layout.sucess_toast, null);
        TextView tv_msg = sheetView.findViewById (R.id.tv_msg);
        tv_msg.setText (msg);
        bottomSheetDialog.setContentView (sheetView);
        bottomSheetDialog.setCanceledOnTouchOutside (true);
        bottomSheetDialog.show ( );

        Handler handler = new Handler ( );

        handler.postDelayed (new Runnable ( ) {
            @Override
            public void run() {
                bottomSheetDialog.dismiss ( );
            }
        }, 2500);
        bottomSheetDialog.setCanceledOnTouchOutside (true);
    }

    public String checkNull(String s) {
        String str = "";
        if (s == null || s.isEmpty ( ) || s.equalsIgnoreCase ("null")) {
            str = "";
        } else {
            str = s;
        }
        return str;
    }

    public String VolleyErrorMessage(VolleyError error) {
        String str_error = "";
        if (error instanceof TimeoutError) {
            str_error = "Connection Timeout";
        } else if (error instanceof AuthFailureError) {
            str_error = "Session Timeout";

        } else if (error instanceof ServerError) {
            str_error = "Server not responding please try again later";

        } else if (error instanceof NetworkError) {
            str_error = "Server not responding please try again later";

        } else if (error instanceof ParseError) {

            str_error = "An Unknown error occur";
        } else if (error instanceof NoConnectionError) {
            str_error = "No Internet Connection";
        }

        return str_error;
    }

    public void customToast(String msg) {

        BottomSheetDialog bottomSheetDialog;
        bottomSheetDialog = new BottomSheetDialog (context, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from (context).inflate (R.layout.toast, null);
        TextView tv_msg = sheetView.findViewById (R.id.tv_msg);
        tv_msg.setText (msg);
        bottomSheetDialog.setContentView (sheetView);
        bottomSheetDialog.setCanceledOnTouchOutside (true);
        bottomSheetDialog.show ( );

        Handler handler = new Handler ( );

        handler.postDelayed (new Runnable ( ) {
            @Override
            public void run() {
                bottomSheetDialog.dismiss ( );
            }
        }, 2500);
        bottomSheetDialog.setCanceledOnTouchOutside (true);
    }

    public void swingAnimations(LinearLayout ln) {
        Animation swing = AnimationUtils.loadAnimation (context, R.anim.swinging);
        ln.startAnimation (swing);

    }

    public void postRequest(String url, HashMap<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        Log.e ("postmethod", "postRequest: " + url + "\n" + params);

        StringRequest stringRequest = new StringRequest (Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Log.e ("params", "check" + params);
                return params;
                // return super.getParams ( );
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy (Constants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy (retryPolicy);

        AppController.getInstance ( ).addToRequestQueue (stringRequest, "tag");

    }


    public void setBetTypeDialog(Dialog dialogs, String gameDate, TextView txtOpen, TextView txtClose, TextView btnType, String stime, String eTime, String game_id) {
        Dialog dialog;
        dialog = new Dialog (context);

        int betType;
//            if(game_id.equals("3")){
//                betType = "close";
//            }else {
        betType = getBetType (getASandC (stime, eTime));
        // betType = getBetType (gameDate, stime, eTime);
        //}

//            if (betType.equalsIgnoreCase("both close")) {
//                 fieldRequired ("BID IS CLOSED FOR TODAY");
//            } else {
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.bettype_layout);
        dialog.show ( );
        TextView tv_open = (TextView) dialog.findViewById (R.id.tv_open);
        TextView tv_close = (TextView) dialog.findViewById (R.id.tv_close);
        Log.e ("betTypecheck", betType + "--");
        if (betType == 1) {
            tv_open.setVisibility (View.GONE);
            tv_close.setVisibility (View.VISIBLE);
        } else if (betType == 0) {
            tv_open.setVisibility (View.VISIBLE);
        }
//                if (betType.equalsIgnoreCase("open") || betType.equalsIgnoreCase("both open")) {
//                    if (tv_open.getVisibility() == View.GONE) {
//                        tv_open.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    if (tv_open.getVisibility() == View.VISIBLE) {
//                        tv_open.setVisibility(View.GONE);
//                    }
//                }
//
//                if (betType.equalsIgnoreCase("close") || betType.equalsIgnoreCase("both open")) {
//                    if (tv_close.getVisibility() == View.GONE) {
//                        tv_close.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    if (tv_close.getVisibility() == View.VISIBLE) {
//                        tv_close.setVisibility(View.GONE);
//                    }
//                }

        tv_open.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                btnType.setText (tv_open.getText ( ).toString ( ));
                dialog.dismiss ( );
            }
        });
        tv_close.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                btnType.setText (tv_close.getText ( ).toString ( ));
                dialog.dismiss ( );
            }
        });


        dialog.setCanceledOnTouchOutside (true);

    }


    public long[] getASandC(String startTime, String endTime) {
        long[] tArr = new long[2];
        Date date = new Date ( );
        SimpleDateFormat sim = new SimpleDateFormat ("HH:mm:ss");
        String time1 = startTime.toString ( );
        String time2 = endTime.toString ( );

        Date cdate = new Date ( );
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat ("HH:mm:ss");
        String time3 = format.format (cdate);
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        try {
            date1 = format.parse (time1);
            date2 = format.parse (time2);
            date3 = format.parse (time3);
        } catch (ParseException e1) {
            e1.printStackTrace ( );
        }
        long difference = date3.getTime ( ) - date1.getTime ( );
        long as = (difference / 1000) / 60;

        long diff_close = date3.getTime ( ) - date2.getTime ( );
        long c = (diff_close / 1000) / 60;
        tArr[0] = as;
        tArr[1] = c;
        return tArr;
    }

    public int getBetType(long[] tArr) {
        // as<0 => open,close
        //c>0 =>nothing but biding closed
        //else=>close
        long as = tArr[0];
        long c = tArr[1];
        if (as < 0) {
            return 2;
        } else if (c > 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public String getBetType(String gdate, String strt_time, String end_time) {
        String bet = "";
        Date date = new Date ( );
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("dd/MM/yyyy");
        String cDate = simpleDateFormat.format (date);
        String g_d = gdate.substring (0, 10);
        if (cDate.equals (g_d)) {
            long sDiff = getTimeDifference (strt_time);
            long eDiff = getTimeDifference (end_time);
            if (sDiff >= 0 && eDiff >= 0) {
                bet = "both open";
            } else if (sDiff < 0 && eDiff < 0) {
                bet = "both close";
            } else {
                if (sDiff >= 0) {
                    bet = "open";
                } else if (eDiff >= 0) {
                    bet = "close";
                } else {
                    bet = "both close";
                }

            }

        } else {
            bet = "both open";
        }
        Log.e ("find open/close", "" + bet);
        return bet;
    }

    public void setDateDialog(Dialog dialog, final String m_id, TextView txtCurrentDate, TextView txtNextDate, TextView txtAfterNextDate, TextView txtDate_id, final TextView btnGameType) {
        dialog = new Dialog (context);
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.betdate_layout);
        txtCurrentDate = (TextView) dialog.findViewById (R.id.tv_date1);
        txtNextDate = (TextView) dialog.findViewById (R.id.tv_date2);
        txtAfterNextDate = (TextView) dialog.findViewById (R.id.tv_date3);
        dialog.setCanceledOnTouchOutside (true);
        dialog.show ( );

        getDateData (m_id, txtCurrentDate, txtNextDate, txtAfterNextDate, loadingBar);


        final Dialog finalDialog = dialog;
        final TextView finalTxtCurrentDate = txtCurrentDate;
        txtCurrentDate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                String c = finalTxtCurrentDate.getText ( ).toString ( );

                ///   String as=getDataString(c);
                btnGameType.setText (c.toString ( ));
                btnGameType.setTextColor (context.getResources ( ).getColor (R.color.black));
                finalDialog.dismiss ( );
            }
        });

        final Dialog finalDialog1 = dialog;
        final TextView finalTxtNextDate = txtNextDate;
        txtNextDate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {


                String c = finalTxtNextDate.getText ( ).toString ( );

                // String as=getDataString(c);
                btnGameType.setText (c.toString ( ));
                btnGameType.setTextColor (context.getResources ( ).getColor (R.color.black));
                finalDialog1.dismiss ( );
            }
        });

        final Dialog finalDialog2 = dialog;
        final TextView finalTxtAfterNextDate = txtAfterNextDate;
        txtAfterNextDate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {


                String c = finalTxtAfterNextDate.getText ( ).toString ( );

                // String as=getDataString(c);
                btnGameType.setText (c.toString ( ));
                btnGameType.setTextColor (context.getResources ( ).getColor (R.color.black));
                finalDialog2.dismiss ( );
            }
        });

    }

    public void getDateData(final String m_id, final TextView txtCurrentDate, final TextView txtNextDate, final TextView txtAfterNextDate, final LoadingBar loadingBar) {
        loadingBar.show ( );
        String json_tag = "json_matka_id";
        HashMap<String, String> params = new HashMap<String, String> ( );
        params.put ("id", m_id);

        postRequest (BaseUrls.URL_MATKA_WITH_ID, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e ("datcheck", "onResponse: " + response);
                try {
                    if (loadingBar.isShowing ( )) {
                        loadingBar.dismiss ( );
                    }


                    JSONObject jsonObject = new JSONObject (response);
                    String resp = jsonObject.getString ("status");

                    if (resp.equals ("success")) {
                        String data = jsonObject.getString ("data");
                        JSONObject object = new JSONObject (data);
//                        JSONArray objects = jsonObject.getJSONArray("data");

                        //JSONArray dataObj=object.getJSONObject (0);
                        // Log.e ("ids", "datacheck "+dataObj );

                        MatkaModel matkasObjects = new MatkaModel ( );
                        matkasObjects.setId (object.getString ("id"));
                        matkasObjects.setName (object.getString ("name"));
                        matkasObjects.setStart_time (object.getString ("start_time"));
                        matkasObjects.setEnd_time (object.getString ("end_time"));
                        matkasObjects.setStarting_num (object.getString ("starting_num"));
                        matkasObjects.setNumber (object.getString ("number"));
                        matkasObjects.setEnd_num (object.getString ("end_num"));
                        matkasObjects.setBid_start_time (object.getString ("bid_start_time"));
                        matkasObjects.setBid_end_time (object.getString ("bid_end_time"));
                        matkasObjects.setSat_start_time (object.getString ("sat_start_time"));
                        matkasObjects.setSat_end_time (object.getString ("sat_end_time"));
                        matkasObjects.setCreated_at (object.getString ("created_at"));
                        matkasObjects.setUpdated_at (object.getString ("updated_at"));
                        matkasObjects.setStatus (object.getString ("status"));

                        String bid_start = "";
                        String bid_end = "";
                        String dt = new SimpleDateFormat ("EEEE").format (new Date ( ));

                        String st_time = "";
                        String st_time1 = "";
                        String st_time2 = "";

                        if (dt.equals ("Saturday")) {
                            st_time = matkasObjects.getSat_start_time ( );
                        } else if (dt.equals ("Sunday")) {
                            st_time = matkasObjects.getStart_time ( );
                        } else {
                            st_time = matkasObjects.getBid_start_time ( );
                        }

                        String dt1 = getNextDay (dt);
                        if (dt1.equals ("Saturday")) {
                            st_time1 = matkasObjects.getSat_start_time ( );
                        } else if (dt1.equals ("Sunday")) {
                            st_time1 = matkasObjects.getStart_time ( );
                        } else {
                            st_time1 = matkasObjects.getBid_start_time ( );
                        }

                        String dt2 = getNextDay (dt1);
                        if (dt2.equals ("Saturday")) {
                            st_time2 = matkasObjects.getSat_start_time ( );
                        } else if (dt2.equals ("Sunday")) {
                            st_time2 = matkasObjects.getStart_time ( );
                        } else {
                            st_time2 = matkasObjects.getBid_start_time ( );
                        }


                        String nd = "";
                        String and = "";
                        String cd = "";


                        if (st_time.equals ("") && st_time.equals ("null")) {

//                            txtCurrentDate.setText(dt + " Bet Close");
                            txtCurrentDate.setText ("\n" + dt);
                            cd = "c";

                            if (st_time1.equals ("") && st_time1.equals ("null")) {
//                                txtNextDate.setText(dt1 + " Bet Close");
                                txtNextDate.setText ("\n" + dt1);
                                nd = "c";
                            } else {
//                                txtNextDate.setText(dt1 + " Bet Open");
                                txtNextDate.setText ("\n" + dt1);
                                nd = "o";
                            }
                            if (st_time2.equals ("") && st_time2.equals ("null")) {
//                                txtAfterNextDate.setText(dt2 + " Bet Close");
                                txtAfterNextDate.setText ("\n" + dt2);
                                and = "c";
                            } else {
//                                txtAfterNextDate.setText(dt2 + " Bet Open");
                                txtAfterNextDate.setText ("\n" + dt2);
                                and = "o";
                            }


                            //  Toast.makeText(context,"Somtehin",Toast.LENGTH_LONG).show();
                        } else {
//                                Toast.makeText(context,""+matkasObjects.getSat_start_time(),Toast.LENGTH_LONG).show();

                            bid_start = st_time;
                            bid_end = matkasObjects.getBid_end_time ( ).toString ( );

                            String time1 = bid_start.toString ( );
                            String time2 = bid_end.toString ( );

                            Date cdate = new Date ( );


                            SimpleDateFormat format = new SimpleDateFormat ("HH:mm:ss");
                            String time3 = format.format (cdate);
                            Date date1 = null;
                            Date date2 = null;
                            Date date3 = null;
                            try {
                                date1 = format.parse (time1);
                                date2 = format.parse (time2);
                                date3 = format.parse (time3);
                            } catch (ParseException e1) {
                                e1.printStackTrace ( );
                            }

                            long difference = date3.getTime ( ) - date1.getTime ( );
                            long as = (difference / 1000) / 60;

                            long diff_close = date3.getTime ( ) - date2.getTime ( );
                            long c = (diff_close / 1000) / 60;

                            Date c_dat = new Date ( );
                            SimpleDateFormat dateFormat2 = new SimpleDateFormat ("dd/MM/yyyy EEEE");
                            String s_dt = dateFormat2.format (c_dat);
                            String n_dt = getNextDate (s_dt);
                            String a_dt = getNextDate (n_dt);
                            if (as < 0) {
//                                progressDialog.dismiss();
                                //btn.setText(s_dt+" Bet Open");
//                                txtCurrentDate.setText(s_dt + " Bet Open");
                                txtCurrentDate.setText (s_dt);

                                //Toast.makeText(OddEvenActivity.this,""+s_dt+"  Open",Toast.LENGTH_LONG).show();
                            } else if (c > 0) {
//                                progressDialog.dismiss();
//                                txtCurrentDate.setText(s_dt + " Bet Close");
                                txtCurrentDate.setText (s_dt);

                                // Toast.makeText(OddEvenActivity.this,""+s_dt+"  Close",Toast.LENGTH_LONG).show();
                            } else {
//                                progressDialog.dismiss();
                                //btn.setText(s_dt+" Bet Open");
//                                txtCurrentDate.setText(s_dt + " Bet Open");
                                txtCurrentDate.setText (s_dt);
                            }

                            if (nd.equals ("c")) {
                                txtNextDate.setText (n_dt);

                            } else {
//                                txtNextDate.setText(n_dt + " Bet Open");
                                txtNextDate.setText (n_dt);

                            }

                            if (and.equals ("c")) {
                                txtAfterNextDate.setText (a_dt);
//                                txtAfterNextDate.setText(a_dt + " Bet Close");

                            } else {
                                txtAfterNextDate.setText (a_dt);
//                                txtAfterNextDate.setText(a_dt + " Bet Open");

                            }

                        }


                    } else {
                        showToast ("wrong data");
                    }
                } catch (Exception ex) {
                    if (loadingBar.isShowing ( )) {
                        loadingBar.dismiss ( );
                    }
//                    progressDialog.dismiss();
                    Toast.makeText (context, "Something went wrong" + ex.getMessage ( ), Toast.LENGTH_LONG).show ( );

                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = VolleyErrorMessage (error);
                errorToast (msg);
            }
        });


    }

    public String getNextDay(String currentDate) {
        String nextDate = "";

        try {
            Calendar calendar = Calendar.getInstance ( );
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("EEEE");
            Date c = simpleDateFormat.parse (currentDate);
            calendar.setTime (c);
            calendar.add (Calendar.DAY_OF_WEEK, 1);
            nextDate = simpleDateFormat.format (calendar.getTime ( ));

        } catch (Exception ex) {
            ex.printStackTrace ( );
            //Toast.makeText(OddEvenActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        return nextDate.toString ( );
    }

    // Function for get Next Date
    public String getNextDate(String currentDate) {
        String nextDate = "";
        try {
            Calendar calendar = Calendar.getInstance ( );
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("dd/MM/yyyy EEEE");
            Date c = simpleDateFormat.parse (currentDate);
            calendar.setTime (c);
            calendar.add (Calendar.DAY_OF_WEEK, 1);
            nextDate = simpleDateFormat.format (calendar.getTime ( ));

        } catch (Exception ex) {
            ex.printStackTrace ( );
            //Toast.makeText(OddEvenActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        return nextDate.toString ( );
    }

    public void showVolleyError(VolleyError error) {
        String msg = VolleyErrorMessage (error);
        if (!msg.isEmpty ( )) {
            showToast ("" + msg);
        }
    }

    public void whatsapp(String phone, String message) {
//        PackageManager packageManager = context.getPackageManager();
        String url = null;
        try {
            url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode (message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace ( );
        }
        try {
            PackageManager pm = context.getPackageManager ( );
            pm.getPackageInfo ("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent (Intent.ACTION_VIEW);
            i.setData (Uri.parse (url));
            context.startActivity (i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText (context, "Whatsapp app not installed in your phone", Toast.LENGTH_LONG).show ( );
            e.printStackTrace ( );
        }
    }

    public String getRandomKey(int i) {
        final String characters = "0123456789";
        StringBuilder stringBuilder = new StringBuilder ( );
        while (i > 0) {
            Random ran = new Random ( );
            stringBuilder.append (characters.charAt (ran.nextInt (characters.length ( ))));
            i--;
        }
        return stringBuilder.toString ( );
    }

    public String get24To12FormatJackport(String timestr) {
        String tm = "";
        SimpleDateFormat _24HourSDF = new SimpleDateFormat ("HH:mm:ss");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat ("hh:mm a");

        try {
            Date _24Hourst = _24HourSDF.parse (timestr);
            tm = _12HourSDF.format (_24Hourst);

        } catch (ParseException e) {
            e.printStackTrace ( );
        }

        return tm;
    }

    public static String get24To12Format(String timestr) {
        String tm = "";
        SimpleDateFormat _24HourSDF = new SimpleDateFormat ("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat ("hh:mm a");

        try {
            Date _24Hourst = _24HourSDF.parse (timestr);
            tm = _12HourSDF.format (_24Hourst);

        } catch (ParseException e) {
            e.printStackTrace ( );
        }

        return tm;
    }

    public String formatTime(String time) {
        String tm = "";
        String t[] = time.split (" ");
        String time_type = t[1].toString ( );

        if (time_type.equals ("PM")) {
            tm = "p.m.";
        } else if (time_type.equals ("AM")) {
            tm = "a.m.";
        } else {
            tm = time_type;
        }

        String c_tm = t[0].toString ( ) + " " + tm;
        return c_tm;
    }

    public boolean getStatusTime(String current_tim) {
        boolean st = false;
        String t[] = current_tim.split (" ");
        String time_type = t[1].toString ( );
        if (time_type.equals ("a.m.") || time_type.equals ("p.m.")) {
            st = true;
        } else if (time_type.equals ("AM") || time_type.equals ("PM")) {
            st = false;
        }
        return st;
    }

    public String getCloseStatus(String gm_time, String current_time) {
        int h = 0;
        int m = 0;
        try {
            int days = 0, hours = 0, min = 0;

            Date date1 = new Date ( );
            Date date2 = new Date ( );

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("hh:mm aa");
            boolean st_time = getStatusTime (current_time);
            if (st_time) {
                date1 = simpleDateFormat.parse (formatTime (gm_time));
                date2 = simpleDateFormat.parse (current_time);

            } else {
                date1 = simpleDateFormat.parse (gm_time);
                date2 = simpleDateFormat.parse (current_time);

            }
            long difference = date2.getTime ( ) - date1.getTime ( );
            days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

            h = hours;
            m = min;
            Log.i ("======= Hours", " :: " + hours);
        } catch (Exception ex) {
            ex.printStackTrace ( );
            Toast.makeText (context, "Something went Wrong", Toast.LENGTH_SHORT).show ( );

        }
        String d = "" + h + ":" + m;
        return String.valueOf (d);
    }

    public long getTimeDifference(String time) {
        long diff_e_s = 0;
        Date date = new Date ( );
        SimpleDateFormat parseFormat = new SimpleDateFormat ("HH:mm:ss");
        String cur_time = parseFormat.format (date);
        try {
            final Date s_time = parseFormat.parse (cur_time.trim ( ));
            Date e_time = parseFormat.parse (time.trim ( ));
            diff_e_s = e_time.getTime ( ) - s_time.getTime ( );
            Log.e ("dddddd", "curr - " + s_time.toString ( ) + " -end - " + e_time.toString ( ));
        } catch (Exception ex) {
            ex.printStackTrace ( );
        }
        return diff_e_s;
    }

    public void loginStatus(Activity activity) {
        HashMap<String, String> params = new HashMap<> ( );
        params.put("user_id",session_management.getUserDetails().get(KEY_ID));
        postRequest (URL_GETSTATUS, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e ("logonstatus", "onResponse: " + response);
                    JSONObject jsonObject = new JSONObject (String.valueOf (response));
                    jsonObject.getString ("login_status");

                    if (jsonObject.getString ("login_status").equals ("1")) {
                        session_management.logoutSession ( activity);
                    } else {
                        session_management.isLoggedIn ( );
                    }
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast ("" + error);
            }
        });

    }

    public void getWalletAmount() {
        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );
        params.put ("user_id", session_management.getUserDetails ( ).get (KEY_ID).toString ( ));
        postRequest (BaseUrls.URL_GET_WALLET_AMOUNT, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e ("wallet", response.toString ( ));
                loadingBar.dismiss ( );
                try {
                    JSONObject object = new JSONObject (response);
                    if (object.getBoolean ("responce")) {
                        JSONArray arr = object.getJSONArray ("data");
                        JSONObject obj = arr.getJSONObject (0);
                        String wamt = obj.getString ("wallet_points");
                        session_management.updateWallet (wamt);
                        Log.e ("Common_wallet", "wallet_amt_-- " + session_management.getUserDetails ( ).get (KEY_WALLET));
                        ((MainActivity) context).setWallet_Amount (String.valueOf (wamt));
//                        ((MainActivity)context).setWallet_Amount(String.valueOf("1000"));
                    } else {
                        showToast ("Something went wrong");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace ( );
                }

            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                showVolleyError (error);
            }
        });

    }

    public void backToHome() {
        Intent i = new Intent (context, MainActivity.class);
        context.startActivity (i);
    }

    public int getTimeFormatStatus(String time) {
        //02:00 PM;
        String t[] = time.split (" ");
        String time_type = t[1].toString ( );
        String t1[] = t[0].split (":");
        int tm = Integer.parseInt (t1[0].toString ( ));

        if (time_type.equals ("AM")) {

        } else {
            if (tm == 12) {

            } else {
                tm = 12 + tm;
            }
        }
        return tm;

    }

    public String get24Hours(String time) {
        String t[] = time.split (" ");
        String time_type = t[1].toString ( );
        String t1[] = t[0].split (":");

        int tm = Integer.parseInt (t1[0].toString ( ));

        if (time_type.equalsIgnoreCase ("PM") || time_type.equalsIgnoreCase ("p.m")) {
            if (tm == 12) {

            } else {
                tm = 12 + tm;
            }
        }

//       String s ="";
//       String h = time.substring(0,1);
//       if (time.contains("PM")|| time.contains("p.m"))
//       {
//       int hours = Integer.parseInt(h);
//       if (hours<12)
//       {
//          hours =hours+12;
//       }
        String s = String.valueOf (tm) + ":" + t1[1] + ":00";

        return s;
    }

    public void getCurrentDate(TextView txt) {
        Date date = new Date ( );
        SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy EEEE");
        String cur_date = sdf.format (date);
        txt.setText (cur_date);
    }

    public void updatePoints(ArrayList<TableModel> list, int pos, String points, String betType) {
        TableModel tableModel = list.get (pos);
        tableModel.setPoints (points);
        tableModel.setType (betType);
    }
}