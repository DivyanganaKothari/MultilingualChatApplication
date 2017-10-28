package trainedge.myapplication.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import trainedge.myapplication.R;

/**
 * Created by DIVYANGANA KOTHARI on 09-10-2017.
 */

public class Contact_Holder extends RecyclerView.ViewHolder {

    public final CardView cv1;
    public final TextView tv_name;
    public final ImageView iv;

    public Contact_Holder(View itemView) {
        super(itemView);
        cv1 = itemView.findViewById(R.id.cv1);
        tv_name = itemView.findViewById(R.id.tv_name);
        iv = itemView.findViewById(R.id.iv);
    }
}

