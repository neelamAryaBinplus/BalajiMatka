package in.games.gdmatkalive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;

import static in.games.gdmatkalive.Config.BaseUrls.URL_TERMS;


public class TermsFragment extends Fragment {
    SwipeRefreshLayout swipe;
    TextView tv_title,tv_terms;
    LoadingBar loadingBar;
    Module module;
    String title,description;

    public TermsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms, container, false);
        ((MainActivity)getActivity()).setTitles("Terms and Conditions");
        initView(view);
        termsData();
        return view;
    }



    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
        loadingBar=new LoadingBar (getActivity ());
        module=new Module (getActivity ());
        tv_title = view.findViewById(R.id.tv_title);
        tv_terms = view.findViewById(R.id.tv_terms);

    }
    private void termsData() {

        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
         module.postRequest (URL_TERMS, params, new Response.Listener<String> ( ) {
    @Override
    public void onResponse(String response) {
        loadingBar.dismiss ();
        Log.e ("terms", "onResponse: "+response);
        try {
            JSONObject jsonObject = new JSONObject (response);
            Boolean result = Boolean.valueOf (jsonObject.getString ("responce"));
            if (result) {
                JSONObject arr=jsonObject.getJSONObject ("data");
                description = arr.getString ("pg_descri");
                title = arr.getString ("pg_title");
                Log.e ("dataterms", "onResponse: "+description);
                tv_terms.setText (description);
                tv_title.setText (title);
            }
            else{
                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_LONG).show();
            }
            loadingBar.dismiss ();
        } catch (JSONException e) {
            loadingBar.dismiss();
            module.showToast(e.getMessage());
            e.printStackTrace ( );
        }

    }
}, new Response.ErrorListener ( ) {
    @Override
    public void onErrorResponse(VolleyError error) {
        module.showVolleyError(error);
        loadingBar.dismiss();
    }
});
    }
}