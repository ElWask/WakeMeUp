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

    //task est la liste des models à afficher
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
            viewHolder.date = (TextView) convertView.findViewById(R.id.todoDate);
            viewHolder.hour = (TextView) convertView.findViewById(R.id.todoHour);
            convertView.setTag(viewHolder);
        }
        //getItem(position) va récupérer l'item [position] de la List<Task> tasks
        System.out.println(
                getItem(position)
        );
        TaskData task = getItem(position);

        //puis remplir notre vue
        viewHolder.title.setText(task.getName());
        viewHolder.desc.setText(task.getDesc());
        viewHolder.date.setText(String.valueOf(task.getDay()) + "/" + String.valueOf(task.getMonth()) + "/" + String.valueOf(task.getYear()));

        if (task.getMinute() < 10) {
            viewHolder.hour.setText(String.valueOf(task.getHour()) + ":0" + String.valueOf(task.getMinute()));
        }else {
            viewHolder.hour.setText(String.valueOf(task.getHour()) + ":" + String.valueOf(task.getMinute()));
        }


        return convertView;
    }

    private class TaskViewHolder {
        public TextView title;
        public TextView desc;
        public TextView date;
        public TextView hour;
    }
}
