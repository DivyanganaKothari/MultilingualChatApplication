package trainedge.myapplication.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import trainedge.myapplication.R;

/**
 * Created by DIVYANGANA KOTHARI on 01-09-2017.
 */

public class Search_Holder extends RecyclerView.ViewHolder {

    public final CardView cv1;
    public final TextView tv_name;
    public final Button btnAdd;
    public final ImageView iv;

    public Search_Holder(View itemView) {
        super(itemView);
        cv1 = itemView.findViewById(R.id.cv1);
        tv_name = itemView.findViewById(R.id.tv_name);
        btnAdd = itemView.findViewById(R.id.btnAdd);
        iv = itemView.findViewById(R.id.iv);
    }
}
