
//
//public class ToastMsg {
//}

package in.games.gdmatkalive.Config;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Toast;

import in.games.gdmatkalive.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 13,June,2020
 */
public class ToastMsg {
    Context context;
    LayoutInflater layoutInflater;

    public ToastMsg(Context context) {
        this.context = context;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void toastIconError(String s)
    {
        Toast toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        android.view.View view=layoutInflater.inflate(R.layout.error_toast,null);
        ((android.widget.TextView)view.findViewById(R.id.tv_msg)).setText(s);
        toast.setView(view);
        toast.show();
    }

    public void toastIconSuccess(String s) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        android.view.View custom_view = layoutInflater.inflate(R.layout.sucess_toast, null);
        ((android.widget.TextView) custom_view.findViewById(R.id.tv_msg)).setText(s);
        toast.setView(custom_view);
        toast.show();
    }
    public void toastIcon(String s)
    {
        Toast toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        android.view.View view=layoutInflater.inflate(R.layout.toast,null);
        ((android.widget.TextView)view.findViewById(R.id.tv_msg)).setText(s);
        toast.setView(view);
        toast.show();
    }
}