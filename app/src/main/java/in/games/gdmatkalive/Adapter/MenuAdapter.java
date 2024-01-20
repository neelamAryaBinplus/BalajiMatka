package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gdmatkalive.Model.MenuModel;
import in.games.gdmatkalive.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    Context context;
    ArrayList<MenuModel> list;

    public MenuAdapter(Context context, ArrayList<MenuModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_menu,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MenuModel menuModel = list.get(position);
        String title = menuModel.getTitle();
        if (title.equalsIgnoreCase("Home"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.home));
        }else  if (title.equalsIgnoreCase("My Profile"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.profile));
        }else  if (title.equalsIgnoreCase("Generate MPIN"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.lock));
        }else  if (title.equalsIgnoreCase("My History"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.history));
        }else  if (title.equalsIgnoreCase("Account Statement"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.statement));
        }else  if (title.equalsIgnoreCase("Fund"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.wallet));
        }else  if (title.equalsIgnoreCase("Notification"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.notification));
        }else  if (title.equalsIgnoreCase("How To Play"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.play));
        }else  if (title.equalsIgnoreCase("Game Rate"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.rate));
        }else  if (title.equalsIgnoreCase("Noticeboard/Rules"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.rule));
        }else  if (title.equalsIgnoreCase("Sign Out"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.signout));
        }

//        holder.img_icon.setImageDrawable(context.getResources().getDrawable(list.get(position).getIcon()));

        holder.tv_title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_icon;
        TextView tv_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
