package ultimate.com.getit;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AccountSettings extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    DatabaseReference mRef;
    EditText name,uName,phone,eMail;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        name = (EditText)findViewById(R.id.nameSetting);
        uName = (EditText)findViewById(R.id.userNameSetting);
        phone = (EditText)findViewById(R.id.phoneSetting);
        eMail = (EditText)findViewById(R.id.emailSetting);
        submit = (Button)findViewById(R.id.submitSetting);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            uid = user.getUid();
        }else {
            Toast.makeText(AccountSettings.this,"I think ur not Logged in",Toast.LENGTH_SHORT).show();
        }
        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersData obj = dataSnapshot.getValue(UsersData.class);
                if(obj!=null){
                    name.setText(obj.getName());
                    uName.setText(obj.getUName());
                    phone.setText(obj.getPhone());
                    eMail.setText(obj.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Switch aSwitch = (Switch) findViewById(R.id.mobileNoShow);
                if (aSwitch.isChecked()) {
                    // The toggle is enabled
                    Purpose.mobileNOShow = true;
                } else {
                    // The toggle is disabled
                    Purpose.mobileNOShow = false;
                }
    }

    public void submit(View view) {
        if(validate()) {
            Map<String, Object> details = new HashMap<String, Object>();
            details.put("UName", uName.getText().toString());
            details.put("Phone", phone.getText().toString());
            details.put("Email", eMail.getText().toString());
            details.put("Name", name.getText().toString());
            details.put("MobileNoShow", Purpose.mobileNOShow);
            mRef.updateChildren(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AccountSettings.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    public boolean validate() {
        boolean valid = true;
        String vname = name.getText().toString();
        String vuName = uName.getText().toString();
        String vemail = eMail.getText().toString();
        String vmobile = phone.getText().toString();

        if (vname.isEmpty() || name.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (vuName.isEmpty()||uName.length()<6) {
            uName.setError("Enter Valid User name at least 6 ");
            valid = false;
        } else {
            uName.setError(null);
        }
        if (vemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(vemail).matches()) {
            eMail.setError("enter a valid email address");
            valid = false;
        } else {
            eMail.setError(null);
        }
        if (vmobile.isEmpty() || vmobile.length()!=10) {
            phone.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            phone.setError(null);
        }
        return valid;
    }

}
