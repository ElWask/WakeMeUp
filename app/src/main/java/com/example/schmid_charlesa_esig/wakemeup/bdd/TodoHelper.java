package com.example.schmid_charlesa_esig.wakemeup.bdd;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.schmid_charlesa_esig.wakemeup.TaskData;

import java.util.ArrayList;
import java.util.List;

import static com.example.schmid_charlesa_esig.wakemeup.BuildConfig.DEBUG;

/**
 * Created by Charly on 10.03.2017.
 */

public class TodoHelper extends SQLiteOpenHelper{
    public TodoHelper(Context context) {
        super(context, Todo.DB_NAME, null, Todo.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + Todo.TodoEntry.TABLE + "("+
                Todo.TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Todo.TodoEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
                Todo.TodoEntry.COL_TASK_DESC + " TEXT NOT NULL, " +
                Todo.TodoEntry.COL_TASK_YEAR + " TEXT NOT NULL, " +
                Todo.TodoEntry.COL_TASK_MONTH + " TEXT NOT NULL, " +
                Todo.TodoEntry.COL_TASK_DAY + " TEXT NOT NULL, " +
                Todo.TodoEntry.COL_TASK_HOUR + " TEXT NOT NULL, " +
                Todo.TodoEntry.COL_TASK_MIN + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Todo.TodoEntry.TABLE);
        onCreate(sqLiteDatabase);
    }

        /********* Used to open database in syncronized way *********/
    private static TodoHelper todoHelper = null;

    /****** Open database for insert,update,delete in syncronized manner *******/
    private static synchronized SQLiteDatabase open() throws SQLException {

        return todoHelper.getWritableDatabase();
    }
    // Getting All User data
    public static List<TaskData> getAllUserData() {

        List<TaskData> taskList = new ArrayList<TaskData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM todos";


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TaskData data = new TaskData();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setName(cursor.getString(1));
                data.setDesc(cursor.getString(2));
                data.setYear(cursor.getInt(3));
                data.setMonth(cursor.getInt(4));
                data.setDay(cursor.getInt(5));
                data.setHour(cursor.getInt(6));
                data.setMinute(cursor.getInt(7));

                // Adding contact to list
                taskList.add(data);
            } while (cursor.moveToNext());
        }

        // return user list
        return taskList;
    }
    /*********** Initialize database *************/
    public static void init(Context context) {
        if (todoHelper == null) {
            if (DEBUG)
                Log.i("DBAdapter", context.toString());
            todoHelper = new TodoHelper(context);
        }
    }
}
