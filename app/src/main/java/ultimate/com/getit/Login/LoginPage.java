package ultimate.com.getit.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import ultimate.com.getit.MainActivity;
import ultimate.com.getit.R;

//TODO : Forgot password feature

public class LoginPage extends AppCompatActivity {

    private Button mlogin;
    private EditText mEmail ,mPassword;
    private boolean isLogin;

    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener fireBaseAuthListner;

    //Defining all required variables here

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mauth = FirebaseAuth.getInstance();

        isLogin = false;

        fireBaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    //if the user is already logged in it simply goes to main activity

                    Intent i = new Intent(LoginPage.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        mlogin = (Button)findViewById(R.id.login);
        TextView mregister = (TextView) findViewById(R.id.resgister);

//      else it takes the email and password from user

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
//        calling login() method

        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPage.this,Register.class);
                startActivityForResult(i,REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                animating the view for changing the intent to registering
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
//            validates the user data
            isLogin = false;
            return;
        }

        mlogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginPage.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

//        progress dialog the main purpose here is to wait until it gets the confirmation from the server

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        // TODO: Implement your own authentication logic here.

            mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {

//                logins the user with the email and password

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        onLoginFailed();
                        isLogin = false;
                    } else {
                        Toast.makeText(LoginPage.this, "Login Completed", Toast.LENGTH_SHORT).show();

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        try {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("IsLoggedIn").setValue(true);
                        }catch (Exception e) {

                        }
                        Intent i = new Intent(LoginPage.this,MainActivity.class);
                        startActivity(i);
                        isLogin = true;

                    }
                }
            });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(isLogin) {
                            onLoginSuccess();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                this.finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(fireBaseAuthListner);
//      TODO : need to figure out for what this is

    }

    @Override
    protected void onStop() {
        super.onStop();
        mauth.removeAuthStateListener(fireBaseAuthListner);
//        todo : Same goes here
    }

//    validating function which checks the email and password requirements

    public boolean validate() {
        boolean valid = true;

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("enter a valid email address");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 ) {
            mPassword.setError("At least 8  alphanumeric characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        mlogin.setEnabled(true);
        if(!checkInternet()){
            Toast.makeText(getBaseContext(), "Please connect to Internet", Toast.LENGTH_LONG).show();
        }
    }
    public void onLoginSuccess() {
        mlogin.setEnabled(true);
//        Intent i = new Intent(LoginPage.this,MainActivity.class);
//        startActivity(i);
        finish();
    }
    public  boolean checkInternet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;
        return connected;
    }
}
