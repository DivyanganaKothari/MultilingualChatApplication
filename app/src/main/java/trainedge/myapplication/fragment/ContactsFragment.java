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

import spencerstudios.com.fab_toast.FabToast;
import trainedge.myapplication.R;
import trainedge.myapplication.activity.HomeActivity;
import trainedge.myapplication.adapter.SearchAdapter;
import trainedge.myapplication.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private boolean isLoaded = false;
    private List<User> data;
    private RecyclerView rv1;
    private TextView tv_name;
    private EditText et_search;
    private ImageView ivSearch;


    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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
        view = inflater.inflate(R.layout.fragment_contacts2, container, false);
        rv1 = view.findViewById(R.id.rv1);
        tv_name = view.findViewById(R.id.tv_name);
        et_search = view.findViewById(R.id.et_search);
        ivSearch = view.findViewById(R.id.ivSearch);

        final SearchAdapter sAdapter = new SearchAdapter(data,(HomeActivity)getActivity());
        rv1.setAdapter(sAdapter);
        rv1.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        rv1.setItemAnimator(itemAnimator);
        final List<String> loadMyContacts = new ArrayList<>();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference("Users");
        //showProgressDialog("loading...");
        DatabaseReference myContactsDb = FirebaseDatabase.getInstance().getReference(currentUser.getUid()).child("Contacts");
        myContactsDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadMyContacts.clear();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        loadMyContacts.add(snapshot.getKey());

                    }
                    //hideProgressDialog();
                 //   Toast.makeText(getActivity(), "Data has been loaded", Toast.LENGTH_SHORT).show();
                }
                isLoaded = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        FabToast.makeText(getActivity(), "PRESS THE SEARCH ICON TO SEE YOUR FRIENDS",FabToast.LENGTH_LONG,FabToast.INFORMATION,FabToast.POSITION_CENTER).show();
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String searchTerm = et_search.getText().toString().trim();
                if (isLoaded) {
                   // showProgressDialog("Finding...");
                    //Toast.makeText(getActivity(), "Finding...", Toast.LENGTH_SHORT).show();
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
                                String lang=snapshot.child("Language").getValue(String.class);
                                String id = snapshot.getKey();
                                for (int i = 0; i < loadMyContacts.size(); i++) {
                                    if (loadMyContacts.get(i).equals (id)) {
                                        isPresent = true;
                                    }
                                }
                                if (email != null && email.contains(searchTerm) && !email.equals(currentUser.getEmail())&& !isPresent) {
                                    //add to arraylist
                                    sAdapter.insert(pos, new User(name, email, id, photo,lang));
                                }
                                pos++;
                            }
                            if (data.size() == 0) {
                                data.clear();
                                sAdapter.notifyDataSetChanged();
                            }
                           // hideProgressDialog();

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                         //   hideProgressDialog();
                        }

                    });
                } else {
                    FabToast.makeText(getActivity(), "could not find data", FabToast.LENGTH_SHORT,FabToast.ERROR,FabToast.POSITION_CENTER).show();
                }

            }
        });

        return view;


    }


}
