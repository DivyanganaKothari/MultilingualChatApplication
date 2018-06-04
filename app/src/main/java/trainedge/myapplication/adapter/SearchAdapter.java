package trainedge.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import trainedge.myapplication.R;
import trainedge.myapplication.activity.HomeActivity;
import trainedge.myapplication.holder.Search_Holder;
import trainedge.myapplication.model.SearchModel;
import trainedge.myapplication.model.User;

/**
 * Created by DIVYANGANA KOTHARI on 01-09-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<Search_Holder> {

   // List<User> list = Collections.emptyList();
   List<User> list;
    HomeActivity context;
    private  FirebaseUser currentUser;
    private  DatabaseReference invitedUser;
    private DatabaseReference ContactChoice;


    public SearchAdapter(){}
    public SearchAdapter(List<User> list, HomeActivity context) {
        this.list = list;
        this.context = context;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ContactChoice = FirebaseDatabase.getInstance().getReference(currentUser.getUid());
        invitedUser = ContactChoice.child("Contacts");
    }

    @Override
    public Search_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_search_view, parent, false);
        //Search_Holder holder = new Search_Holder(v);
        return new Search_Holder(v);

    }

    @Override
    public void onBindViewHolder(final Search_Holder holder, int position) {
        final User data = list.get(position);
        holder.tv_name.setText(data.name);
        Glide.with(context).load(data.photo).into(holder.iv);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.showProgressDialog("Adding..");
                addToFirecbase(data,holder.btnAdd);
                //view.setEnabled(false);


            }


        });
    }

    private void addToFirecbase(User data, Button btnAdd) {
        invitedUser.child(data.id).setValue(data.name, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    context.hideProgressDialog();
                }
            }
        });
        btnAdd.setText("Added");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, User data) {
        //list.add(position, data);
        list.add(data);
        notifyItemInserted(position);
    }

    public void remove(SearchModel data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);

    }

}
