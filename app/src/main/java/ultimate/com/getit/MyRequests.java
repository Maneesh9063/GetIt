package ultimate.com.getit;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ultimate.com.getit.Adapters.MyOwnAdapter;

public class MyRequests extends AppCompatActivity {

    String uid = FirebaseAuth.getInstance().getUid();
    DatabaseReference mRef;
    ArrayList<updatingClass> details;
    TextView tv;
    Button hand,online;
    MyOwnAdapter myOA;
    boolean handExists = false,onlineExists = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        hand = (Button)findViewById(R.id.deleteHandrequest);
        online = (Button)findViewById(R.id.deleteOnlinerequest);
//        tv =(TextView)findViewById(R.id.myRequestsTV);

//        if(!handExists && !onlineExists){
//            hand.setVisibility(View.GONE);
//            online.setVisibility(View.GONE);
//
//            tv.setVisibility(View.VISIBLE);
//        }
//        else if(handExists) {
//            hand.setVisibility(View.VISIBLE);
//            tv.setVisibility(View.GONE);
//        }
//        else  if(onlineExists){
//            online.setVisibility(View.VISIBLE);
//            tv.setVisibility(View.GONE);
//
//        }

    }

    public void deleteHandCashRequest(View view) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Hand");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(uid).exists()) {
                    handExists = true;
                    reference.child(uid).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteOnlneCashRequest(View view) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Online");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(uid).exists()) {
                    onlineExists = true;
                    reference.child(uid).removeValue();
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//            recyclerView = (RecyclerView)findViewById(R.id.myRequestsRV);
//            tv =(TextView)findViewById(R.id.myRequestsTV);
//
//            mRef = FirebaseDatabase.getInstance().getReference().child("Online");
//            details = new ArrayList<>();
//
//            mRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                details.clear();
//                    for (DataSnapshot child : dataSnapshot.getChildren()){
//                        updatingClass c = child.getValue(updatingClass.class);
//                        if(c.getUid().equals(uid)) {
//                            details.add(c);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//            mRef = FirebaseDatabase.getInstance().getReference().child("Hand");
//            mRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                details.clear();
//                    for (DataSnapshot child : dataSnapshot.getChildren()){
//                        updatingClass c = child.getValue(updatingClass.class);
//                        if((c.getUid().equals(uid))) {
//                            details.add(c);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                }
//            });
//
////        if(details.isEmpty()){
////            recyclerView.setVisibility(View.GONE);
////            tv.setVisibility(View.VISIBLE);
////        }
////        else {
////            recyclerView.setVisibility(View.VISIBLE);
////            tv.setVisibility(View.GONE);
////        }
//            tv.setVisibility(View.GONE);
//            myOA = new MyOwnAdapter(this,details);
//            recyclerView.setAdapter(myOA);
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        runLayoutAnimation(recyclerView);
//        }

}
