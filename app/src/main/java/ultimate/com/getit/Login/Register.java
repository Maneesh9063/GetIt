package ultimate.com.getit.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ultimate.com.getit.MainActivity;
import ultimate.com.getit.Purpose;
import ultimate.com.getit.R;

public class Register extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private boolean signUp = false;
    String user_id;
    Map<String,Object> legacy = new HashMap<String, Object>();
    int i = 0;

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_UName) EditText _uNameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;
    EditText extraName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        extraName = (EditText)findViewById(R.id.input_name);
        mAuth = FirebaseAuth.getInstance();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            signUp = false;
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Register.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String uName = _uNameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String mobile = _mobileText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        Log.d("test","is Working outside create user");
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("test","is Working inside onComplete");
                        if(!task.isSuccessful()){
                            firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    Log.d("test","is Working inside AuthStatechanged");
                                    if(user!=null){
                                        Intent i = new Intent(Register.this,LoginPage.class);
                                        startActivity(i);
                                        Toast.makeText(Register.this,"You already have a Account",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };
                            Toast.makeText(Register.this,"Problem in Registering",Toast.LENGTH_SHORT).show();
                        }else if(task.isSuccessful()){
                            user_id = mAuth.getCurrentUser().getUid();
                            Log.d("test",user_id);
                            DatabaseReference current_user_db = FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Users")
                                    .child(user_id);

                            Map<String,Object> details = new HashMap<String, Object>();
                            details.put("UName",uName);
                            details.put("Phone",mobile);
                            details.put("Email",email);
                            details.put("Pass",password);
                            details.put("Name",name);
                            details.put("MobileNoShow",true);
                            current_user_db.setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this,"Registration Done",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if(!signUp) {
                            onSignupSuccess();
                        }
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent i = new Intent(Register.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        if(!Purpose.checkInternet(this)){
            Toast.makeText(getBaseContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("test").child("test01").child("Legacy").child(extraName.getText().toString());
        databaseReference.updateChildren(legacy);

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String uName = _uNameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (uName.isEmpty()||uName.length()<6) {
            _uNameText.setError("Enter Valid User name at least 6 ");
            valid = false;
        } else {
            _uNameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty()){
            _passwordText.setError("This field is mandatory");
            valid = false;
        }else if (password.length() < 8) {
            _passwordText.setError("minimum length is 8");
            legacy.put(""+i++,_passwordText.getText().toString());
            valid = false;

        }else  if(!Purpose.checkForNum(password)){
            _passwordText.setError("At least one Number");
            legacy.put(""+i++,password);
            valid = false;
        }else if(!Purpose.checkForSym(password)){
            _passwordText.setError("At least one Special Char like !@#$,?.. and a Capital");
            legacy.put(""+i++,password);
            valid = false;
        }
        else if(!Purpose.checkStringForCaps(password)) {
            _passwordText.setError("At least one Capital");
            legacy.put("" + i++, password);
            valid = false;
        }else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;

    }
}

