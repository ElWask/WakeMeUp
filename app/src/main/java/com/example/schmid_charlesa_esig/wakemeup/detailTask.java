package com.example.schmid_charlesa_esig.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;

import java.util.List;

public class DetailTask extends AppCompatActivity {
    TextView name,desc,date,hour;
    Button quit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);


        name = (TextView)findViewById(R.id.nameTask);
        desc = (TextView)findViewById(R.id.descTask);
        date = (TextView)findViewById(R.id.dateTask);
        hour = (TextView)findViewById(R.id.hourTask);

        List<TaskData> taskDataList;
        taskDataList = TodoHelper.getAllUserDataWhenTime(TodoListActivity.getYearTask,TodoListActivity.getMonthTask,TodoListActivity.getHourTask,TodoListActivity.getMinTask);
        int rightMonth =  taskDataList.get(0).getMonth() + 1;
        String dateComplete = taskDataList.get(0).getDay() + "/" + rightMonth + "/" + taskDataList.get(0).getYear();
        String hourComplete = String.valueOf(taskDataList.get(0).getHour())+":"+taskDataList.get(0).getMinute();

        name.setText(taskDataList.get(0).getName());
        desc.setText(taskDataList.get(0).getDesc());
        date.setText(dateComplete);
        hour.setText(hourComplete);
    }
}
