package com.example.schmid_charlesa_esig.wakemeup;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.schmid_charlesa_esig.wakemeup.bdd.Todo;
import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;


public class MainActivity extends AppCompatActivity {
    Button gotoAlarm;
    ImageView logoImg;
    int secret;
    //todolist with databse
    private TodoHelper mHelper;
    private ListView mTodoListView;
    Calendar calendar = Calendar.getInstance();
//    Add a task button with see all task
    public void gotoAddTask(View view){

        Intent i = new Intent(this,TodoListActivity.class);
        i.putExtra("addTask","yes");
        startActivity(i);
    }
//    See all task
    public void gotoTask(View view){

        Intent i = new Intent(this,TodoListActivity.class);
        i.putExtra("addTask","no");
        startActivity(i);
    }
//    Add an alarm
    public void gotoAlarm(View view){

        Intent i = new Intent(this,AlarmActivity.class);
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotoAlarm=(Button)findViewById(R.id.AlarmPageButton);
        logoImg = (ImageView)findViewById(R.id.logoImg);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Voici vos tâches du jour");
           // ab.setSubtitle("Voici vos tâches du jour");
        }
        // todolist with database
        mHelper = new TodoHelper(this);
        mTodoListView = (ListView) findViewById(R.id.listviewMainDay);
    }

    @Override
    protected void onResume() {
        System.out.println("I'm background ");
        updateUI();
        super.onResume();
    }
// genere les taches selon le jour
    public List<TaskData> genererTasks() {

        List<TaskData> taskDataList;
        TodoHelper.init(MainActivity.this);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        taskDataList = TodoHelper.getAllUserDataWithDay(year,month,day);

        return taskDataList;
    }
    public void updateUI() {
        List<TaskData> tasks = genererTasks();
        final TaskAdapter taskAdapter = new TaskAdapter(MainActivity.this, tasks);
        mTodoListView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
        mTodoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskData nameTask = (TaskData) mTodoListView.getItemAtPosition(i);
                String nameTrans = nameTask.getName();

                Intent intent = new Intent(MainActivity.this,DetailTask.class);
                intent.putExtra("TaskName",nameTrans);
                startActivity(intent);
            }
        });
        mTodoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int i, long l) {
                final AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setIcon(android.R.drawable.ic_dialog_alert);
                b.setMessage("Modifier ou supprimer cette tâche ?");
                b.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        System.out.println("Modifier");
                        TaskData nameTask = (TaskData) mTodoListView.getItemAtPosition(i);
                        String nameTrans = nameTask.getName();

                        Intent intent = new Intent(MainActivity.this,SetDetailTaskActivity.class);
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
        Comparator<TaskData> comparer = new Comparator<TaskData>() {
            public int compare(TaskData o1, TaskData o2) {
                    int heure1 = o1.getHour();
                    int heure2 = o2.getHour();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        int order = Integer.compare(heure1,heure2);
                        if (order != 0){
                            return order;
                        }else{
                            int min1 = o1.getMinute();
                            int min2 = o2.getMinute();
                            return Integer.compare(min1,min2);
                        }
                    }else return heure1-heure2;
            }
        };
        Collections.sort(tasks,comparer);
        taskAdapter.notifyDataSetChanged();
    }
    public void deleteTask(String nom, String desc){

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Todo.TodoEntry.TABLE, Todo.TodoEntry.COL_TASK_TITLE + " = ? AND " + Todo.TodoEntry.COL_TASK_DESC + " = ?", new String[]{nom, desc});
        db.close();
        updateUI();
    }
    public void openChrome(){
        String url = "https://github.com/ElWask/WakeMeUp";
        try {
            Intent i = new Intent("android.intent.action.MAIN");
            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
            i.addCategory("android.intent.category.LAUNCHER");
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        catch(ActivityNotFoundException e) {
            // Chrome is not installed
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }
    }

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
                openChrome();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}