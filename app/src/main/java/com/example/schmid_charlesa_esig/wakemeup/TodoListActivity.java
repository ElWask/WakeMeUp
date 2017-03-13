package com.example.schmid_charlesa_esig.wakemeup;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
//todolist with databse
import com.example.schmid_charlesa_esig.wakemeup.bdd.Todo;
import com.example.schmid_charlesa_esig.wakemeup.bdd.TodoHelper;
import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

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

        // Open alertDialog when open
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
                        String taskDesc = String.valueOf(descriptionBox.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Todo.TodoEntry.COL_TASK_TITLE, taskTitle);
                        values.put(Todo.TodoEntry.COL_TASK_DESC, taskDesc);
                        values.put(Todo.TodoEntry.COL_TASK_DATE, 0);
                        values.put(Todo.TodoEntry.COL_TASK_HOUR, 0);
                        values.put(Todo.TodoEntry.COL_TASK_MIN, 0);

                        db.insertWithOnConflict(Todo.TodoEntry.TABLE, null,values,SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        updateUI();
                    }
                })
                .setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        updateUI();
                    }
                })
                .create();
        dialog.show();
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
                                String taskDesc = String.valueOf(descriptionBox.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(Todo.TodoEntry.COL_TASK_TITLE, taskTitle);
                                values.put(Todo.TodoEntry.COL_TASK_DESC, taskDesc);
                                values.put(Todo.TodoEntry.COL_TASK_DATE, "0");
                                values.put(Todo.TodoEntry.COL_TASK_HOUR, 0);
                                values.put(Todo.TodoEntry.COL_TASK_MIN, 0);
                                db.insertWithOnConflict(Todo.TodoEntry.TABLE, null,values,SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void updateUI(){
        ArrayList<String> todoListName = new ArrayList<>();


        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursorName = db.query(Todo.TodoEntry.TABLE, new String[]{
                Todo.TodoEntry.COL_TASK_TITLE},null,null,null,null,null);

        while (cursorName.moveToNext()){
            int index = cursorName.getColumnIndex(Todo.TodoEntry.COL_TASK_TITLE);
            todoListName.add(cursorName.getString(index));
        }
        if (mAdapterName == null){
            List<TaskData> tasks = genererTasks();
            TaskAdapter taskAdapter = new TaskAdapter(TodoListActivity.this, tasks);
            /*http://androidexample.com/SQLite_Database_Manipulation_Class_-_Android_Example/index.php?view=article_discription&aid=51&aaid=76*/
           // mAdapterName = new ArrayAdapter<String>(this, R.layout.item_todo, R.id.todoTitle);
            mTodoListView.setAdapter(taskAdapter);
            taskAdapter.notifyDataSetChanged();
        }else {
            mAdapterName.clear();
            mAdapterName.addAll(todoListName);
            mAdapterName.notifyDataSetChanged();
        }
        cursorName.close();
        db.close();



    }

    public List<TaskData> genererTasks() {
       // A CONTINUER SUR http://stackoverflow.com/questions/10111166/get-all-rows-from-sqlite

        List<TaskData> taskDataList = new ArrayList<TaskData>();

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
