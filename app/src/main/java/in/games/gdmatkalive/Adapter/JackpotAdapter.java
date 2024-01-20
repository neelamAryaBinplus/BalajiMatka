package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Fragment.SelectGameFragment;
import in.games.gdmatkalive.Model.JackpotModel;
import in.games.gdmatkalive.R;

public class JackpotAdapter extends RecyclerView.Adapter<JackpotAdapter.ViewHolder> {
    Context context;
    ArrayList<JackpotModel> list;
    Module module;
    private int flag = 0;
    Animation animation;
    Module common;

    public JackpotAdapter(Context context, ArrayList<JackpotModel> list) {
        this.context = context;
        this.list = list;
        module = new Module (context);
        common = new Module(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (context).inflate (R.layout.row_jackpot, null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.lin_num.setAnimation (animation);
//        module.swingAnimations (holder.lin_num);

        JackpotModel model = list.get (position);
        holder.tv_num.setText (model.getResult ());
        if(model.getResult ().equals (null)||model.getResult ().isEmpty ()){
            holder.tv_num.setText ("**");
        }
        else {
            holder.tv_num.setText (model.getResult ());
        }
        holder.tv_time.setText (common.get24To12FormatJackport(model.getStart_time ()));
        String startTime = "";
        String endTime = "";
        String dy = new SimpleDateFormat ("EEEE").format (new Date ( ));
//        String dy="Sunday";

        if (getValidTime (model.getStart_time ( ).toString ( ), model.getEnd_time ( ).toString ( ))) {
            startTime = model.getStart_time ( );
            endTime = model.getEnd_time ( );
        } else {
            startTime = model.getStart_time ( );
            endTime = model.getEnd_time ( );
        }


        Log.e ("matka_time", "onBindViewHolder: " + model.getName ( ) + "--" + startTime + "\n " + endTime);

        Date date = new Date ( );
        SimpleDateFormat sim = new SimpleDateFormat ("HH:mm:ss");
        String time1 = startTime.toString ( );
        String time2 = endTime.toString ( );

        Date cdate = new Date ( );
        SimpleDateFormat format = new SimpleDateFormat ("HH:mm:ss");
        String time3 = format.format (cdate);
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        try {
            date1 = format.parse (time1);
            date2 = format.parse (time2);
            date3 = format.parse (time3);
        } catch (ParseException e1) {
            e1.printStackTrace ( );
        }
        long difference = date3.getTime ( ) - date1.getTime ( );
        long as = (difference / 1000) / 60;

        long diff_close = date3.getTime ( ) - date2.getTime ( );
        long c = (diff_close / 1000) / 60;

        if (getValidTime (model.getStart_time ( ).toString ( ), model.getEnd_time ( ).toString ( ))) {
            getPlayButton (as, c, holder.tv_bidStatus, holder.rel_play);
        } else {
            setInactiveStatus (holder.tv_bidStatus, holder.rel_play);

        }
        holder.rel_matka.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                gotoGames (model);
            }
        });
        holder.rel_play.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                gotoGames (model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size ( );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time, tv_bidStatus, tv_num;
        RelativeLayout rel_play, rel_matka;
//        LinearLayout lin_num;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            rel_play = itemView.findViewById (R.id.rel_play);
            tv_time = itemView.findViewById (R.id.tv_time);
            tv_bidStatus = itemView.findViewById (R.id.tv_bidStatus);
            tv_num = itemView.findViewById (R.id.tv_num);
//            lin_num = itemView.findViewById (R.id.lin_num);
            animation = AnimationUtils.loadAnimation (context, R.anim.swinging);
//            lin_num.setAnimation (animation);
            rel_matka = itemView.findViewById (R.id.rel_matka);
        }
    }

    public boolean getValidTime(String sTime, String eTime) {

        if (sTime.equalsIgnoreCase ("00:00:00") && eTime.equalsIgnoreCase ("00:00:00")) {
            return false;
        } else if (sTime.equalsIgnoreCase ("00:00:00.000000") && eTime.equalsIgnoreCase ("00:00:00.000000")) {
            return false;
        } else {
            return true;
        }
    }

    public void getPlayButton(long as, long c, TextView tv_status, RelativeLayout btnPlay) {
        if (as < 0) {
            flag = 2;
//       tv_status.setVisibility(View.VISIBLE);
            setActiveStatus (tv_status, btnPlay);
//        btnPlay.setVisibility(View.VISIBLE);

        } else if (c > 0) {
            flag = 3;
            setInactiveStatus (tv_status, btnPlay);
//        tv_status.setVisibility(View.VISIBLE);
//        btnPlay.setVisibility(View.GONE);
        } else {
            flag = 1;
//        tv_status.setVisibility(View.VISIBLE);
            setActiveStatus (tv_status, btnPlay);
//        btnPlay.setVisibility(View.GONE);
        }
    }

    public void setActiveStatus(TextView tv, RelativeLayout btnPlay) {
        tv.setVisibility (View.VISIBLE);
        tv.setText ("PLAY NOW");
        btnPlay.setVisibility (View.VISIBLE);
        tv.setTextColor (context.getResources ( ).getColor (R.color.white));
    }

    public void setInactiveStatus(TextView tv, RelativeLayout btnPlay) {

        tv.setVisibility (View.VISIBLE);
        tv.setText ("CLOSED");
        btnPlay.setVisibility (View.GONE);
        tv.setTextColor (context.getResources ( ).getColor (R.color.white));

    }

    public void gotoGames(JackpotModel model){
        String dyClick=new SimpleDateFormat("EEEE").format(new Date());
//                String dyClick="Sunday";
        Log.e("asdaee",""+dyClick);
        String stime ="";
        String etime ="";
        int err=0;
        boolean is_error=false;
        if(dyClick.equalsIgnoreCase("Sunday"))
        {
            if(getValidTime(model.getStart_time(),model.getEnd_time()))
            {
                err=1;
                stime=model.getStart_time().toString();
                etime=model.getEnd_time().toString();
                Log.e("jackadp", "onClick: "+etime );

            }else{
                err=2;
                is_error=true;
            }
        } else {
            stime=model.getStart_time ().toString();
            etime=model.getEnd_time ().toString();
        }

        long endDiff=common.getTimeDifference(etime);
             common.showToast(""+endDiff);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
       if(endDiff<=0) {
            common.marketClosed ("Bid is closed for today");
        } else {
            try
            {
                String m_id=model.getId().toString().trim();
                Log.e("matlasid",m_id);
                String matka_name=model.getName().toString().trim();
                String status = model.getStatus();

                if (status.equals( "active" )) {

                    Log.e("jacktime", "onClick: "+stime+" == "+etime );
                    Bundle bundle = new Bundle();
                    Fragment fm  = new SelectGameFragment ();

                    bundle.putString("matka_name",model.getName ());
                    bundle.putString ("m_id",model.getId ());
                    bundle.putString("s_num","");
                    bundle.putString("num","");
                    bundle.putString("e_num","");
                    bundle.putString("end_time",model.getEnd_time ());
                    bundle.putString("start_time",model.getStart_time ());

                    bundle.putString("type","jackpot");

                    Log.e("jackkdatae", "matkaAdapter: "+bundle.toString());


                    fm.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) context;

                    FragmentManager fragmentManager = activity.getSupportFragmentManager ();
                    fragmentManager.beginTransaction().replace(R.id.frame, fm)
                            .addToBackStack(null).commit();
                } else {
                    common.marketClosed ("Bid closed");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(context,""+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}