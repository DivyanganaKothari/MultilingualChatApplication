package trainedge.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

/**
 * Created by hp on 04-08-2017.
 */

public class BaseActivity extends AppCompatActivity {

    public static final String PACKAGE_NAME = "trainedge.myapplication";

    public static final String APP_NAME = "Multilingual-Chat-App";

    public Context context = this;

    public ProgressDialog dialog;
    private AnimatedCircleLoadingView animatedCircleLoadingView;


    public void showProgressDialog(String msg) {

        dialog = new ProgressDialog(this);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();

    }

  /*  private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(65);
                        changePercent(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }

    public void resetLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.resetLoading();
            }
        });
    }

*/

    public void hideProgressDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    public void message(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void messageBar(View v, String msg) {
        //require design library
        //Snackbar.make(v,msg,Snackbar.LENGTH_LONG).show();

    }

    public void showAlert(String title, String message, String yes, String no, int icon) {


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();

    }

    public void log(String data) {
        Log log = null;
        log.d("trainedge.myapplication", data);

    }



}
