
package trainedge.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import spencerstudios.com.fab_toast.FabToast;
import trainedge.myapplication.R;
import trainedge.myapplication.activity.HomeActivity;
import trainedge.myapplication.adapter.ContactAdapter;
import trainedge.myapplication.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContactsToChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ContactsToChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private RecyclerView rv1;
    private ImageView ivSearch;
    private EditText et_search;
    private boolean isLoaded = false;
    private List<User> data;
    private TextView tv_name;
    private ContactAdapter cAdapter;

    public ContactsToChatFragment() {
        // Required empty public constructor
    }

    public static ContactsToChatFragment newInstance(String param1, String param2) {
        ContactsToChatFragment fragment = new ContactsToChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contacts_to_chat, container, false);
        rv1 = view.findViewById(R.id.rv1);
        tv_name = view.findViewById(R.id.tv_name);
        final List<User> myContacts = new ArrayList<>();
        final List<User> contacts = new ArrayList<>();
        final List<String> frdId = new ArrayList<String>();
        cAdapter = new ContactAdapter(myContacts, (HomeActivity) getActivity());

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        rv1.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        rv1.setItemAnimator(itemAnimator);
        rv1.setAdapter(cAdapter);

        final DatabaseReference allDb = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference myContactsDb = FirebaseDatabase.getInstance().getReference(currentUser.getUid()).child("Contacts");
        myContactsDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myContacts.clear();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        frdId.add(snapshot.getKey());
                    }
                }
                isLoaded = true;
                findContact(allDb, myContacts, frdId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {
                //    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void findContact(DatabaseReference allDb, final List<User> myContacts, final List<String> frdId) {
        if (isLoaded) {
            // showProgressDialog("Finding...");
           // Toast.makeText(getActivity(), "Finding...", Toast.LENGTH_SHORT).show();

            allDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int pos = 0;
                    myContacts.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (frdId.contains(snapshot.getKey())) {
                                myContacts.add(snapshot.getValue(User.class));
                            }
                        }
                    } else {
                        FabToast.makeText(getActivity(), "could not find data", FabToast.LENGTH_SHORT, FabToast.SUCCESS, FabToast.POSITION_CENTER).show();
                    }

                    if (myContacts.size() > 0) {
                        cAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (databaseError != null) {
                        FabToast.makeText(getActivity(), "error", FabToast.LENGTH_SHORT,FabToast.ERROR,FabToast.POSITION_CENTER).show();
                    }

                }
            });
        }
    }
}
