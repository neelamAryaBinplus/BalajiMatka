package in.games.gdmatkalive.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_PLAY;


public class HowtoPlayFragment extends Fragment implements View.OnClickListener {
    SwipeRefreshLayout swipe;
    TextView tv_description,tv_link;
    LinearLayout lin_link;
    Module module;
    LoadingBar loadingBar;
    SessionMangement sessionMangement;

    public HowtoPlayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_howto_play, container, false);
        ((MainActivity)getActivity()).setTitles("How To Play");
        initView(view);
        return view;
    }

    private void initView(View view) {

        module = new Module(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        loadingBar = new LoadingBar(getActivity());
        swipe = view.findViewById(R.id.swipe);
        tv_description = view.findViewById(R.id.tv_description);
        tv_link = view.findViewById(R.id.tv_link);
        lin_link = view.findViewById(R.id.lin_link);
        lin_link.setOnClickListener(this);


        if (ConnectivityReceiver.isConnected())
        {
            howToPlayData();
        }else {
            module.noInternet();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    if (ConnectivityReceiver.isConnected())
                    {
                        howToPlayData();
                    }else {
                        module.noInternet();
                    }
                    swipe.setRefreshing(false);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.lin_link:
                String h= tv_link.getText().toString().trim();
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(h));
                startActivity(intent);
                break;
        }
    }
    private void howToPlayData() {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<String,String>();

        module.postRequest(URL_PLAY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject object = array.getJSONObject(0);
                    String data=object.getString("data");
                    String link=object.getString("link");
                    tv_description.setText(data);
                    tv_link.setText(String.valueOf(link));
                    loadingBar.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }
}