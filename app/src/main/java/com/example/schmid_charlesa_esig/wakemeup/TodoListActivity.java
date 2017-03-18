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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
//todolist with databse
import com.example.schmid_charlesa_esig.wakemeup.bdd.Todo;
import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TodoListActivity extends AppCompatActivity {
    // for the calendar
    Calendar calendar = Calendar.getInstance();
    String getTitleTask;
    String getDescTask;
    static String getYearTask;
    static String getMonthTask;
    String getDayTask;
    static String getHourTask;
    static String getMinTask;
    //todolist with databse

    public static final String TAG = "TodoListActivity";
    private TodoHelper mHelper;
    private ListView mTodoListView;

    private ArrayAdapter<String> mAdapterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        // todolist with database
        mHelper = new TodoHelper(this);
        mTodoListView = (ListView) findViewById(R.id.list_todo);
        mTodoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("You press" + view+" and " + i);
            }
        });
        openTaskName();
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

    private void openTaskName() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleBox = new EditText(this);
        titleBox.setHint("Title");
        layout.addView(titleBox);

        final EditText descriptionBox = new EditText(this);
        descriptionBox.setHint("Description");
        layout.addView(descriptionBox);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New Task")
                .setMessage("Add a new task : ")
                .setView(layout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String taskTitle = String.valueOf(titleBox.getText());
                        getTitleTask = taskTitle;
                        String taskDesc = String.valueOf(descriptionBox.getText());
                        getDescTask = taskDesc;
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
            String taskYear = String.valueOf(i);
            getYearTask = taskYear;
            String taskMonth = String.valueOf(i1);
            getMonthTask = taskMonth;
            String taskDay = String.valueOf(i2);
            getDayTask = taskDay;
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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TodoListActivity.this, 0, monIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    public void updateUI() {
        ArrayList<String> todoListName = new ArrayList<>();


        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursorName = db.query(Todo.TodoEntry.TABLE, new String[]{
                Todo.TodoEntry.COL_TASK_TITLE}, null, null, null, null, null);

        while (cursorName.moveToNext()) {
            int index = cursorName.getColumnIndex(Todo.TodoEntry.COL_TASK_TITLE);
            todoListName.add(cursorName.getString(index));
        }
        if (mAdapterName == null) {
            List<TaskData> tasks = genererTasks();
            TaskAdapter taskAdapter = new TaskAdapter(TodoListActivity.this, tasks);

            mTodoListView.setAdapter(taskAdapter);
            taskAdapter.notifyDataSetChanged();
        } else {
            mAdapterName.clear();
            mAdapterName.addAll(todoListName);
            mAdapterName.notifyDataSetChanged();
        }
        cursorName.close();
        db.close();
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