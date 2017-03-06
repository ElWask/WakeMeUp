package com.example.schmid_charlesa_esig.wakemeup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.primary;

/**
 * Created by SCHMID_CHARLESA-ESIG on 06.03.2017.
 */

public class DBAdapter {

    /******* if debug is set true then it will show all Logcat message ***/
    public static final boolean DEBUG = true;

    /********** Logcat TAG ************/
    public static final String LOG_TAG = "DBAdapter";

    /************ Table Fields ************/
    public static final String KEY_ID = "_id";

    public static final String KEY_TASK_NAME = "task_name";

    public static final String KEY_TASK_EMAIL = "task_email";


    /************* Database Name ************/
    public static final String DATABASE_NAME = "DB_sqllite";

    /**** Database Version (Increase one if want to also upgrade your database) ****/
    public static final int DATABASE_VERSION = 1;// started at 1

    /** Table names */
    public static final String TASK_TABLE = "tbl_task";

    /**** Set all table with comma seperated like TASK_TABLE,ABC_TABLE ******/
    private static final String[ ] ALL_TABLES = { TASK_TABLE };

    /** Create table syntax */
    private static final String TASK_CREATE = "create table tbl_task( _id integer primary key autoincrement,task_name text not null, task_email text not null);";

    /********* Used to open database in syncronized way *********/
    private static DataBaseHelper DBHelper = null;

    protected DBAdapter() {

    }

    /********** Initialize database *********/
    public static void init(Context context) {
        if (DBHelper == null) {
            if (DEBUG)
                Log.i("DBAdapter", context.toString());
            DBHelper = new DataBaseHelper(context);
        }
    }

    /********** Main Database creation INNER class ********/
    private static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (DEBUG)
                Log.i(LOG_TAG, "new create");
            try {
                db.execSQL(TASK_CREATE);


            } catch (Exception exception) {
                if (DEBUG)
                    Log.i(LOG_TAG, "Exception onCreate() exception");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (DEBUG)
                Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
                        + "to" + newVersion + "...");

            for (String table : ALL_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }
            onCreate(db);
        }

    } // Inner class closed


    /***** Open database for insert,update,delete in syncronized manner ****/
    private static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }


    /************* General functions*************/


    /*********** Escape string for single quotes (Insert,Update) ********/
    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace(", );
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }

    /********** UnEscape string for single quotes (show data) ************/
    private static String sqlUnEscapeString(String aString) {

        String aReturn = "";

        if (null != aString) {
            aReturn = aString.replace(""," ");
        }

        return aReturn;
    }

    /********* Task data functons *********/

    public static void addTaskData(TaskData uData) {

        // Open database for Read / Write       

        final SQLiteDatabase db;
        try {
            db = open();

        String name = sqlEscapeString(uData.getName());
        String email = sqlEscapeString(uData.getEmail());
        ContentValues cVal = new ContentValues();
        cVal.put(KEY_TASK_NAME, name);
        cVal.put(KEY_TASK_EMAIL, email);
        // Insert task values in database
        db.insert(TASK_TABLE, null, cVal);
        db.close(); // Closing database connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Updating single data
    public static int updateTaskData(TaskData data) {

        final SQLiteDatabase db;
        try {
            db = open();


        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, data.getName());
        values.put(KEY_TASK_EMAIL, data.getEmail());

        // updating row
        return db.update(TASK_TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Getting single contact
    public static TaskData getTaskData(int id) {

        // Open database for Read / Write
        final SQLiteDatabase db;
        try {
            db = open();


        Cursor cursor = db.query(TASK_TABLE, new String[] { KEY_ID,
                        KEY_TASK_NAME, KEY_TASK_EMAIL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);



        if (cursor != null)
            cursor.moveToFirst();

        TaskData data = new TaskData(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        // return task data
        return data;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Getting All Task data
    public static List<TaskData> getAllTaskData() {

        List<TaskData> contactList = new ArrayList<TaskData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TASK_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db;
        try {
            db = open();

        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TaskData data = new TaskData();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setName(cursor.getString(1));
                data.setEmail(cursor.getString(2));

                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // return task list
        return contactList;
    }



    // Deleting single contact
    public static void deleteTaskData(TaskData data) {
        final SQLiteDatabase db;
        try {
            db = open();

        db.delete(TASK_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getting dataCount

    public static int getTaskDataCount() {

        final SQLiteDatabase db;
        try {
            db = open();


        String countQuery = "SELECT  * FROM " + TASK_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}  // CLASS CLOSED}