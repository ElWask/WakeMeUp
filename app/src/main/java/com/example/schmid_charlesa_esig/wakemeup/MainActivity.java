package com.example.schmid_charlesa_esig.wakemeup;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schmid_charlesa_esig.wakemeup.bdd.Todo;
import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;

import static com.example.schmid_charlesa_esig.wakemeup.TodoListActivity.getDescTask;
import static com.example.schmid_charlesa_esig.wakemeup.TodoListActivity.getTitleTask;


public class MainActivity extends AppCompatActivity {
    Button gotoAlarm;
    ImageView logoImg;
    //todolist with databse

    private TodoHelper mHelper;
    private ListView mTodoListView;
    Calendar calendar = Calendar.getInstance();
    public void gotoAddTask(View view){

        Intent i = new Intent(this,TodoListActivity.class);
        i.putExtra("addTask","yes");
        startActivity(i);
    }
    public void gotoTask(View view){

        Intent i = new Intent(this,TodoListActivity.class);
        i.putExtra("addTask","no");
        startActivity(i);
    }
    public void gotoTaskOfTheDay(View view){

        Intent i = new Intent(this,DayTask.class);
        startActivity(i);
    }
    public void gotoAlarm(View view){

        Intent i = new Intent(this,AlarmActivity.class);
        startActivity(i);
    }
    public void gotoRobot(View view){
                Intent i = new Intent(getApplicationContext(),TalkToMe.class);
                startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gotoAlarm=(Button)findViewById(R.id.AlarmPageButton);
        logoImg = (ImageView)findViewById(R.id.logoImg);

        // todolist with database
        mHelper = new TodoHelper(this);
        mTodoListView = (ListView) findViewById(R.id.listviewMainDay);
        updateUI();


    }

    @Override
    protected void onResume() {
        System.out.println("I'm background ");
        updateUI();
        super.onResume();
    }

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
                        /*PROBLEME*/
                        View vue =  mTodoListView.findViewById(i) ;
                        System.out.println("Supprimer" + vue);
                        //deleteTask(vue) ;
                        /*FIN PROBLEME*/
                        taskAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Removed from list", Toast.LENGTH_SHORT).show();

                    }
                });

                b.show();
                return true;
            }
        });
    }
    public void deleteTask(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.todoTitle);
        TextView taskDescTextView = (TextView) parent.findViewById(R.id.todoDesc);
        String task = String.valueOf(taskTextView.getText());
        String taskDesc = String.valueOf(taskDescTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Todo.TodoEntry.TABLE, Todo.TodoEntry.COL_TASK_TITLE + " = ? AND " + Todo.TodoEntry.COL_TASK_DESC + " = ?", new String[]{task,taskDesc});
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