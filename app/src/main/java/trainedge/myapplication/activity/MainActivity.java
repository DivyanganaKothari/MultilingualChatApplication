package trainedge.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import trainedge.myapplication.R;

public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN = 9001;
    FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    //private EditText enter_email;
    //private EditText enter_pswrd;
    private TextView or_tv;
    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);


        //enter_email = (EditText) findViewById(R.id.enter_email);
        //enter_pswrd = (EditText) findViewById(R.id.enter_pswrd);
        //Button signup_btn = (Button) findViewById(R.id.signup_btn);
        //signup_btn.setOnClickListener(this);
        //Button signin_btn = (Button) findViewById(R.id.signin_btn);
        //signin_btn.setOnClickListener(this);
        //or_tv = (TextView) findViewById(R.id.or_tv);


        SignInButton google_sign_in = (SignInButton) findViewById(R.id.google_sign_in);
        google_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        //initialize facebook login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //log(loginResult.toString());
                handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(context, "please wait ...", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onCancel() {
                //log("user cancelled");
                Toast.makeText(MainActivity.this, "you cancelled Login", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                //log(error.getMessage());
                Toast.makeText(MainActivity.this, "there was this error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(MainActivity.this, "Auth went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            showProgressDialog("Welcome");
                            saveToDatabase(user);

                        } else {
                            // If sign in fails, display a message to the user.
                           // log(task.getException().getMessage());
                            //og("failed");
                            Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveToDatabase(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            //log("failed" + task.getException().getMessage());
                            Toast.makeText(context, "Authentication Failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }


                });


    }
    private void saveToDatabase(final FirebaseUser user) {
        db = FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String, Object> map=new HashMap<>();
        map.put("email",user.getEmail());
        map.put("photo",user.getPhotoUrl().toString());
        map.put("name",user.getDisplayName());
        map.put("id",user.getUid());
        db.child(user.getUid()).setValue(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    updateUI(user);

                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this,LanguageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        message(connectionResult.getErrorMessage());
    }


 /*   @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signin_btn:
                SignIn();
                break;
            case R.id.signup_btn:
                SignUp();
                break;
        }
    }

    private void SignIn() {
        String email = enter_email.getText().toString();
        String pass = enter_pswrd.getText().toString();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            message(task.getException().getMessage());
                        } else {
                            Intent intent = new Intent(MainActivity.this, LanguageActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });

    }


    private void SignUp() {
        String email = enter_email.getText().toString();
        String pass = enter_pswrd.getText().toString();
        if (email.isEmpty()) {
            enter_email.setText("fill email");
            return;
        }
        if (pass.isEmpty()) {
            enter_pswrd.setText("fill password");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            message(task.getException().getMessage());
                        } else {
                            Intent intent = new Intent(MainActivity.this, LanguageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }*/


}