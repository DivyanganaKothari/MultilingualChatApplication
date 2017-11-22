package trainedge.myapplication.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import trainedge.myapplication.R;
import trainedge.myapplication.activity.ChatActivity;
import trainedge.myapplication.activity.HomeActivity;
import trainedge.myapplication.holder.Contact_Holder;
import trainedge.myapplication.model.User;

/**
 * Created by DIVYANGANA KOTHARI on 18-11-2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<Contact_Holder> {
    List<User> chatName;
    HomeActivity context;

    public ChatAdapter(List<User> chatName, HomeActivity context) {
        this.chatName=chatName;
        this.context=context;

    }

    @Override
    public Contact_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_search_view2,parent,false);
        return new Contact_Holder(v);
    }

    @Override
    public void onBindViewHolder(Contact_Holder  holder, int position) {
        final User data=chatName.get(position);
        holder.tv_name.setText(data.name);
        Glide.with(context).load(data.photo).into(holder.iv);
        holder.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatActivity.class);

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
