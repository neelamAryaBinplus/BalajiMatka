package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.AllHistoryModel;
import in.games.gdmatkalive.R;


public class AllHistoryAdapter extends RecyclerView.Adapter<AllHistoryAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<AllHistoryModel> list;
    ArrayList<AllHistoryModel> listFilter;
    Module module;

    public AllHistoryAdapter(Context context, ArrayList<AllHistoryModel> list) {
        this.context = context;
        this.list = list;
        this.listFilter = list;
        module = new Module(context);
    }
    @NonNull
    @Override
    public AllHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_history,null);
        return new AllHistoryAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllHistoryAdapter.ViewHolder holder, int position) {

        AllHistoryModel model = list.get(position);
        String bid_time = model.getTime();
        String d = bid_time.substring(0,10);
        String dd[] =d.split("-");
        holder.play_on.setText(dd[2]+"/"+dd[1]+"/"+dd[0]);
        String t = bid_time.substring(11,bid_time.length());
        SimpleDateFormat tformat = new SimpleDateFormat("hh:mm:ss");
        try {
            Date tt = tformat.parse(t);
            SimpleDateFormat f = new SimpleDateFormat("hh:mm a");
            String ttt = f.format(tt);
            holder.bid_time.setText(ttt);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(model.getBet_type().equals("")){
            holder.tv_name.setText (model.getName ());
        }else if(model.getGame_id().equals("13")) {
            holder.tv_name.setText(model.getName()+" ( Full Sangam ) ");
        }else if(model.getGame_id().equals("12")){
            holder.tv_name.setText(model.getName()+" ( Half Sangam ) ");
        }else{
            holder.tv_name.setText(model.getName()+" ( "+model.getBet_type()+" ) ");
        }

//holder.txt_matka_name.setText (model.getName ());
        // holder.txt_matka_name.setText(model.getName()+" ( "+model.getBet_type()+" ) ");
        holder.bid_digit.setText(model.getDigits());
        holder.bid_id.setText(model.getId());
        holder.bid_point.setText(model.getPoints());
        holder.play_for.setText(model.getDate());


        if (model.getStatus().equals("pending"))
        {
            holder.bid_status.setText("Result not announced");
            holder.bid_status.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icons8_neutral_16px,0);
        }
        else if(model.getStatus().equals("won")||model.getStatus().equals("win"))
        {
            holder.bid_status.setText("You won the bid");
            holder.bid_status.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icons8_happy_16px,0);
        }
        else if(model.getStatus().equals("loss"))
        {  holder.bid_status.setText("You Lost the bid");
            holder.bid_status.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icons8_sad_16px,0);
        }

    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list = listFilter;
                } else {
                    ArrayList<AllHistoryModel> filteredList = new ArrayList<>();
                    for (AllHistoryModel row : listFilter) {

                        if  (row.getDate().equals(charString)) {
                            filteredList.add(row);
                        }
                    }
                    if (filteredList.size()<=0)
                    {
                        module.errorToast("No History Found For Date : "+""+charString);
                    }
                    list = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<AllHistoryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,play_on,play_for,bid_id,bid_digit,bid_point,bid_time,bid_status;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            tv_name=itemView.findViewById (R.id.tv_name);
            play_on=itemView.findViewById (R.id.play_on);
            play_for=itemView.findViewById (R.id.play_for);
            bid_id=itemView.findViewById (R.id.bid_id);
            bid_digit=itemView.findViewById (R.id.bid_digit);
            bid_point=itemView.findViewById (R.id.bid_point);
            bid_time=itemView.findViewById (R.id.bid_time);
            bid_status=itemView.findViewById (R.id.bid_status);
        }
    }
}
