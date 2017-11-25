package trainedge.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import trainedge.myapplication.R;
import trainedge.myapplication.activity.ChatActivity;
import trainedge.myapplication.holder.ReceiverHolder;
import trainedge.myapplication.holder.SenderHolder;
import trainedge.myapplication.model.MessageList;

/**
 * Created by DIVYANGANA KOTHARI on 26-10-2017.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MessageList> chatList;
    private final ChatActivity chatActivity;
    private final FirebaseUser user;
    public int SENDER = 0;
    public int RECIEVER = 1;
    public OkHttpClient client = new OkHttpClient();

    public MessageListAdapter(ChatActivity chatActivity, List<MessageList> chatList) {
        this.chatList = chatList;
        this.chatActivity = chatActivity;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vReceive = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_card, parent, false);
        View vSend = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_card, parent, false);
        if (viewType == SENDER) {
            return new SenderHolder(vSend);
        }
        return new ReceiverHolder(vReceive);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SenderHolder){
            SenderHolder sh= (SenderHolder) holder;
            MessageList messageList = chatList.get(position);
            sh.text_message_body.setText(messageList.content);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(messageList.Time);
            String date = DateFormat.format("hh:mm", cal).toString();
            sh.text_message_time.setText(date);

        }else {
            ReceiverHolder rh= (ReceiverHolder) holder;
            MessageList messageList = chatList.get(position);
            rh.text_message_body.setText(messageList.content);
            rh.text_message_name.setText("");
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(messageList.Time * 1000L);
            String date = DateFormat.format("dd hh:mm:ss", cal).toString();
            rh.text_message_time.setText(date);
            String translated=translate(rh.text_message_body,messageList.content,messageList.sender_lang,messageList.receiver_lang);
            Glide.with(chatActivity).load(R.drawable.ic_person_outline_black_24dp).into(rh.image_message_profile);

        }

    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageList msgList = chatList.get(position);
        if (msgList.Sender_Id.equals(user.getUid())) {
            return SENDER;
        }
        return RECIEVER;
    }
    public String translate(TextView text_message_body, String text, String firstLang, String secondLang) {
        String translated="";
        String url = String.format("http://mymemory.translated.net/api/get?q=%s!&langpair=%s|%s&key=%s", text, firstLang, secondLang,chatActivity.getResources().getString(R.string.translation_key));
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jObject = null;
            jObject = new JSONObject(response.body().string());
            JSONObject data = jObject.getJSONObject("responseData");
            //result.setText(data.getString("translatedText"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return translated;
    }
}
