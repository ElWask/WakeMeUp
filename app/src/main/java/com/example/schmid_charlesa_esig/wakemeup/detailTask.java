package com.example.schmid_charlesa_esig.wakemeup;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;

import java.util.List;

public class DetailTask extends AppCompatActivity {
    TextView name, desc, date, hour;
    Button stopAlarm;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    int getRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        name = (TextView) findViewById(R.id.nameTask);
        desc = (TextView) findViewById(R.id.descTask);
        date = (TextView) findViewById(R.id.dateTask);
        hour = (TextView) findViewById(R.id.hourTask);
        stopAlarm = (Button) findViewById(R.id.btnDetailTask);

        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("TaskName");

        List<TaskData> taskDataList;

        taskDataList = TodoHelper.getAllUserDataWhenName(title);

        int rightMonth = taskDataList.get(0).getMonth() + 1;
        String dateComplete = taskDataList.get(0).getDay() + "/" + rightMonth + "/" + taskDataList.get(0).getYear();
        String hourComplete;
        if (taskDataList.get(0).getMinute() < 10) {
            hourComplete = String.valueOf(taskDataList.get(0).getHour()) + ":0" + taskDataList.get(0).getMinute();
        } else {
            hourComplete = String.valueOf(taskDataList.get(0).getHour()) + ":" + taskDataList.get(0).getMinute();
        }


        name.setText(taskDataList.get(0).getName());
        desc.setText(taskDataList.get(0).getDesc());
        date.setText(dateComplete);
        hour.setText(hourComplete);

        //        Create a n intent to the alarm receiver
        final Intent monIntent = new Intent(this, AlarmReceiver.class);
        //        Initalise alarm mana
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        getRequest = TodoListActivity.REQUEST_CODE;
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //        Create a pending intent that delays the intent until the time asked
                pendingIntent = PendingIntent.getBroadcast(DetailTask.this, getRequest, monIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//                updateText("Alarm disabled");
                alarmManager.cancel(pendingIntent);
//                say we press the end alarm
                monIntent.putExtra("extra", "alarm off");
//                stop the ringtone
                sendBroadcast(monIntent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id == android.R.id.home){finish();}
        return super.onOptionsItemSelected(item);
    }
}
