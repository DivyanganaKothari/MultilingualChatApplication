package trainedge.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import trainedge.myapplication.R;
import trainedge.myapplication.adapter.InfoAdapter;
import trainedge.myapplication.model.InfoModel;

public class LanguageActivity extends BaseActivity implements View.OnClickListener {

    public static final String LANG_PREF = "lang_pref";
    public static final String IS_VISITED = "is_visited";
    public static final String LANG_KEY = "lang_key";
    private RecyclerView rv;
    private SharedPreferences lang_pref;
    private ArrayList<InfoModel> data, actaulData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        lang_pref = getSharedPreferences("lang_pref", MODE_PRIVATE);
        lang_pref = getSharedPreferences(LANG_PREF, MODE_PRIVATE);
        if (lang_pref.getBoolean(IS_VISITED, false)) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        //List<InfoModel> data = fill_with_data();
        fill_with_data();
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        InfoAdapter iAdapter = new InfoAdapter(this, data,actaulData);
        rv.setAdapter(iAdapter);
        // Intent intent= new Intent(LanguageActivity.this,HomeActivity.class);
        //startActivity(intent);
        //finish();
        //RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        //itemAnimator.setAddDuration(1000);
        //ItemAnimator.setRemoveDuration(1000);
        //rv.setItemAnimator(itemAnimator);

    }

    private void fill_with_data() {

        // List<InfoModel> data = new ArrayList<>();
        data = new ArrayList<>();
        actaulData = new ArrayList<>();
        data.add(new InfoModel("English"));
        actaulData.add(new InfoModel("english"));
        data.add(new InfoModel("Hindi हिन्दी"));
        actaulData.add(new InfoModel("hindi"));
        data.add(new InfoModel("Turkish Türkçe "));
        actaulData.add(new InfoModel("turkish"));
        data.add(new InfoModel("Swedish Svenska"));
        actaulData.add(new InfoModel("swedish"));
        data.add(new InfoModel("Arabic  اللغة العربي "));
        actaulData.add(new InfoModel("arabic "));
        data.add(new InfoModel("French french  "));
        actaulData.add(new InfoModel("french"));
        data.add(new InfoModel("Chinese  汉语, 漢語, 华语, 華語, or 中文 "));
        actaulData.add(new InfoModel("chinese"));
        data.add(new InfoModel("Czech Český Jazyk, Čeština "));
        actaulData.add(new InfoModel("czech"));
        data.add(new InfoModel("Japanese 日本語, 日本語 "));
        actaulData.add(new InfoModel("japanese"));
    }

    @Override
    public void onClick(View view) {

    }
}




