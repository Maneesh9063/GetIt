package ultimate.com.getit.Tabs;

//TODO : Schimer effect to be implemented
//know nothing about it ...

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import ultimate.com.getit.Purpose;
import ultimate.com.getit.R;
import ultimate.com.getit.updatingClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class Online extends Fragment {
    ArrayList<updatingClass> details;
    RecyclerView rv;
    MyOwnAdapter adapter;
    String uid = FirebaseAuth.getInstance().getUid();
    TextView tv;
    public Online() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_online, container, false);

        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefreshOnline);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch(); // your code
                runLayoutAnimation(rv);
                pullToRefresh.setRefreshing(false);
            }
        });

        details = new ArrayList<>();
        rv = (RecyclerView) rootView.findViewById(R.id.onlineList);
        tv = (TextView) rootView.findViewById(R.id.onlineTV);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        if(!Purpose.checkInternet(getActivity())){
            Toast.makeText(getContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);

        return rootView;
    }

    public void onStart() {
        super.onStart();
        fetch();
        runLayoutAnimation(rv);

    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
//        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void fetch(){
        DatabaseReference mRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Hand");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                details.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    updatingClass c = child.getValue(updatingClass.class);
//                    if(c.getUid()!=uid) {
                    details.add(c);
//                    }
                    adapter = new MyOwnAdapter(getContext(),details);
                    rv.setAdapter(adapter);
//                    runLayoutAnimation(rv);

                }

                if(details.isEmpty()){
                    rv.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                }
                else {
                    rv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
