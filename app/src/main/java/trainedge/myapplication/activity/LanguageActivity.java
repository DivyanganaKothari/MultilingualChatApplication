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
        actaulData.add(new InfoModel("en"));
        data.add(new InfoModel("Hindi हिन्दी"));
        actaulData.add(new InfoModel("hi"));
        data.add(new InfoModel("Greek "));
        actaulData.add(new InfoModel("el"));
        data.add(new InfoModel("Spanish"));
        actaulData.add(new InfoModel("es"));
        data.add(new InfoModel("Italian"));
        actaulData.add(new InfoModel("it "));
        data.add(new InfoModel("French french  "));
        actaulData.add(new InfoModel("fr"));
        data.add(new InfoModel("Chinese  汉语, 漢語, 华语, 華語, or 中文 "));
        actaulData.add(new InfoModel("zh"));
        data.add(new InfoModel("German  "));
        actaulData.add(new InfoModel("de"));
        data.add(new InfoModel("Portuguese "));
        actaulData.add(new InfoModel("pt"));
        data.add(new InfoModel("Japanese"));
        actaulData.add(new InfoModel("ja"));
        data.add(new InfoModel("Arabic"));
        actaulData.add(new InfoModel("ar"));
        data.add(new InfoModel("Bulgarian"));
        actaulData.add(new InfoModel("bg"));
        data.add(new InfoModel("Irish"));
        actaulData.add(new InfoModel("ga"));
        data.add(new InfoModel("Korean"));
        actaulData.add(new InfoModel("ko"));
        data.add(new InfoModel("Latin"));
        actaulData.add(new InfoModel("la"));
        data.add(new InfoModel("Persian"));
        actaulData.add(new InfoModel("fa"));
        data.add(new InfoModel("Russian"));
        actaulData.add(new InfoModel("ru"));
        data.add(new InfoModel("Uzbek"));
        actaulData.add(new InfoModel("uz"));
        data.add(new InfoModel("Swedish"));
        actaulData.add(new InfoModel("sv"));
        data.add(new InfoModel("Vietnamese"));
        actaulData.add(new InfoModel("vi"));
        data.add(new InfoModel("Albanian"));
        actaulData.add(new InfoModel("sq"));
        data.add(new InfoModel("Indonesian"));
        actaulData.add(new InfoModel("id"));
        data.add(new InfoModel("Georgian"));
        actaulData.add(new InfoModel("ka"));
        data.add(new InfoModel("Danish"));
        actaulData.add(new InfoModel("da"));
        data.add(new InfoModel("Romanian"));
        actaulData.add(new InfoModel("ro"));




    }

    @Override
    public void onClick(View view) {

    }
}




