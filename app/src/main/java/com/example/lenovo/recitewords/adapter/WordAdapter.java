package com.example.lenovo.recitewords.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.recitewords.Data.WordList;
import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.Data.WordList;

import java.util.List;

public class WordAdapter extends ArrayAdapter {

    private final int resourceId;

    public WordAdapter(@NonNull Context context, int resource, List<WordList> objects) {
        super(context, resource,objects);
        this.resourceId = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final WordList list1 = (WordList) getItem(position);
        final View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView list_name = view.findViewById(R.id.list_name);
        TextView list_number = view.findViewById(R.id.list_number);
        TextView list_from = view.findViewById(R.id.list_from);
        ImageView imageView = view.findViewById(R.id.imageView4);
        list_name.setText(list1.getName());
        list_number.setText(list1.getNumber());
        list_from.setText(list1.getFrom());
        if(position==0){
            imageView.setImageResource(R.drawable.cet41);
        }
        if(position==1){
            imageView.setImageResource(R.drawable.cet6);
        }
        if(position==2){
            imageView.setImageResource(R.drawable.cet8);
        }

        return view;
    }
}
