package com.chrislee.tetris;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RankDatabase {

    private static final String DB_NAME = "rank.db";
    private static final String DB_TABLE = "table1";
    private static final int DB_VERSION = 1;

    private static final String KEY_ID = "_id";
    public static final String KEY_RANK = "rank";
    public static final String KEY_SCORE = "score";
    public static final String KEY_NAME = "name";

    private static final String DB_CREATE = "CREATE TABLE " + DB_TABLE + " (" + KEY_ID + " INTEGER PRIMARY_KEY,"
            + KEY_RANK + " INTEGER," + KEY_SCORE + " INTEGER" + KEY_NAME + " TEXT)";

    private Context mContext = null;
    private SQLiteDatabase mDatabase = null;
    private DatabaseHelper mHelper = null;

    public void RankDatabase(Context context) {
        mContext = context;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void open() {
        mHelper = new DatabaseHelper(mContext);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void close() {
        mHelper.close();
    }

}
