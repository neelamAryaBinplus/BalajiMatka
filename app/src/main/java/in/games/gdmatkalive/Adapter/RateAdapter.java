package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gdmatkalive.Model.RateModel;
import in.games.gdmatkalive.R;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ViewHolder> {
    Context context;
    ArrayList<RateModel> list;

    public RateAdapter(Context context, ArrayList<RateModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_rate,null);
        return new RateAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateAdapter.ViewHolder holder, int position) {

        holder.tv_gamename.setText(list.get(position).getName());
        holder.tv_gamerate.setText(list.get(position).getRate_range()+" : "+list.get(position).getRate());

    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_gamename,tv_gamerate;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            tv_gamename = itemView.findViewById(R.id.tv_gamename);
            tv_gamerate = itemView.findViewById(R.id.tv_gamerate);
        }
    }
}
