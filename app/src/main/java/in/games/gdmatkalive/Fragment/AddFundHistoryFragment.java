package in.games.gdmatkalive.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.FundHistoryAdapter;
import in.games.gdmatkalive.Model.FundHistoryModel;
import in.games.gdmatkalive.R;

public class AddFundHistoryFragment extends Fragment {
    RecyclerView rec_aHistory;
    SwipeRefreshLayout swipe;
    ArrayList<FundHistoryModel> fList;
    FundHistoryAdapter fundHistoryAdapter;

    public AddFundHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_fund_history, container, false);
        ((MainActivity)getActivity()).setTitles("Add Fund History");

        fList = new ArrayList<>();
        rec_aHistory = view.findViewById(R.id.rec_aHistory);
        swipe = view.findViewById(R.id.swipe);
        fundHistory();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    fundHistory();
                    swipe.setRefreshing(false);
                }
            }
        });

        return view;
    }
    private void fundHistory() {
        fList.add(new FundHistoryModel());
        fList.add(new FundHistoryModel());
        fList.add(new FundHistoryModel());
        fList.add(new FundHistoryModel());
        rec_aHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        fundHistoryAdapter = new FundHistoryAdapter(getContext(),fList);
        fundHistoryAdapter.notifyDataSetChanged();
        rec_aHistory.setAdapter(fundHistoryAdapter);
    }
}