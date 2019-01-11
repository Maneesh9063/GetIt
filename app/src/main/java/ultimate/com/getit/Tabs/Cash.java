package ultimate.com.getit.Tabs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ultimate.com.getit.Adapters.MyOwnAdapter;
import ultimate.com.getit.Adapters.arrayAdapter;
import ultimate.com.getit.Purpose;
import ultimate.com.getit.R;
import ultimate.com.getit.updatingClass;
/**
 * A simple {@link Fragment} subclass.
 */
public class Cash extends Fragment {
    ArrayList<updatingClass> details;
    String uid = FirebaseAuth.getInstance().getUid();
    MyOwnAdapter myOA;
    RecyclerView recyclerView;
    TextView tv;
    public Cash() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_cash, container, false);
        details = new ArrayList<>();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerVeiw);
        tv = (TextView)rootView.findViewById(R.id.cashTV);
        if(!Purpose.checkInternet(getActivity())){
            Toast.makeText(getContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference mRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Online");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                details.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    updatingClass c = child.getValue(updatingClass.class);
//                    if(c.getUid()!=uid) {
                        details.add(c);
                    runLayoutAnimation(recyclerView);

//                    }
                }
                if(details.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                }
                else {

                    recyclerView.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);
                }
                myOA = new MyOwnAdapter(getContext(),details);
                recyclerView.setAdapter(myOA);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
//        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}
