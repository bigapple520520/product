package com.xuan.game.tetris;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 存储排行的数据库操作
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-14 下午8:19:00 $
 */
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

    private Context context = null;
    private SQLiteDatabase database = null;
    private DBHelper dBHelper = null;

    public RankDatabase(Context context) {
        this.context = context;
    }

    public void open() {
        dBHelper = new DBHelper(context);
        database = dBHelper.getWritableDatabase();
    }

    public void close() {
        dBHelper.close();
    }

    /**
     * 数据库操作类
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-1-14 下午8:15:48 $
     */
    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
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

}
