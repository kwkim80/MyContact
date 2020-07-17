package ca.algonquin.kw2446.contactsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private List<Contact> list;
//    ItemClicked activity;
//
//    public interface  ItemClicked{
//        void onItemClicked(int i);
//    }
    public ContactsAdapter(Context context, List<Contact> list){
        super(context,R.layout.list_item, list);
        this.context=context;
        this.list=list;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.list_item,parent,false);

        TextView tvChar=convertView.findViewById(R.id.tvChar);
        TextView tvEmail=convertView.findViewById(R.id.tvEmail);
        TextView tvName=convertView.findViewById(R.id.tvName);

        tvChar.setText(list.get(position).getName().charAt(0)+"");
        tvEmail.setText(list.get(position).getEmail());
        tvName.setText(list.get(position).getName());

//        convertView.setOnClickListener((v)->{
//            activity.onItemClicked(position);
//        });
        return convertView;
    }
}

