package com.example.schmid_charlesa_esig.wakemeup;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import java.util.Calendar;
import java.util.Locale;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    TimePicker timePicker;
    TextView updateText;
    Context context;
    PendingIntent pendingIntent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menualarm,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.parameters:
                startActivity(new Intent(this,MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        this.context = this;

//        Initalise alarm mana
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        initialise our timepicker
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
//        initialise our text to update
        updateText = (TextView) findViewById(R.id.updateText);
//        Create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();
//        initialise buttons
        Button setAlarm = (Button) findViewById(R.id.setAlarm);
        Button stopAlarm = (Button) findViewById(R.id.endAlarm);

//        Create an intent to the alarm receiver
        final Intent monIntent = new Intent(this.context, AlarmReceiver.class);

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();


                String hourS = String.valueOf(hour);
                String minuteS = String.valueOf(minute);

                if (minute < 10) {
                    minuteS = "0" + String.valueOf(minute);
                }
//
                updateText("Alarm set to : " + hourS + ":" + minuteS);

//                put extra string in monintent to say we press the set alarm
                monIntent.putExtra("extra","alarm on");
//        Create a pending intent that delays the intent until the time asked
                pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, monIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        set the alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP ,calendar.getTimeInMillis(), pendingIntent);
//        method that change the update textbox
//        updateText("Alarm activate");
            }

        });
        stopAlarm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //        Create a pending intent that delays the intent until the time asked
                pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, monIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                updateText("Alarm disabled");
                alarmManager.cancel(pendingIntent);
//                say we press the end alarm
                monIntent.putExtra("extra","alarm off");
//                stop the ringtone
                sendBroadcast(monIntent);
            }
        });
    }
    private void updateText(String s) {
        updateText.setText(s);
    }
}