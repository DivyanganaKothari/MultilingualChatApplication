package trainedge.myapplication.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import trainedge.myapplication.R;

/**
 * Created by DIVYANGANA KOTHARI on 19-08-2017.
 */

public class View_Holder extends RecyclerView.ViewHolder {

        public CardView cv;
        public TextView title;

        public View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);

        }
        }

