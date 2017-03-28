package com.example.schmid_charlesa_esig.wakemeup;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
//todolist with databse
import com.example.schmid_charlesa_esig.wakemeup.bdd.Todo;
import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TodoListActivity extends AppCompatActivity {
    // for the calendar
    Calendar calendar = Calendar.getInstance();
    static String getTitleTask;
    static String getDescTask;
    static String getContactTask;
    static String getNumberContactTask;
    static String getYearTask;
    static String getMonthTask;
    static String getDayTask;
    static String getHourTask;
    static String getMinTask;

    //todolist with database
    private TodoHelper mHelper;
    private ListView mTodoListView;

    //to have different alarm
    static int REQUEST_CODE;
    private static final int RESULT_PICK_CONTACT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        //retour button
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // todolist with database
        mHelper = new TodoHelper(this);
        mTodoListView = (ListView) findViewById(R.id.list_todo);

        if (getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            final String doIAddTheTask =  bundle.getString("addTask");
            if (doIAddTheTask.equalsIgnoreCase("yes")){
                openTaskName();
            }
            updateUI();
        }

    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.todomenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_task:
                openTaskName();
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openTaskName() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleBox = new EditText(this);
        titleBox.setHint("Title");
        layout.addView(titleBox);

        final EditText descriptionBox = new EditText(this);
        descriptionBox.setHint("Description");
        layout.addView(descriptionBox);

        final Button buttonBox = new Button(this);
        buttonBox.setHint("Ajouter un numéro");
        layout.addView(buttonBox);
        buttonBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // contact
                pickContact();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nouvelle tâche")
                .setMessage("Ajout d'une nouvelle tâche : ")
                .setView(layout)
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getTitleTask = String.valueOf(titleBox.getText());
                        getDescTask = String.valueOf(descriptionBox.getText());
                        if (getContactTask != null){
                            getDescTask += "\n" + "avec " + getContactTask + " " + getNumberContactTask;
                        }
                        openTaskDate();
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();
        dialog.show();
    }

    private void openTaskDate() {
        new DatePickerDialog(this,listener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            getYearTask = String.valueOf(i);
            getMonthTask = String.valueOf(i1);
            getDayTask = String.valueOf(i2);
            openTaskTime();
        }
    };

    private void openTaskTime() {
        new TimePickerDialog(this,listenerTime,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }
    TimePickerDialog.OnTimeSetListener listenerTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            String taskHour = String.valueOf(i);
            getHourTask = taskHour;
            String taskMin = String.valueOf(i1);
            getMinTask = taskMin;

            SQLiteDatabase db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Todo.TodoEntry.COL_TASK_TITLE, getTitleTask);
            values.put(Todo.TodoEntry.COL_TASK_DESC, getDescTask);
            values.put(Todo.TodoEntry.COL_TASK_YEAR, getYearTask);
            values.put(Todo.TodoEntry.COL_TASK_MONTH, getMonthTask);
            values.put(Todo.TodoEntry.COL_TASK_DAY, getDayTask);
            values.put(Todo.TodoEntry.COL_TASK_HOUR, taskHour);
            values.put(Todo.TodoEntry.COL_TASK_MIN, taskMin);
            db.insertWithOnConflict(Todo.TodoEntry.TABLE, null,values,SQLiteDatabase.CONFLICT_REPLACE);
            db.close();

            updateUI();
            alarmIntent();
        }
    };

    public void pickContact()
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }

        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            getNumberContactTask = cursor.getString(phoneIndex);
            getContactTask = cursor.getString(nameIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alarmIntent() {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        cal.set(Calendar.DATE,Integer.valueOf(getDayTask));  //1-31
        cal.set(Calendar.MONTH,Integer.valueOf(getMonthTask));  //first month is 0!!! January is zero!!!
        cal.set(Calendar.YEAR,Integer.valueOf(getYearTask));//year...

        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(getHourTask));  //HOUR
        cal.set(Calendar.MINUTE, Integer.valueOf(getMinTask));       //MIN
        cal.set(Calendar.SECOND, 0);       //SEC always 0 cause we don't five a f

        //        Create an intent to the alarm receiver
        final Intent monIntent = new Intent(this, AlarmReceiver.class);
        //put extra string in monintent to say we press the set alarm
        monIntent.putExtra("extra","alarm on");
        monIntent.putExtra("taskNameTrans",TodoListActivity.getTitleTask);
        REQUEST_CODE = (int)cal.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TodoListActivity.this, REQUEST_CODE, monIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    public void updateUI() {
        List<TaskData> tasks = genererTasks();
        final TaskAdapter taskAdapter = new TaskAdapter(TodoListActivity.this, tasks);
        mTodoListView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
        mTodoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskData nameTask = (TaskData) mTodoListView.getItemAtPosition(i);
                String nameTrans = nameTask.getName();

                Intent intent = new Intent(TodoListActivity.this,DetailTask.class);
                intent.putExtra("TaskName",nameTrans);
                startActivity(intent);
            }
        });
        mTodoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int i, long l) {
                final AlertDialog.Builder b = new AlertDialog.Builder(TodoListActivity.this);
                b.setIcon(android.R.drawable.ic_dialog_alert);
                b.setMessage("Modifier ou supprimer cette tâche ?");
                b.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        System.out.println("Modifier");
                        TaskData nameTask = (TaskData) mTodoListView.getItemAtPosition(i);
                        String nameTrans = nameTask.getName();

                        Intent intent = new Intent(TodoListActivity.this,SetDetailTaskActivity.class);
                        intent.putExtra("TaskName",nameTrans);
                        startActivity(intent);
                    }
                });
                b.setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TaskData toRemove = taskAdapter.getItem(i);

                        taskAdapter.remove(toRemove);
                        deleteTask(toRemove.getName(),toRemove.getDesc());
                        taskAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Tâche supprimée de la liste", Toast.LENGTH_SHORT).show();
                    }
                });

                b.show();
                return true;
            }
        });
    }

    public List<TaskData> genererTasks() {

        List<TaskData> taskDataList;
        TodoHelper.init(TodoListActivity.this);
        taskDataList = TodoHelper.getAllUserData();

        return taskDataList;
    }

    public void deleteTask(String nom, String desc){

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Todo.TodoEntry.TABLE, Todo.TodoEntry.COL_TASK_TITLE + " = ? AND " + Todo.TodoEntry.COL_TASK_DESC + " = ?", new String[]{nom, desc});
        db.close();
        updateUI();
    }
}