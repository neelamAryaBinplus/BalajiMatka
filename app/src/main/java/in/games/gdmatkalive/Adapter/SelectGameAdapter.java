package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.games.gdmatkalive.Model.SelectGameModel;
import in.games.gdmatkalive.R;

public class SelectGameAdapter extends RecyclerView.Adapter<SelectGameAdapter.ViewHolder> {
    Context context;
    ArrayList<SelectGameModel> list;

    public SelectGameAdapter(Context context, ArrayList<SelectGameModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_select_game,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.tv_gameName.setText(list.get(position).getName());
      SelectGameModel model = list.get(position);
        try {
            Picasso.with(context).load(model.getGame_logo()).into(holder.img_game2);
            Picasso.with(context).load(model.getGame_logo()).into(holder.img_game);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("bhnmgvbh", String.valueOf(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gameName;
        LinearLayout lin_game;
        ImageView img_game,img_game2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_gameName = itemView.findViewById(R.id.tv_gameName);
            img_game = itemView.findViewById(R.id.img_game);
            img_game2 = itemView.findViewById(R.id.img_game2);
            //lin_game = itemView.findViewById(R.id.lin_game);
        }
    }
}
