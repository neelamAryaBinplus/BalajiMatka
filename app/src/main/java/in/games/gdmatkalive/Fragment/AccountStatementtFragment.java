package in.games.gdmatkalive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

public class AccountStatementtFragment extends Fragment implements View.OnClickListener {
LinearLayout lin_withdrawFundHistory,lin_addFundHistory;
LoadingBar loadingBar;
Module module;
SessionMangement sessionMangement;
    public AccountStatementtFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_account_statementt, container, false);
        ((MainActivity)getActivity()).setTitles("Account Statement");

        loadingBar = new LoadingBar(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        module = new Module(getActivity());
        lin_withdrawFundHistory = root.findViewById(R.id.lin_withdrawFundHistory);
        lin_addFundHistory = root.findViewById(R.id.lin_addFundHistory);
        lin_addFundHistory.setOnClickListener(this);
        lin_withdrawFundHistory.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        Fragment fm = null;
        Bundle args = new Bundle();
        switch (v.getId())
        {
            case R.id.lin_withdrawFundHistory:
                fm = new WithdrawFundHistoryFragment();
                args.putString("name","w");
                break;
            case R.id.lin_addFundHistory:
                fm = new WithdrawFundHistoryFragment();
//                fm = new AddFundHistoryFragment();
                args.putString("name","a");
                break;
        }

        if (fm!=null)
        {
            fm.setArguments(args);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fm).addToBackStack(null).commit();

        }
    }




}