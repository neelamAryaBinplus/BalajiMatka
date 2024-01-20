package in.games.gdmatkalive.Adapter;

import android.app.Activity;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Fragment.SelectGameFragment;
import in.games.gdmatkalive.Model.MatkaModel;
import in.games.gdmatkalive.R;

public class MatkaAdapter extends RecyclerView.Adapter<MatkaAdapter.ViewHolder> {
    private final String TAG=MatkaAdapter.class.getSimpleName();
    Activity context;
    ArrayList<MatkaModel> list;
    Module common;
    private int flag=0;
    Animation animation;
    Module module;

    public MatkaAdapter(Activity context, ArrayList<MatkaModel> list) {
        this.context = context;
        this.list = list;
        common = new Module(context);
        module = new Module(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_matka,null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       //
        // holder.lin_num.setAnimation(animation);
       // common.swingAnimations(holder.lin_num);
        final MatkaModel model=list.get(position);
        holder.tv_matkaName.setText(model.getName());
        String startTime="";
        String endTime="";
        String dy=new SimpleDateFormat("EEEE").format(new Date());
//        String dy="Sunday";
        if(dy.equalsIgnoreCase("Sunday"))
        {
            if(getValidTime(model.getStart_time().toString(),model.getEnd_time().toString())){
                startTime=model.getStart_time();
                endTime=model.getEnd_time();
            }else{
                startTime=model.getBid_start_time();
                endTime=model.getBid_end_time();
            }
        }
        else if(dy.equalsIgnoreCase("Saturday"))
        {
            if(getValidTime(model.getSat_start_time().toString(),model.getSat_end_time().toString())){
                startTime=model.getSat_start_time();
                endTime=model.getSat_end_time();
            }else{
                startTime=model.getBid_start_time();
                endTime=model.getBid_end_time();
            }
        }
        else{
            startTime=model.getBid_start_time();
            endTime=model.getBid_end_time();
        }
        Log.e("matka_time", "onBindViewHolder: "+model.getName()+"--"+startTime+"\n "+endTime );


        holder.tv_sNum.setText(getValidNumber(model.getStarting_num(),1)+"-"+getValidNumber(model.getNumber(),2)+"-"+getValidNumber(model.getEnd_num(),3));

        Date date=new Date();
        SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
        String time1 = startTime.toString();
        String time2 = endTime.toString();

        Date cdate=new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time3=format.format(cdate);
        Date date1 = null;
        Date date2=null;
        Date date3=null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
            date3=format.parse(time3);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long difference = date3.getTime() - date1.getTime();
        long as=(difference/1000)/60;

        long diff_close=date3.getTime()-date2.getTime();
        long c=(diff_close/1000)/60;
        if(dy.equalsIgnoreCase("Sunday")){
            if(getValidTime(model.getStart_time().toString(),model.getEnd_time().toString())){
                getPlayButton(as,c,holder.tv_bidStatus,holder.rel_play);
                holder.tv_openTime.setText(common.get24To12Format(model.getStart_time()));
                holder.tv_closeTime.setText(common.get24To12Format(model.getEnd_time()));
            }else
            {
                setInactiveStatus(holder.tv_bidStatus,holder.rel_play);
                holder.tv_openTime.setText(common.get24To12Format(model.getBid_start_time()));
                holder.tv_closeTime.setText(common.get24To12Format(model.getBid_end_time()));
//              holder.btnPlay.setVisibility(View.GONE);
            }
        }else if(dy.equalsIgnoreCase("Saturday")){
            if(getValidTime(model.getSat_start_time().toString(),model.getSat_end_time().toString())){
                getPlayButton(as,c,holder.tv_bidStatus,holder.rel_play);
                holder.tv_openTime.setText(common.get24To12Format(model.getSat_start_time()));
                holder.tv_closeTime.setText(common.get24To12Format(model.getSat_end_time()));
            }else
            {
                setInactiveStatus(holder.tv_bidStatus,holder.rel_play);
                holder.tv_openTime.setText(common.get24To12Format(model.getBid_start_time()));
                holder.tv_closeTime.setText(common.get24To12Format(model.getBid_end_time()));

//              holder.btnPlay.setVisibility(View.GONE);
            }
        }else{

            if(getValidTime(model.getBid_start_time().toString(),model.getBid_end_time().toString())){
                getPlayButton(as,c,holder.tv_bidStatus,holder.rel_play);
            }
            else
            {
                setInactiveStatus(holder.tv_bidStatus,holder.rel_play);

            }
            holder.tv_openTime.setText(common.get24To12Format(model.getBid_start_time()));
            holder.tv_closeTime.setText(common.get24To12Format(model.getBid_end_time()));

        }

        holder.rel_matka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGames(model);
            }
        });
        holder.rel_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGames(model);
            }
        });

//        holder.re


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_openTime,tv_closeTime,tv_matkaName,tv_sNum,tv_mNum,tv_eNum,tv_bidStatus;
        RelativeLayout rel_play;
        LinearLayout lin_num;
//        CardView card_matka;
        RelativeLayout rel_matka;
       // ImageView iv_pause;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_num = itemView.findViewById(R.id.lin_num);
            tv_openTime = itemView.findViewById(R.id.tv_openTime);
            tv_closeTime = itemView.findViewById(R.id.tv_closeTime);
            tv_matkaName = itemView.findViewById(R.id.tv_matkaName);
            tv_sNum = itemView.findViewById(R.id.tv_sNum);
            //tv_mNum = itemView.findViewById(R.id.tv_mNum);
            tv_bidStatus = itemView.findViewById(R.id.tv_bidStatus);
           // tv_eNum = itemView.findViewById(R.id.tv_eNum);
            rel_play = itemView.findViewById(R.id.rel_play);
//            card_matka = itemView.findViewById(R.id.card_matka);
            animation = AnimationUtils.loadAnimation(context, R.anim.swinging);
            //lin_num.setAnimation(animation);
            rel_matka = itemView.findViewById(R.id.rel_matka);
            //iv_pause=itemView.findViewById (R.id.iv_pause);

        }
    }
    public boolean getValidTime(String sTime, String eTime){

        if(sTime.equalsIgnoreCase("00:00:00") && eTime.equalsIgnoreCase("00:00:00")){
            return false;
        }else if(sTime.equalsIgnoreCase("00:00:00.000000") && eTime.equalsIgnoreCase("00:00:00.000000")){
            return false;
        }else{
            return true;
        }
    }

    public String getValidNumber(String str, int palace){
        String validStr="";
        if(str ==null || str.isEmpty() || str.equalsIgnoreCase("null")){
            if(palace==1){
                validStr="***";
            }else if(palace==2){
                validStr="**";
            }else{
                validStr="***";
            }
        }else{
            validStr=str;
        }
        return validStr;
    }
    public void getPlayButton(long as, long c, TextView tv_status, RelativeLayout btnPlay){
        if(as<0)
        {
            flag=2;
//       tv_status.setVisibility(View.VISIBLE);
            setActiveStatus(tv_status,btnPlay);
//        btnPlay.setVisibility(View.VISIBLE);

        }
        else if(c>0)
        {
            flag=3;
            setInactiveStatus(tv_status,btnPlay);
//        tv_status.setVisibility(View.VISIBLE);
//        btnPlay.setVisibility(View.GONE);
        }
        else
        {
            flag=1;
//        tv_status.setVisibility(View.VISIBLE);
            setActiveStatus(tv_status,btnPlay);
//        btnPlay.setVisibility(View.GONE);
        }
    }

    public void setActiveStatus(TextView tv, RelativeLayout btnPlay){
        tv.setVisibility(View.VISIBLE);
        tv.setText("PLAY NOW");
        btnPlay.setVisibility(View.VISIBLE);
        tv.setTextColor(context.getResources().getColor(R.color.white));
    }

    public void setInactiveStatus(TextView tv, RelativeLayout btnPlay){
//        if(tv.getVisibility()== View.GONE){
//            tv.setVisibility(View.VISIBLE);
//        }
        tv.setVisibility(View.VISIBLE);
        tv.setText("CLOSED");
        btnPlay.setVisibility(View.GONE);
        //iv_pause.setVisibility (View.VISIBLE);
        tv.setTextColor(context.getResources().getColor(R.color.white));

    }

    public void gotoGames(MatkaModel model){
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
            {err=1;
                stime=model.getStart_time().toString();
                etime=model.getEnd_time().toString();
                Log.e(TAG, "onClick: "+etime );

            }else{
                err=2;
                is_error=true;
            }


        }
        else if(dyClick.equalsIgnoreCase("Saturday"))
        {
            if(getValidTime(model.getSat_start_time(),model.getSat_end_time()))
            {
                err=3;
                stime=model.getSat_start_time().toString();
                etime=model.getSat_end_time().toString();
            }else{
                err=4;
                is_error=true;

            }
        }
        else
        {
            stime=model.getBid_start_time().toString();
            etime=model.getBid_end_time().toString();
        }

        long endDiff=common.getTimeDifference(etime);
//              common.showToast(""+err);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        if(endDiff<0 || is_error==true)
        {
            common.marketClosed ("Bid is closed for today");
        }
        else
        {
            try
            {

                // Toast.makeText(HomeActivity.this,""+Prevalent.Matka_count,Toast.LENGTH_LONG).show();

                // String st=txtStatus.getText().toString();
                String m_id=model.getId().toString().trim();
                Log.e("matlasid",m_id);
                String matka_name=model.getName().toString().trim();
                String status = model.getStatus();

                if (status.equals( "active" )) {


                    Log.e(TAG, "onClick: "+stime+" == "+etime );
                    Bundle bundle = new Bundle();
                    Fragment fm  = new SelectGameFragment ();

                    bundle.putString("matka_name",module.checkNull(model.getName()));
                    bundle.putString ("m_id", module.checkNull(m_id));
                    bundle.putString("s_num",module.checkNull(model.getStarting_num()));
                    bundle.putString("num",module.checkNull(model.getNumber()));
                    bundle.putString("e_num",module.checkNull(model.getEnd_num()));
                    bundle.putString("end_time",module.checkNull(model.getBid_end_time()));
                    bundle.putString("start_time",module.checkNull(model.getBid_start_time()));

                    bundle.putString("type","matka");
                    Log.e(TAG, "matkaAdapter: "+bundle.toString());


                    fm.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) context;

                    FragmentManager fragmentManager = activity.getSupportFragmentManager ();
                    fragmentManager.beginTransaction().replace(R.id.frame, fm)
                            .addToBackStack(null).commit();
                }
                else
                {
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
