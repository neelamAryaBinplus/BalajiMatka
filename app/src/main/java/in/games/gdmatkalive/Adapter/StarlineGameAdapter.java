package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.StarlineGameModel;
import in.games.gdmatkalive.R;

public class StarlineGameAdapter extends RecyclerView.Adapter<StarlineGameAdapter.ViewHolder> {
    Context context;
    ArrayList<StarlineGameModel> list;
    Module module;
    Animation animation;
    public StarlineGameAdapter(Context context, ArrayList<StarlineGameModel> list) {
        this.context = context;
        this.list = list;
        module = new Module(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_starline,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // holder.lin_num.setAnimation(animation);
       // module.swingAnimations(holder.lin_num);
        StarlineGameModel postion=list.get(position);
//        Date date=new Date();
//        SimpleDateFormat format1=new SimpleDateFormat("hh:mm aa");
//        String dr=format1.format(date);

//        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
//        String ddt=simpleDateFormat.format(date);
//        int c_tm= Integer.parseInt(ddt);
        holder.tv_time.setText(postion.getS_game_time());

//        String tm=getCloseStatus(postion.getS_game_end_time().toString(),dr);
//        String[] end_time=tm.split(":");
//        int h= Integer.parseInt(end_time[0].toString());
//        int m= Integer.parseInt(end_time[1].toString());
//        Log.e("starline", "getView: "+h+"  : "+m );



        Date date = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm aa");
        String dr = format1.format(date);
        String tm = module.getCloseStatus(list.get(position).getS_game_end_time(), dr);
        String[] end_times = tm.split(":");
        int h = Integer.parseInt(end_times[0].toString());
        int m = Integer.parseInt(end_times[1].toString());
        if (h <= 0 && m < 0) {
            holder.tv_bidStatus.setText("PLAY NOW");

            holder.tv_sNum.setText("***-**");
            holder.tv_bidStatus.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.tv_bidStatus.setText("CLOSED");
            holder.tv_bidStatus.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_sNum.setText(""+postion.getS_game_number());
//            holder.btn_image.setVisibility(View.INVISIBLE);
        }


//
//        if(h<=0 && m<0){
//            holder.tv_bidStatus.setText("BID Is Running For Today");
//            holder.tv_sNum.setText("***-**");
////            holder.tv_bidStatus.setTextColor(Color.parseColor("#053004"));
//            holder.tv_bidStatus.setTextColor(context.getResources().getColor(R.color.dark_green));
//        }
//        else{
//            holder.tv_bidStatus.setText("BID Is Closed For Today");
////            holder.tv_bidStatus.setTextColor(Color.parseColor("#b31109"));
//            holder.tv_bidStatus.setTextColor(context.getResources().getColor(R.color.red));
//            holder.tv_sNum.setText(""+postion.getS_game_number());
//            holder.btn_image.setVisibility(View.INVISIBLE);
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time,tv_bidStatus,tv_sNum,tv_eNum;
        RelativeLayout rel_play,rel_main;
        LinearLayout lin_num;
        ImageView btn_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_num = itemView.findViewById(R.id.lin_num);
            rel_play = itemView.findViewById(R.id.rel_play);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_bidStatus = itemView.findViewById(R.id.tv_bidStatus);
            tv_sNum = itemView.findViewById(R.id.tv_sNum);
            //tv_eNum = itemView.findViewById(R.id.tv_eNum);
//            btn_image=itemView.findViewById (R.id.btn_image);

//            animation = AnimationUtils.loadAnimation(context, R.anim.swinging);
//            lin_num.setAnimation(animation);
            rel_main=itemView.findViewById (R.id.rel_main);

        }
    }

    public int getTimeFormatStatus(String time)
    {
        //02:00 PM;
        String t[]=time.split(" ");
        String time_type=t[1].toString();
        String t1[]=t[0].split(":");
        int tm= Integer.parseInt(t1[0].toString());

        if(time_type.equals("AM"))
        {

        }
        else
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }
        return tm;

    }


    public String getCloseStatus(String gm_time, String current_time)
    {
        int h=0;
        int m=0;
        try {
            int days=0,hours=0,min=0;

            Date date1=new Date();
            Date date2=new Date();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
            boolean st_time=getStatusTime(current_time);
            if(st_time)
            {
                date1 = simpleDateFormat.parse(formatTime(gm_time));
                date2 = simpleDateFormat.parse(current_time);

            }
            else
            {
                date1 = simpleDateFormat.parse(gm_time);
                date2 = simpleDateFormat.parse(current_time);

            }

            long difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000*60*60*24));
            hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

//        hours = (hours < 0 ? -hours : hours);
//        min = (min < 0 ? -min : min);
            h=hours;
            m=min;
            Log.i("======= Hours"," :: "+hours);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
           // Toast.makeText (context, "Something went Wrong", Toast.LENGTH_SHORT).show ( );

//            Toast.makeText(context,"err :--  "+ex.getMessage()+"\n "+gm_time+"\n "+current_time, Toast.LENGTH_LONG).show();
        }
        String d=""+h+":"+m;
        return String.valueOf(d);
    }

    public boolean getStatusTime(String current_tim)
    {
        boolean st=false;
        String t[]=current_tim.split(" ");
        String time_type=t[1].toString();
        if(time_type.equals("a.m.") || time_type.equals("p.m."))
        {
            st=true;
        }
        else if(time_type.equals("AM") || time_type.equals("PM"))
        {
            st=false;
        }
        return st;
    }

    public String formatTime(String time)
    {
        String tm="";
        String t[]=time.split(" ");
        String time_type=t[1].toString();

        if(time_type.equals("PM"))
        {
            tm="p.m.";
        }
        else if(time_type.equals("AM"))
        {
            tm="a.m.";
        }
        else
        {
            tm=time_type;
        }

        String c_tm=t[0].toString()+" "+tm;
        return c_tm;
    }

    public String get12TimeFormatStatus(String time)
    {

        String type="";
        //02:00 PM;
        String t[]=time.split(" ");
        String time_type=t[1].toString();
        String t1[]=t[0].split(":");
        int tm= Integer.parseInt(t1[0].toString());

        if(time_type.equals("AM"))
        {

        }
        else
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }
        String d=tm+":"+t1[1].toString();
        return d;

    }

}
