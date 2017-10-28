package trainedge.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import trainedge.myapplication.R;
import trainedge.myapplication.activity.ChatActivity;
import trainedge.myapplication.holder.MessageHolder;
import trainedge.myapplication.model.MessageList;

/**
 * Created by DIVYANGANA KOTHARI on 26-10-2017.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageHolder> {

    private final List<MessageList> chatList;
    private final ChatActivity chatActivity;

    public MessageListAdapter(ChatActivity chatActivity, List<MessageList> chatList) {
        this.chatList = chatList;
        this.chatActivity = chatActivity;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_card,parent,false);
        return new MessageHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        MessageList messageList = chatList.get(position);
        holder.text_message_body.setText(messageList.content);
        holder.text_message_time.setText(String.valueOf(messageList.Time));
        holder.image_message_profile.setVisibility(View.GONE);
        holder.text_message_name.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
