package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gdmatkalive.Model.FundHistoryModel;
import in.games.gdmatkalive.R;

public class FundHistoryAdapter extends RecyclerView.Adapter<FundHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<FundHistoryModel> list;

    public FundHistoryAdapter(Context context, ArrayList<FundHistoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_fund_history,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_req_no.setText(list.get(position).getRequest_id());
        holder.tv_point.setText(list.get(position).getRequest_points());
        holder.tv_status.setText(list.get(position).getRequest_status());
        String time = list.get(position).getTime();
        String[] dt = time.split(" ");
        String dat = dt[0];
        String tim = dt[1];
        Log.e("check_date",dat+"\n"+tim);

        holder.tv_date.setText(dat);
        holder.tv_type.setText(list.get(position).getType()+" - ");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_type,tv_point,tv_status,tv_date,tv_req_no;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type=itemView.findViewById (R.id.tv_type);
            tv_point=itemView.findViewById (R.id.tv_point);
            tv_status=itemView.findViewById (R.id.tv_status);
            tv_date=itemView.findViewById (R.id.tv_date);
            tv_req_no=itemView.findViewById (R.id.tv_req_no);

        }
    }
}
