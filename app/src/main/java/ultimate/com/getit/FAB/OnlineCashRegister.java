package ultimate.com.getit.FAB;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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

import ultimate.com.getit.Purpose;
import ultimate.com.getit.R;
import ultimate.com.getit.updatingClass;

import static ultimate.com.getit.Purpose.MY_PERMISSIONS_REQUEST_LOCATION;

public class OnlineCashRegister extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText amount;
    DatabaseReference mRef;
    String sAmount;
    double latitude, longitude;
    private static final String TAG = "CurrentLocationApp";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_cash_register);
        amount = (EditText) findViewById(R.id.onlineAmount);

        if(!Purpose.checkInternet(this)){
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }

        buildGoogleApiClient();
    }

    public void updateOnlineRequest(View view) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            final String uid = user.getUid();
            mRef = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Users")
                    .child(uid);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    updatingClass obj = dataSnapshot.getValue(updatingClass.class);
                    mRef = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Online")
                            .child(uid);
                    sAmount = amount.getText().toString();
                    Map<String,Object> map = new HashMap<String,Object>();
                    if(obj!=null) {
//                        updates everything to database
                        map.put("Name", obj.getName());
                        map.put("UName", obj.getUName());
                        map.put("Phone", obj.getPhone());
                        map.put("Amount", sAmount);
                        map.put("TimeOfRegistration",(System.currentTimeMillis()));
                        map.put("uid",uid);
                        map.put("HandCashRequest", false);
                        map.put("Latitude",(latitude));
                        map.put("Longitude",(longitude));

                        mRef.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(OnlineCashRegister.this, "Updated..!!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else{
            Toast.makeText(OnlineCashRegister.this, "Please login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
            if(Purpose.checkLocationPermission(this)) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    latitude = (mLastLocation.getLatitude());
                    longitude = (mLastLocation.getLongitude());
                }
            }else{
                Toast.makeText(this,"Please provide the permissions",Toast.LENGTH_LONG).show();
            }
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"We need to get permission or else we cant show the locations to u",Toast.LENGTH_LONG);

                }
                return;
            }

        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient () {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
    }



}
