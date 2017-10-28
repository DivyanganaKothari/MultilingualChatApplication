package trainedge.myapplication.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import trainedge.myapplication.R;
import trainedge.myapplication.model.User;

public class ContactsActivity extends BaseActivity {
    private static final int REQUEST_SELECT_CONTACT = 23;
    Uri contactUri;
    String contactID;
    private RecyclerView rv1;
    private TextView tv_name;
    private EditText et_search;
    private String searchTerm;
    private ImageView ivSearch;
    private FirebaseUser currentUser;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final List<User> data = new ArrayList<>();
        rv1 = (RecyclerView) findViewById(R.id.rv1);
        tv_name = (TextView) findViewById(R.id.tv_name);
        et_search = (EditText) findViewById(R.id.et_search);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        //final SearchAdapter sAdapter = new SearchAdapter(data, this);
        //rv1.setAdapter(sAdapter);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        rv1.setItemAnimator(itemAnimator);
        final List<String> loadMyContacts = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference("Users");
        showProgressDialog("loading...");
        DatabaseReference myContactsDb = FirebaseDatabase.getInstance().getReference(currentUser.getUid()).child("Contacts");
        myContactsDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadMyContacts.clear();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        loadMyContacts.add(snapshot.getKey());

                    }
                    hideProgressDialog();
                    isLoaded = true;
                }
            }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            if (databaseError != null) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTerm = et_search.getText().toString().trim();
                if (isLoaded) {
                    showProgressDialog("Finding...");
                    usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int pos = 0;
                            data.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                boolean isPresent = false;
                                String email = snapshot.child("email").getValue(String.class);
                                String name = snapshot.child("name").getValue(String.class);
                                String photo = snapshot.child("photo").getValue(String.class);
                                String id = snapshot.getKey();
                                for (int i = 0; i < loadMyContacts.size(); i++) {
                                    if (loadMyContacts.get(i).equals (id)) {
                                        isPresent = true;
                                    }
                                }
                                if (email != null && email.contains(searchTerm) && !email.equals(currentUser.getEmail())&& !isPresent) {
                                    //add to arraylist
                                    //sAdapter.insert(pos, new User(name, email, id, photo));
                                }
                                pos++;
                            }
                            if (data.size() == 0) {
                                data.clear();
                               // sAdapter.notifyDataSetChanged();
                            }
                            hideProgressDialog();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            hideProgressDialog();
                        }

                    });
                } else {
                    Toast.makeText(context, "data is still loading", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

