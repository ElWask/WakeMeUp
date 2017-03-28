package com.example.schmid_charlesa_esig.wakemeup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;
import java.util.Calendar;
import java.util.List;

/**
 * Created by SCHMID_CHARLESA-ESIG on 30.01.2017.
 */
public class RingtonePlayingService extends Service {
    MediaPlayer mediaSong;
    boolean isRunning;
    int startID;
    int mId;
    String taskTodo;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("In the service", " Recieved start id" + startId + ": " + intent);

//        fetch the extra String values
        String state = intent.getExtras().getString("extra");
        String title = intent.getExtras().getString("titleNotif");
        Log.e("Ringtone state extra: ", state);


//        this convert the extra string to the intent to the value 0 or 1
        assert state!=null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("state ID is : ", state);
                break;
            default:
                startId = 0;
                break;
        }
//        if there is no music playing and we press set Alarm
        if (!this.isRunning && startId ==1 ) {
            if (title != null) {
                Log.e("There is no musc ", "and you want to start");

                mediaSong = MediaPlayer.create(this, R.raw.nyancat);
                mediaSong.start();

                this.isRunning = true;
                this.startID = 0;
            }

//        notification
            String taskName;
            Intent intentDetailTask;
            String stylePers = "";
            List<TaskData> taskDataList;
            taskDataList = TodoHelper.getAllUserDataWhenName(title);
            if (taskDataList != null) {
                if (title != null) {
                    if (!title.equals("Se réveiller")) {
                        taskName = String.valueOf(taskDataList.get(0).getName());
                        //        set up the intent that goes to the alarm activity
                        intentDetailTask = new Intent(this.getApplicationContext(), DetailTask.class);
                        intentDetailTask.putExtra("TaskName", title);
                        taskTodo = String.valueOf(taskDataList.get(0).getDesc());
                    } else {
                        taskName = "Se réveiller";
                        intentDetailTask = new Intent(this.getApplicationContext(), AlarmActivity.class);
                        Calendar calendar = Calendar.getInstance();
                        String year = String.valueOf(calendar.get(Calendar.YEAR));
                        String month = String.valueOf(calendar.get(Calendar.MONTH));
                        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                        taskDataList = TodoHelper.getAllUserDataWithDay(year, month, day);
                        //                Merci à Diego Ruiz Torres
                        taskTodo = "";
                        for (int i = 0; i < taskDataList.size(); i++) {
                            taskTodo = taskTodo + String.valueOf(taskDataList.get(i).getName()) + "\n";
                        }
                        stylePers = "Vous avez programmé la tache: \n"
                                + taskName + "\n"
                                + "Aujourdui vous devez : \n"
                                + taskTodo + "\n";
                    }
                    Log.d("app", "f : " + taskTodo);
//         Make the notification parameters
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.alarmicon)
                                    .setContentTitle(taskName)
                                    .setContentText(
                                            "Vous avez programmé la tache: " + taskName + "\n")
                                    .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission

                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(stylePers)
                                    )
                                    .setAutoCancel(true);// #0

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(intentDetailTask);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                    mNotificationManager.notify(mId, mBuilder.build());
                }
            }else{
                System.out.println("Supprimée");
            }
        }
//      if there is music playing and we press endalarm
        else if (this.isRunning && startId == 0 ){
            Log.e("There is musc ","and you want to end");

//            stop the ringtone
            mediaSong.stop();
            mediaSong.reset();

            this.isRunning = false;
            startID = 0;
        }
//      if the user press random buttons
//        if there is no music and we press end alam
        else if (!this.isRunning && startId == 0 ){
            Log.e("There is no musc ","and you want to start");
            this.isRunning = false;
            this.startID = 0;
        }
//        if there is music and we press set alarm
        else if (this.isRunning && startId == 1 ){
            Log.e("There is  musc ","and you want to start");
            System.out.println("music and press alarm");
            this.isRunning = true;
            this.startID = 1;
        }
//        anything else
        else{
            Log.e("dunno htf you came here","WTF?");
        }


        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        this.isRunning=false;
        Toast.makeText(this,"on Destroy called", Toast.LENGTH_SHORT).show();
    }
}
