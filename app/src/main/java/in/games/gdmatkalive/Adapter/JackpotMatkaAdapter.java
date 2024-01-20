package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.MatkahistoryModel;
import in.games.gdmatkalive.R;

public class JackpotMatkaAdapter extends RecyclerView.Adapter<JackpotMatkaAdapter.ViewHolder> {
    Context context;
    ArrayList<MatkahistoryModel> list;
    Module common;

    public JackpotMatkaAdapter(Context context, ArrayList<MatkahistoryModel> list) {
        this.context = context;
        this.list = list;
        common = new Module(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_matka_history,null);
        return new JackpotMatkaAdapter.ViewHolder (view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name.setText (common.get24To12FormatJackport(list.get (position).getStart_time ())+" HISTORY");

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
