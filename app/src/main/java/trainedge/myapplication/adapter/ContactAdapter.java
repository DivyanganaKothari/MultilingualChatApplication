package trainedge.myapplication.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import trainedge.myapplication.R;
import trainedge.myapplication.activity.ChatActivity;
import trainedge.myapplication.activity.HomeActivity;
import trainedge.myapplication.holder.Contact_Holder;
import trainedge.myapplication.model.ContactModel;
import trainedge.myapplication.model.User;

/**
 * Created by DIVYANGANA KOTHARI on 09-10-2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<Contact_Holder> {
    //public static final String id_key="trainedge.demotraining";
    List<User> list;
    HomeActivity context;
    boolean keysLoaded=false;

    public ContactAdapter(List<User> list, HomeActivity context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Contact_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_search_view2, parent, false);
        return new Contact_Holder(v);

    }

    @Override
    public void onBindViewHolder(Contact_Holder holder, int position) {

        final User data = list.get(position);
        holder.tv_name.setText(data.name);
        Glide.with(context).load(data.photo).into(holder.iv);
        holder.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, ChatActivity.class);
                Bundle extras=new Bundle();
                extras.putString("id",data.id);
                extras.putString("email",data.email);
                extras.putString("lang",data.language);
                extras.putString("name",data.name);
                intent1.putExtras(extras);
                ConverstationNodeKey(FirebaseAuth.getInstance().getCurrentUser().getEmail(),data.email,intent1);

            }
        });
    }

    private void ConverstationNodeKey(String senderEmail, String receiverEmail1, final Intent intent1) {
       final String testNode1 = concatEmails(senderEmail, receiverEmail1);
        final String testNode2 = concatEmails(receiverEmail1, senderEmail);
        FirebaseDatabase.getInstance().getReference("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key=null;
                if(dataSnapshot.hasChildren()){
                    if(dataSnapshot.hasChild(testNode1)){
                        key=testNode1;
                    }
                    else if(dataSnapshot.hasChild(testNode2)){
                        key=testNode2;
                    }
                    else {
                        key=testNode1;
                    }
                }
                else {
                    key=testNode1;
                }
                intent1.putExtra("conv_key",key);
                context.startActivity(intent1);
                keysLoaded=true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String concatEmails(String senderEmail, String receiverEmail) {
        String temp=senderEmail+receiverEmail;
        temp=temp.replace('.','_');
        temp=temp.replace('@','_');
        return temp;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void insert(int position, User data) {
        list.add(data);
        notifyItemInserted(position);
    }


    public void remove(ContactModel data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);

    }
}