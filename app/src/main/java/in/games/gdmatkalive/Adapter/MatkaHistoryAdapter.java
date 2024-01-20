package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gdmatkalive.Model.MatkahistoryModel;
import in.games.gdmatkalive.R;


public class MatkaHistoryAdapter extends RecyclerView.Adapter<MatkaHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<MatkahistoryModel> list;

    public MatkaHistoryAdapter(Context context, ArrayList<MatkahistoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MatkaHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_matka_history,null);
        return new MatkaHistoryAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatkaHistoryAdapter.ViewHolder holder, int position) {
       holder.tv_name.setText(list.get(position).getName()+" HISTORY");
       // holder.tv_name.setText (common.get24To12FormatJackport(list.get (position).getStart_time ())+" HISTORY");
    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            tv_name=itemView.findViewById (R.id.tv_name);


        }
    }
}
