package in.games.gdmatkalive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Interfaces.GetAppSettingData;
import in.games.gdmatkalive.Model.IndexResponse;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

//import static in.games.gdmatkalive.Activity.SplashActivity.home_text;
//import static in.games.gdmatkalive.Activity.SplashActivity.indexMinAmount;
//import static in.games.gdmatkalive.Activity.SplashActivity.index_min_wallet_msg;
//import static in.games.gdmatkalive.Activity.SplashActivity.index_notice;
//import static in.games.gdmatkalive.Activity.SplashActivity.index_w_amount;
//import static in.games.gdmatkalive.Activity.SplashActivity.min_add_amount;
//import static in.games.gdmatkalive.Activity.SplashActivity.whatsapp_msg;
//import static in.games.gdmatkalive.Activity.SplashActivity.whatsapp_no;
//import static in.games.gdmatkalive.Activity.SplashActivity.withdraw_no;
//import static in.games.gdmatkalive.Activity.SplashActivity.withdraw_text;


public class NoticeboardFragment extends Fragment {
LoadingBar loadingBar;
Module module;
LinearLayout lin_whatsapp;
TextView tv_withdraw,tv_whatsapp,tv_sample,tv_minDeposit,tv_minwithdraw,tv_notice;
SessionMangement sessionMangement;
String title="",description="";
SwipeRefreshLayout swipe;
String indexMinAmount,index_min_wallet_msg,index_notice,index_w_amount,min_add_amount,
        whatsapp_msg="Hi,Admin!",whatsapp_no,withdraw_no,withdraw_text;

    public NoticeboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_noticeboard, container, false);
        ((MainActivity)getActivity()).setTitles("NoticeBoard");
        initview(view);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    tv_withdraw.setText (withdraw_text);
                    tv_whatsapp.setText (withdraw_no);
                    tv_sample.setText(index_min_wallet_msg);
                    tv_minDeposit.setText(min_add_amount);
                    tv_minwithdraw.setText(index_w_amount);
                    tv_notice.setText(index_notice);
                    swipe.setRefreshing(false);
                }
            }
        });

//        noticeBoard();
        return view;
    }

    private void initview(View view) {

        sessionMangement = new SessionMangement(getActivity());
        loadingBar = new LoadingBar(getActivity());
        module = new Module(getActivity());
        swipe = view.findViewById(R.id.swipe);
        tv_withdraw=view.findViewById (R.id.tv_withdraw);
        tv_whatsapp =view.findViewById (R.id.tv_whatsapp);
        lin_whatsapp=view.findViewById (R.id.lin_whatsapp);
        tv_sample = view.findViewById(R.id.tv_sample);
        tv_minDeposit = view.findViewById(R.id.tv_minDeposit);
        tv_minwithdraw = view.findViewById(R.id.tv_minwithdraw);
        tv_notice = view.findViewById(R.id.tv_notice);

        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                indexMinAmount =  model.getMin_amount();
                index_min_wallet_msg = model.getMin_wallet_msg();
                index_notice =model.getNotice();
                index_w_amount= model.getW_amount();
                min_add_amount= model.getMin_wallet();
                whatsapp_no = model.getMobile();
                withdraw_no =model.getWithdraw_no();
                withdraw_text = model.getWithdraw_text();

                tv_withdraw.setText (withdraw_text);
                tv_whatsapp.setText (withdraw_no);
                tv_sample.setText(index_min_wallet_msg);
                tv_minDeposit.setText(indexMinAmount);
                tv_minwithdraw.setText(index_w_amount);
                tv_notice.setText(index_notice);

            }
        });
        lin_whatsapp.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                module.whatsapp(whatsapp_no,whatsapp_msg);
            }
        });

    }
//
//    public void noticeBoard() {
//        loadingBar.show();
//        HashMap<String, String> params = new HashMap<>();
////        params.put ("title",title);
////        params.put ("detail",detail);
//        module.postRequest(URL_NOTICEBOARD, params, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("noticeboard", response.toString());
//                try {
//                    JSONObject object = new JSONObject(response);
//                    boolean status = object.getBoolean("responce");
//                    if (status) {
//                        JSONArray data_arr = object.getJSONArray("data");
//                        for (int i = 0; i <= data_arr.length() - 1; i++) {
//                            JSONObject jsonObject = data_arr.getJSONObject(0);
//                            title = jsonObject.getString("title");
//                            description = jsonObject.getString("description");
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                   module.showToast("Something went wrong");
//                }
//                loadingBar.dismiss();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                loadingBar.dismiss();
//            }
//        });
//    }
}