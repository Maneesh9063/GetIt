package ultimate.com.getit.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ultimate.com.getit.R;
import ultimate.com.getit.updatingClass;

public class arrayAdapter extends ArrayAdapter<updatingClass> {

    private Context context;
    private List<updatingClass> updatingClassList;

    public arrayAdapter(Context context,List<updatingClass> updatingClassList){
        super(context,R.layout.m_row, updatingClassList);
        this.updatingClassList = updatingClassList;
        this.context = context;
    }
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        updatingClass card_item = updatingClassList.get(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.m_row, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.nameOnCard);
        TextView phone = (TextView) convertView.findViewById(R.id.phoneOnCard);
        Button amount = (Button) convertView.findViewById(R.id.amountOnCard);
        name.setText(card_item.getName());
        phone.setText(card_item.getPhone());
        amount.setText(card_item.getAmount());

        return convertView;

    }
}