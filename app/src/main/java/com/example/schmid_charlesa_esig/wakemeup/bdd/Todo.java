package com.example.schmid_charlesa_esig.wakemeup.bdd;

import android.provider.BaseColumns;

/**
 * Created by Charly on 10.03.2017.
 */

public class Todo {
    public static final String DB_NAME ="com.example.schmid_charlesa_esig.wakemeup.bdd";
    public static final int DB_VERSION = 5;

    public class TodoEntry implements BaseColumns{
        public static  final String TABLE = "todos";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DESC = "desc";
        public static final String COL_TASK_YEAR = "year";
        public static final String COL_TASK_MONTH = "month";
        public static final String COL_TASK_DAY = "day";
        public static final String COL_TASK_HOUR = "hour";
        public static final String COL_TASK_MIN = "minu";

    }
}
