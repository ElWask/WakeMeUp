package com.example.schmid_charlesa_esig.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayTask extends AppCompatActivity {

    //todolist with databse

    private TodoHelper mHelper;
    private ListView mTodoListView;
     Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // todolist with database
        mHelper = new TodoHelper(this);
        mTodoListView = (ListView) findViewById(R.id.list_todo);
        List<TaskData> tasks = genererTasks();
        TaskAdapter taskAdapter = new TaskAdapter(DayTask.this, tasks);
        mTodoListView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
        mTodoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("This should show when press on one button and should be the item at position"+i);
                Toast.makeText(DayTask.this, "Hello world", Toast.LENGTH_LONG).show();
            }
        });
    }

    public List<TaskData> genererTasks() {

        List<TaskData> taskDataList;
        TodoHelper.init(DayTask.this);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        taskDataList = TodoHelper.getAllUserDataWithDay(year,month,day);

        return taskDataList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id == android.R.id.home){finish();}
        return super.onOptionsItemSelected(item);
    }
}