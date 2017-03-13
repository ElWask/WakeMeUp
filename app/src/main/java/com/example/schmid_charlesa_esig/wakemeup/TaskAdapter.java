package com.example.schmid_charlesa_esig.wakemeup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SCHMID_CHARLESA-ESIG on 13.03.2017.
 */

public class TaskAdapter extends ArrayAdapter<TaskData> {

    //tweets est la liste des models à afficher
    public TaskAdapter(Context context, List<TaskData> task) {
        super(context, 0, task);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo,parent, false);
        }

        TaskViewHolder viewHolder = (TaskViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TaskViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.todoTitle);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.todoDesc);
            convertView.setTag(viewHolder);
        }
        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        System.out.println(
                getItem(position)
        );
        TaskData task = getItem(position);


        //il ne reste plus qu'à remplir notre vue
        viewHolder.title.setText(task.getName());
        viewHolder.desc.setText(task.getDesc());

        return convertView;
    }

    private class TaskViewHolder {
        public TextView title;
        public TextView desc;
    }
}
