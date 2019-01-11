package ultimate.com.getit.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ultimate.com.getit.FAB.HandCashRegister;
import ultimate.com.getit.Login.LoginPage;
import ultimate.com.getit.MainActivity;
import ultimate.com.getit.Purpose;
import ultimate.com.getit.R;
import ultimate.com.getit.updatingClass;

public class MyOwnAdapter extends RecyclerView.Adapter<MyOwnAdapter.MHolder> {

    Context ct;
    ArrayList<updatingClass> data;

    public class MHolder extends RecyclerView.ViewHolder {
        TextView nameR, phoneR,amountR,timeR,distanceR;
        ImageView iconR;

        public MHolder(@NonNull View itemView) {
            super(itemView);
            nameR = (TextView) itemView.findViewById(R.id.nameOnCard);
            phoneR = (TextView) itemView.findViewById(R.id.phoneOnCard);
            amountR = (TextView)itemView.findViewById(R.id.amountOnCard);
            timeR = (TextView)itemView.findViewById(R.id.timeAgo);
            distanceR = (TextView)itemView.findViewById(R.id.distance);
            iconR = (ImageView)itemView.findViewById(R.id.iconOnCard);
        }
    }

    public MyOwnAdapter(Context ctx , ArrayList<updatingClass> info){
        ct=ctx;
        data = info;
        if(data.isEmpty()){

        }
    }

    @NonNull
    @Override
    public MyOwnAdapter.MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater myInflator = LayoutInflater.from(parent.getContext());
        View myView = myInflator.inflate(R.layout.m_row,parent,false);
        return new MHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {
        updatingClass obj = data.get(position);
        String time = Purpose.getTimeAgo(obj.getTimeOfRegistration());
        String dis = Purpose.distance(obj.getLatitude(),obj.getLongitude(),MainActivity.latitude,MainActivity.longitude);
            holder.nameR.setText("Name : " + obj.getName());
            holder.phoneR.setText("Phone : " + obj.getPhone());
            holder.amountR.setText("Amount : " + obj.getAmount());
            holder.timeR.setText(time);
            holder.distanceR.setText((dis));

            if (obj.getHandCashRequest()) {
                holder.iconR.setImageResource(R.drawable.hand_cash_icon);
            } else
                holder.iconR.setImageResource(R.drawable.digital_cash_icon);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
