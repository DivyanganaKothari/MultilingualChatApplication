package trainedge.myapplication.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import spencerstudios.com.fab_toast.FabToast;
import trainedge.myapplication.R;
import trainedge.myapplication.activity.HomeActivity;
import trainedge.myapplication.activity.LanguageActivity;
import trainedge.myapplication.holder.View_Holder;
import trainedge.myapplication.model.InfoModel;

import static android.content.Context.MODE_PRIVATE;

public class InfoAdapter extends RecyclerView.Adapter<View_Holder> {

    //Context context;
    private final LanguageActivity activity;
    private final ArrayList<InfoModel> actualData;
    ArrayList<InfoModel> data;


    private DatabaseReference languageChoice;
    private SharedPreferences lang_pref;


    public InfoAdapter(LanguageActivity activity, ArrayList<InfoModel> data, ArrayList<InfoModel> actualData) {
        this.data = data;
        this.actualData = actualData;
        this.activity = activity;
        languageChoice = FirebaseDatabase.getInstance().getReference("Users");
        lang_pref = activity.getSharedPreferences(LanguageActivity.LANG_PREF, MODE_PRIVATE);
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(activity).inflate(R.layout.activity_info_adapter2, parent, false);
        return new View_Holder(v);

    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        InfoModel item = data.get(position);
        final InfoModel actualItem = actualData.get(position);
        holder.title.setText(item.getTitle());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showProgressDialog("Updating your Preferences");
                String lang_name = actualItem.getTitle();
                addToFirebase(lang_name);
            }
        });
    }

    private void addToFirebase(final String lang_name) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        languageChoice.child(currentUser.getUid()).child("language").setValue(lang_name, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    FabToast.makeText(activity, "Success", FabToast.LENGTH_SHORT,FabToast.SUCCESS, FabToast.POSITION_CENTER).show();
                    SharedPreferences.Editor e = lang_pref.edit();
                    e.putString(LanguageActivity.LANG_KEY, lang_name);
                    e.putBoolean(LanguageActivity.IS_VISITED, true);
                    e.apply();
                    Intent home = new Intent(activity, HomeActivity.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.hideProgressDialog();
                    activity.startActivity(home);
                    activity.finish();
                } else {
                    FabToast.makeText(activity, databaseError.getMessage(), FabToast.LENGTH_SHORT,FabToast.SUCCESS,FabToast.POSITION_CENTER).show();
                }
                // activity.hideProgressDialog();
            }


        });
    }


    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return data.size();
    }


}