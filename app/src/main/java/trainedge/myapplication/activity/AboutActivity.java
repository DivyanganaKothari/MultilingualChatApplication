package trainedge.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import trainedge.myapplication.R;

public class AboutActivity extends AppCompatActivity {

    private ImageView ivlogo;
    private TextView tvname;
    private TextView tv_v;
    private TextView tv_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ivlogo = (ImageView) findViewById(R.id.ivlogo);
        tvname = (TextView) findViewById(R.id.tvname);
        tv_contact = (TextView) findViewById(R.id.tv_contact);
    }

}
