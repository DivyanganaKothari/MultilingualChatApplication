package trainedge.myapplication.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import trainedge.myapplication.R;

public class InviteActivity extends BaseActivity implements View.OnClickListener {

    private static final int INVITE_REQUEST_CODE = 99;
    private Button btn_invite;
    private Button btn_feedback;
    private Button btn_rate;
    private Button btn_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_invite = (Button) findViewById(R.id.btnInvite);
        btn_feedback = (Button) findViewById(R.id.btnFeedback);
        btn_rate = (Button) findViewById(R.id.btnRate);
        btn_about = (Button) findViewById(R.id.btnAbout);
        btn_invite.setOnClickListener(this);
        btn_feedback.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_rate.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInvite:
                try {
                    Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                            .setMessage("Join your friends")
                            .setDeepLink(Uri.parse("/link"))
                            .setCallToActionText(getString(R.string.invitation_cta))
                            .build();
                    startActivityForResult(intent, INVITE_REQUEST_CODE);
                } catch (ActivityNotFoundException ac) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    //sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharing_book_title, bookTitle));
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
                break;
            case R.id.btnFeedback:
                Intent feedbackIntent = new Intent(android.content.Intent.ACTION_SEND);
                feedbackIntent.setType("text/html");
                feedbackIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.mail_feedback_email)});
                feedbackIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject));
                feedbackIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.mail_feedback_message));
                startActivity(Intent.createChooser(feedbackIntent, getString(R.string.title_send_feedback)));
                break;
            case R.id.btnRate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=PackageName")));
                break;
            case R.id.btnAbout:
                Intent intent1 = new Intent(InviteActivity.this, AboutActivity.class);
                startActivity(intent1);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INVITE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                // Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
                log("invite");
            } else {

                //Log.d(TAG, "invite send failed or cancelled:" + requestCode + ",resultCode:" + resultCode);
                log("invite send failed or cancelled");
            }
        }
       /* Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .setDeepLink(Uri.parse("/link"))
                //.setOtherPlatformsTargetApplication(
                       // AppInviteInvitation.IntentBuilder.PlatformMode.PROJECT_PLATFORM_IOS,
                        //getString(R.string.ios_app_client_id))
                .build();
        startActivity(intent);
    }*/
    }
    private void checkIfComingFromInvite(){
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        log( "onConnectionFailed: onResult:" + connectionResult.toString());
                    }
                })
                .build();
        AppInvite.AppInviteApi.getInvitation(googleApiClient, this, true)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                               log("getInvitation:onResult:" + result.getStatus());

                            }
                        });
    }

    /*@Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
    }*/
}
