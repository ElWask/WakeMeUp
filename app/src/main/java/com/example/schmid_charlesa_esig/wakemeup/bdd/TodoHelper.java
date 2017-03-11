package com.example.schmid_charlesa_esig.wakemeup.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                Todo.TodoEntry.COL_TASK_DESC + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Todo.TodoEntry.TABLE);
        onCreate(sqLiteDatabase);
    }
}
