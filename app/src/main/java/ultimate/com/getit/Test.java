package ultimate.com.getit;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ultimate.com.getit.Adapters.arrayAdapter;

public class Test extends AppCompatActivity {

//    String uid;
//    TextView t2,t3;
    ListView t;
    List<updatingClass> cl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        t = (ListView) findViewById(R.id.lv);
        cl = new ArrayList<>();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user!=null){
//            uid = user.getUid();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
            DatabaseReference mRef = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Online");
//                    .child(uid);

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    cl.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        updatingClass c = child.getValue(updatingClass.class);

                        cl.add(c);
                    }
                    arrayAdapter adapter = new arrayAdapter(Test.this,cl);
                    t.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    public void doIt(View view) {
        DatabaseReference mRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Hand");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cl.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    updatingClass c = child.getValue(updatingClass.class);

                    cl.add(c);
                }

                arrayAdapter adapter = new arrayAdapter(Test.this,cl);
                t.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    public class Data{
//
//
//        HashMap<String,String>  map;
//        HashMap<String,HashMap<String,String>> map1;
//        ArrayList<ArrayList<String>> arrayListObj;
//
//        Set s;
//        Iterator iterator;
//        Map.Entry<String,String> m;
////        Data t;
//        ArrayList<String> arrayList;
//        public Data(){
//
//        }
//        public Data(String uid,Data obj,DataSnapshot dataSnapshot){
//            ArrayList<ArrayList<String>> als = new ArrayList<>();
//            ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>> listMain =(ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>>)dataSnapshot.getValue();
//
//            String[][] m =new String[listMain.size()][3] ;
////            obj.map = obj.listMain.get(uid);
//            for (int i=0;i<listMain.size();i++){
//                for(int j=0;j<3;j++){
////                    obj.map1 = obj.listMain.get(i);
//                    s = map1.entrySet();
//                    iterator = s.iterator();
//                    while (iterator.hasNext()){
////                        Map.Entry mentry = (Map.Entry)iterator.next();
//                        als.add(1,)
//                    }
////                    obj.map = obj.map1.;
//                    map.entrySet();
//
//
//                }
//
//            }
//
//        }
//
//    }
}
