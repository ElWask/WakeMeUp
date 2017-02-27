package com.example.schmid_charlesa_esig.wakemeup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charles on 17.02.2017.
 */

public class TodoAdapter extends ArrayAdapter{
    List list = new ArrayList<>();
    public TodoAdapter(Context context, int resource) {
        super(context, resource);
    }
    static class DataHandler{
        ImageView poster;
        TextView title;
        TextView desc;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DataHandler handler;

        if (convertView == null){
            LayoutInflater inflater =(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_layout,parent,false);
            handler = new DataHandler();
            handler.poster =(ImageView) row.findViewById(R.id.icon_item);
            handler.title =(TextView) row.findViewById(R.id.title_item);
            handler.desc =(TextView) row.findViewById(R.id.desc_item);
            row.setTag(handler);

        }
        else{
            handler = (DataHandler) row.getTag();
        }
        TodoDataProvider dataProvider;
        dataProvider = (TodoDataProvider) this.getItem(position);
        handler.poster.setImageResource(dataProvider.getPoster_resource());
        handler.title.setText(dataProvider.getItem_title());
        handler.desc.setText(dataProvider.getItem_desc());

        return row;
    }
}