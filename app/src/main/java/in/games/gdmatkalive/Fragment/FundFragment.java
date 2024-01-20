package in.games.gdmatkalive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.R;

public class FundFragment extends Fragment implements View.OnClickListener {
LinearLayout lin_addFund,lin_withdrawFund;
    public FundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fund, container, false);
        ((MainActivity)getActivity()).setTitles("Fund");
        initView(view);
        return view;
    }

    private void initView(View view) {
        lin_addFund = view.findViewById(R.id.lin_addFund);
        lin_withdrawFund = view.findViewById(R.id.lin_withdrawFund);
        lin_withdrawFund.setOnClickListener(this);
        lin_addFund.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;

        switch (v.getId())
        {
            case R.id.lin_addFund:
                fragment = new AddFundFragment();
                break;
            case R.id.lin_withdrawFund:
                fragment = new WithdrawalFundFragment();
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }
}