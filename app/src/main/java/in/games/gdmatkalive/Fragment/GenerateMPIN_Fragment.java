package in.games.gdmatkalive.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.CREATE_MPIN;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.Constants.KEY_MOBILE;


public class GenerateMPIN_Fragment extends Fragment implements View.OnClickListener {
LinearLayout  gen_mpin;
LoadingBar loadingBar;
Module module;
SessionMangement sessionMangement;

    public GenerateMPIN_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_generate_m_p_i_n_, container, false);
        initview(view);
        ((MainActivity)getActivity ()).setTitles ("Genearte MPIN");
        return view;
    }

    private void initview(View view) {
        sessionMangement = new SessionMangement(getActivity());
        loadingBar = new LoadingBar(getActivity());
        module = new Module(getActivity());
        gen_mpin=view.findViewById (R.id.gen_mpin);
        gen_mpin.setOnClickListener (this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId ()==R.id.gen_mpin){
            Dialog dialog ;
            dialog=new Dialog(getContext ());

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_mpin);
            dialog.show();


            Button btn_gen = (Button) dialog.findViewById (R.id.btn_gen);
            EditText et_mpin=(EditText)dialog.findViewById (R.id.et_mpin);
            btn_gen.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    String mpin = et_mpin.getText ().toString ();
                    if(et_mpin.getText ().toString ().isEmpty ()){
                        et_mpin.setError ("MPIN Required");
                        et_mpin.requestFocus ();
                    }else if (mpin.length()!=4)
                    {
                        et_mpin.setError ("4 Digit MPIN Required");
                        et_mpin.requestFocus ();
                    } else {
//                        Toast.makeText (getContext (), "Success", Toast.LENGTH_SHORT).show ( );
//                       dialog.dismiss ();
                        getMPINData(mpin,et_mpin);
                        dialog.dismiss();
                    }

                }
            });

            dialog.setCanceledOnTouchOutside (true);

        }

    }
    private void getMPINData(final String mpin, EditText et_mpin) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",mpin);
        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        params.put("mobile",sessionMangement.getUserDetails().get(KEY_MOBILE));

        module.postRequest(CREATE_MPIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create_mpin",response.toString());
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("success"))
                    {
                        module.successToast(object.getString("message"));
//                        sessionMangement.updateMpin(mpin);
                    }else {
                        module.errorToast("Something went wrong");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }
}