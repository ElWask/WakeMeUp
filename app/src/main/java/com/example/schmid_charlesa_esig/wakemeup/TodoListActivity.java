package com.example.schmid_charlesa_esig.wakemeup;

import android.app.Activity;
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
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
//todolist with databse
import com.example.schmid_charlesa_esig.wakemeup.bdd.Todo;
import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TodoListActivity extends AppCompatActivity {
    // for the calendar
    Calendar calendar = Calendar.getInstance();
    static String getTitleTask;
    static String getDescTask;
    static String getContactTask;
    static String getYearTask;
    static String getMonthTask;
    static String getDayTask;
    static String getHourTask;
    static String getMinTask;
    //todolist with databse

    private TodoHelper mHelper;
    private ListView mTodoListView;

    //to have different alarm
    static int REQUEST_CODE;
    final int PICK_CONTACT=1;
    //to add a contact
    Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        // todolist with database
        mHelper = new TodoHelper(this);
        mTodoListView = (ListView) findViewById(R.id.list_todo);

        Bundle bundle = getIntent().getExtras();
        final String doIAddTheTask =  bundle.getString("addTask");
        if (doIAddTheTask.equalsIgnoreCase("yes")){
            openTaskName();
        }
        updateUI();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected void getContactInfo(Intent intent)
    {

        Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);
        while (cursor.moveToNext())
        {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            getContactTask = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            System.out.println(getContactTask);
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if ( hasPhone.equalsIgnoreCase("1"))
                hasPhone = "true";
            else
                hasPhone = "false" ;

            if (Boolean.parseBoolean(hasPhone))
            {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                while (phones.moveToNext())
                {
                    String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    System.out.println(phone);
                }
                phones.close();
            }
        }  //while (cursor.moveToNext())
        cursor.close();
    }//getContactInfo
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {

        if (requestCode == PICK_CONTACT)
        {
            getContactInfo(intent);
            // Your class variables now have the data, so do something with it.
        }
    }//onActivityResult

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
                System.out.println("LOL");
                // contact
                startActivityForResult(intentContact, PICK_CONTACT);
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
        TaskAdapter taskAdapter = new TaskAdapter(TodoListActivity.this, tasks);
        mTodoListView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
        mTodoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskData nameTask = (TaskData) mTodoListView.getItemAtPosition(i);
                String nameTrans = nameTask.getName();

                Intent intent = new Intent(TodoListActivity.this,SetDetailTaskActivity.class);
                intent.putExtra("TaskName",nameTrans);
                startActivity(intent);
            }
        });
    }

    public List<TaskData> genererTasks() {

        List<TaskData> taskDataList;
        TodoHelper.init(TodoListActivity.this);
        taskDataList = TodoHelper.getAllUserData();

        return taskDataList;
    }

    public void deleteTask(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.todoTitle);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Todo.TodoEntry.TABLE, Todo.TodoEntry.COL_TASK_TITLE + " = ?", new String[]{task});
        db.close();
        updateUI();
    }
}