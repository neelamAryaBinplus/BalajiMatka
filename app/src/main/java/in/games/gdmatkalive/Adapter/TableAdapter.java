package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import in.games.gdmatkalive.Model.TableModel;
import in.games.gdmatkalive.R;

public class TableAdapter extends BaseAdapter {
    List<TableModel> list;
    Context context;
    String number;

    public TableAdapter(List<TableModel> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size ( );
    }

    @Override
    public Object getItem(int i) {
        return list.get (i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {


        View itemView = LayoutInflater.from (context).inflate (R.layout.row_add_data, null);
        TextView tv_no = (TextView) itemView.findViewById (R.id.tv_no);
        TextView tv_game = (TextView) itemView.findViewById (R.id.tv_game);
        TextView tv_digit = (TextView) itemView.findViewById (R.id.tv_digit);
        TextView tv_point = (TextView) itemView.findViewById (R.id.tv_point);
        TextView tv_type = (TextView) itemView.findViewById (R.id.tv_type);
        Button btn_delete = (Button) itemView.findViewById (R.id.btn_delete);


        final TableModel tableModel = list.get (i);
        {
           // tv_no.setText (num);
            tv_no.setText (String.valueOf (i+1));
            tv_game.setText (tableModel.getGame ( ));
            tv_digit.setText (tableModel.getDigits ( ));
            tv_point.setText (tableModel.getPoints ( ));
            tv_type.setText (tableModel.getType ( ));
            btn_delete.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
                    try {
                        list.remove (i);
                        notifyDataSetChanged ( );


                        int we = list.size ( );
                        int points = Integer.parseInt (tableModel.getPoints ( ));
                        int tot_pnt = we * points;
                        //   final TableModel tableModel = list.get (i);

                        // btn_submit.setText("(BIDS="+we+")(Points="+tot_pnt+")");
                    }catch (Exception e)
                    {
                        e.printStackTrace ();
                    }
                }
            });
            return itemView;


        }
    }
}
