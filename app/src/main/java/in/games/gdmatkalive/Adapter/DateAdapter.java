package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gdmatkalive.Model.DateModel;
import in.games.gdmatkalive.R;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    Context context;
    ArrayList<DateModel> list;

    public DateAdapter(Context context, ArrayList<DateModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_date,null);

        return new DateAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_type,tv_day;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            tv_date=itemView.findViewById (R.id.tv_date);
            tv_day=itemView.findViewById (R.id.tv_day);
            tv_type=itemView.findViewById (R.id.tv_type);
        }
    }
}
