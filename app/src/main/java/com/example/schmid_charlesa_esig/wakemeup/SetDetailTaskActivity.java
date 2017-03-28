package com.example.schmid_charlesa_esig.wakemeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;

import java.util.List;

public class SetDetailTaskActivity extends AppCompatActivity {
    EditText nameTask,descTask,yearTask,monthTask,dayTask,hourTask,minuteTask;
    Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_detail_task);
//      get the name trans
        Bundle bundle = getIntent().getExtras();
        final String nameTransfered =  bundle.getString("TaskName");

        System.out.println(bundle+"/" + nameTransfered);
//        get the form from layout
        nameTask = (EditText)findViewById(R.id.setnameTask);
        descTask = (EditText)findViewById(R.id.setdescTask);
        yearTask = (EditText)findViewById(R.id.setyearTask);
        monthTask = (EditText)findViewById(R.id.setmonthTask);
        dayTask = (EditText)findViewById(R.id.setdayTask);
        hourTask = (EditText)findViewById(R.id.sethourTask);
        minuteTask = (EditText)findViewById(R.id.setminuteTask);
        edit = (Button)findViewById(R.id.btnDetailTask);
//      list
        List<TaskData> taskDataList;
        taskDataList = TodoHelper.getAllUserDataWhenName(nameTransfered);
        int rightMonth =  taskDataList.get(0).getMonth() + 1;

        nameTask.setText(taskDataList.get(0).getName());
        descTask.setText(taskDataList.get(0).getDesc());
        yearTask.setText(String.valueOf(taskDataList.get(0).getYear()));
        monthTask.setText(String.valueOf(taskDataList.get(0).getMonth()));
        dayTask.setText(String.valueOf(taskDataList.get(0).getDay()));
        hourTask.setText(String.valueOf(taskDataList.get(0).getHour()));
        minuteTask.setText(String.valueOf(taskDataList.get(0).getMinute()));
//edit the task
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldName = nameTransfered;
                //update bdd

                TodoHelper.setAllUserDataWithName(oldName,nameTask.getText().toString(),
                        descTask.getText().toString(),yearTask.getText().toString(),
                        monthTask.getText().toString(),dayTask.getText().toString(),
                        hourTask.getText().toString(),minuteTask.getText().toString());
                Intent intent = new Intent(SetDetailTaskActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
