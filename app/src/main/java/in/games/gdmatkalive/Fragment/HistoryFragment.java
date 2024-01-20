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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements View.OnClickListener {
LinearLayout lin_win,lin_bid;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment ( );
        Bundle args = new Bundle ( );
        args.putString (ARG_PARAM1, param1);
        args.putString (ARG_PARAM2, param2);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (getArguments ( ) != null) {
            mParam1 = getArguments ( ).getString (ARG_PARAM1);
            mParam2 = getArguments ( ).getString (ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_history, container, false);
        initview(view);
        ((MainActivity)getActivity()).setTitles("History");
        return view;
    }

    private void initview(View view) {
        lin_bid=view.findViewById (R.id.lin_bid);
        lin_win=view.findViewById (R.id.lin_win);
        lin_bid.setOnClickListener (this);
        lin_win.setOnClickListener (this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId ()==R.id.lin_bid){
            Bundle bundle = new Bundle();
            Fragment fm =new MatkaNameHistoryFragment ();
            bundle.putString("type","matka");
            fm.setArguments(bundle);
            FragmentManager fragmentManager1=getFragmentManager ();
            fragmentManager1.beginTransaction ().add (R.id.frame,fm)
                    .commit ();
        }
        if(v.getId ()==R.id.lin_win){
            Bundle bundle = new Bundle();
            Fragment fm =new MatkaNameHistoryFragment ();
            bundle.putString("type","matka_win");
            fm.setArguments(bundle);
            FragmentManager fragmentManager1=getFragmentManager ();
            fragmentManager1.beginTransaction ().add (R.id.frame,fm)
                    .commit ();
        }
    }
}