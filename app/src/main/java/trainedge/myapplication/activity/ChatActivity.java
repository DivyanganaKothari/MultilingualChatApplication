package trainedge.myapplication.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import trainedge.myapplication.R;
import trainedge.myapplication.adapter.MessageListAdapter;
import trainedge.myapplication.model.MessageList;

public class ChatActivity extends AppCompatActivity {

    public OkHttpClient client = new OkHttpClient();
    String senderEmail;
    String receiverId;
    String receiverEmail;
    private FirebaseUser currentuser;
    private String senderId;
    private EditText edittext_chatbox;
    private String receiver_lang;
    private SharedPreferences lang_pref;
    private String sender_lang;
    private String conv_key;
    private RecyclerView recyclerview_message_list;
    private List<MessageList> chatList;
    private MessageListAdapter mAdapter;
    private Button btn;
    private String recName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            receiverId = getIntent().getStringExtra("id");
            receiverEmail = getIntent().getStringExtra("email");
            receiver_lang = getIntent().getStringExtra("lang");
            conv_key = getIntent().getStringExtra("conv_key");
            recName = getIntent().getStringExtra("name");
            getSupportActionBar().setTitle(recName);
        }

        edittext_chatbox = (EditText) findViewById(R.id.edittext_chatbox);
        btn = (Button) findViewById(R.id.buttonSend);
        chatList = new ArrayList<>();
        currentuser = FirebaseAuth.getInstance().getCurrentUser();

        lang_pref = getSharedPreferences("lang_pref", MODE_PRIVATE);

        final DatabaseReference myContactsDb = FirebaseDatabase.getInstance().getReference("messages").child(conv_key);

        senderId = currentuser.getUid();
        senderEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        sender_lang = lang_pref.getString("lang_key", "");
        HashMap<String, String> map = new HashMap<>();
        map.put("person1",senderId);
        map.put("person2",receiverId);
        myContactsDb.setValue(map);
        mAdapter = new MessageListAdapter(this, chatList);

        recyclerview_message_list = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        recyclerview_message_list.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_message_list.setAdapter(mAdapter);





        myContactsDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatList.clear();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equals("person1") || snapshot.getKey().equals("person2")) {
                            continue;
                        } else {
                            chatList.add(snapshot.getValue(MessageList.class));
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    int size = chatList.size();
                    recyclerview_message_list.smoothScrollToPosition(size-1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final long Time = Calendar.getInstance().getTime().getTime();

                String content = edittext_chatbox.getText().toString();
                if (content.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "write something", Toast.LENGTH_SHORT).show();
                    return;
                }
                MessageList msgTobeSent = new MessageList(receiverId, senderId, Time, content, receiver_lang, sender_lang);
                myContactsDb.push().setValue(msgTobeSent);
                edittext_chatbox.setText("");

            }
        });


    }
    /** Translate a given text between a source and a destination language */
    public String translate(String text,String firstLang,String secondLang) {
        String translated="";
        String url = String.format("http://mymemory.translated.net/api/get?q=%s!&langpair=%s|%s&key=%s", text, firstLang, secondLang,getResources().getString(R.string.translation_key));
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
    class Translation extends AsyncTask<Object,Void,Void> {
        @Override
        protected Void doInBackground(Object... objects) {


            return null;
        }
    }
}


