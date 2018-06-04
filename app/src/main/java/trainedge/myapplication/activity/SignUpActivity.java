package trainedge.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import spencerstudios.com.fab_toast.FabToast;
import trainedge.myapplication.R;

public class SignUpActivity extends BaseActivity {

    private static final int REQUEST_SIGNUP = 0;
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private TextView link_login;
    private Button btn_signup;
    private EditText et_pass;
    private EditText et_email;
    private EditText et_name;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText et_pass1;
    private Pattern pattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_pass1 = (EditText) findViewById(R.id.et_pass1);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        link_login = (Button) findViewById(R.id.link_login);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent i = new Intent(SignUpActivity.this, LanguageActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        };

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void signup() {

        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String password = et_pass.getText().toString();
        String pass1=et_pass1.getText().toString();


        if (name.isEmpty()) {
            et_name.setError("Required to fill NAME");
            return;
        }
        if(email.isEmpty()) {
            et_email.setError("Required to fill Email");
            return;
            }

        if (password.isEmpty()) {
            et_pass.setError("Fill Passwword");
           return;
        }
        if (!password.equals(pass1)) {
            et_pass1.setError("Password doesn't match");
            return;
        }
        matcher = Pattern.compile(PASSWORD_PATTERN).matcher(password);


        if (!matcher.matches()) {
            et_pass.setError(
                    "    must contains one digit from 0-9\n" +
                            "     must contains one lowercase characters\n" +
                            "     must contains one uppercase characters\n" +
                            "     must contains one special symbols in the list \"@#$%\"\n" +
                            "     match anything with previous condition checking\n" +
                            "    length at least 6 characters and maximum of 20");
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            FabToast.makeText(SignUpActivity.this, "SignUp Failed! Try Again" + task.getException(),
                                    FabToast.LENGTH_SHORT,FabToast.ERROR,FabToast.POSITION_CENTER).show();
                            updateUI(null);
                        } else {

                            FabToast.makeText(SignUpActivity.this,"User Successfully Registered! Login Again", FabToast.LENGTH_SHORT,FabToast.SUCCESS,FabToast.POSITION_CENTER).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            // ...
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
            Intent intent = new Intent(SignUpActivity.this, LanguageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

/*    @Override
    public void onClick(View view) {
        int id;
        id=view.getId();
        if (id==R.id.link_login){
            Intent intent=new Intent(SignupActivity.this,MainActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }
        if (id==R.id.btn_signup){
            name = et_name.getText().toString();
            email = et_email.getText().toString();
            password = et_pass.getText().toString();
            if (name.isEmpty() || name.length() < 3) {
                et_name.setError("at least 3 characters");
            } else {
                et_name.setError(null);
            }
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                et_email.setError("enter a valid email address");
            } else {
                et_email.setError(null);
            }
            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                et_pass.setError("between 4 and 10 alphanumeric characters");
            } else {
                et_pass.setError(null);
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(SignupActivity.this,"User Successfully Registered", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        }
                    });
        }
    }*/
}
