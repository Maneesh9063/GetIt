package ultimate.com.getit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import ultimate.com.getit.FAB.HandCashRegister;
import ultimate.com.getit.FAB.OnlineCashRegister;
import ultimate.com.getit.Login.LoginPage;
import ultimate.com.getit.Tabs.Cash;
import ultimate.com.getit.Tabs.Online;

import static ultimate.com.getit.Purpose.MY_PERMISSIONS_REQUEST_LOCATION;
import static ultimate.com.getit.Purpose.uid_local;

//TODO: Had a confusion in the tabs and the tabs are reversly named here they are updating reversly
//      Should make the location more accurate and perfect and learn about it
//      learn about pages

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ViewPager mPage;
    private GoogleApiClient mGoogleApiClient;
    public static double  latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout mTab = (TabLayout) findViewById(R.id.mTabs);
        mPage = (ViewPager)findViewById(R.id.tab1);
        FloatingActionButton handFAB = (FloatingActionButton) findViewById(R.id.fabHand);
        FloatingActionButton onlineFAB = (FloatingActionButton) findViewById(R.id.fabOnline);
        mPage.setAdapter(new PagerAdapter(getSupportFragmentManager(),MainActivity.this));

        mTab.setupWithViewPager(mPage);
//        need to learn the working of this

        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPage.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        OneSignal.startInit(this).init();
        OneSignal.setSubscription(true);

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {

                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);

        if(!checkInternet()){
            Toast.makeText(getBaseContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }
        if(!Purpose.checkLocationPermission(this)){
            Toast.makeText(this,"Please provide the permissions",Toast.LENGTH_LONG).show();
        }

        buildGoogleApiClient();

        handFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,HandCashRegister.class);
                startActivity(i);
            }
        });

        onlineFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,OnlineCashRegister.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!checkInternet()){
            Toast.makeText(getBaseContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }
        if(!Purpose.checkLocationPermission(this)){
            Toast.makeText(this,"Please provide the permissions",Toast.LENGTH_LONG).show();
        }
    }

    public void logut(MenuItem item) {

        OneSignal.setSubscription(false);
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginPage.class);
        startActivity(intent);
        finish();
        try {
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid_local).child("IsLoggedIn").setValue(false);
        }catch (Exception e){

        }
    }

//    these all functions are direct from xml  onclick ...
    public void accountSettings(MenuItem item) {
        Intent i = new Intent(MainActivity.this,AccountSettings.class);
        startActivity(i);
    }

    public void myRequests(MenuItem item) {
        Intent i = new Intent(MainActivity.this ,MyRequests.class);
        startActivity(i);
    }

    class PagerAdapter extends FragmentPagerAdapter
    {
        String titles[] = {"Available Liquid Cash","Available Digital Cash"};
        Context ctx;

        public PagerAdapter(FragmentManager fm,Context c) {
            super(fm);
            ctx = c;
        }
        @Override
        public int getCount() {
            return titles.length;
        }
        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Online();
                case 1:
                    return new Cash();
            }
            return null;
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
//need to know how
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return true;
    }
    public boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return  (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
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
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                latitude = (mLastLocation.getLatitude());
                longitude = (mLastLocation.getLongitude());
            }else{
                Toast.makeText(this,"Please provide the permissions",Toast.LENGTH_LONG).show();
            }
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
