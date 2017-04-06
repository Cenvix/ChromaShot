package com.cs.mobapde;

/**
 * Created by CCS on 06/03/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.StringBuilderPrinter;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String TABLE_SCORES= "scores";
    public static final String SCORE_ID = "_id";
    public  static final String COLUMN_SCORE = "score";


    public static final String TABLE_OPTIONS = "options";
    public static final String OPTION_ID = "_id";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_OPTION = "option";

    public static final String COLUMN_OWNER = "owner";
    public static final String COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "commment.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_SCORES = "create table "
            + TABLE_SCORES + "( " + SCORE_ID
            + " integer primary key autoincrement, " + COLUMN_SCORE
            + " integer not null "+");";

    private static final String DATABASE_CREATE_OPTIONS = "create table "
            + TABLE_OPTIONS + "( " + OPTION_ID
            + " integer primary key autoincrement, " + COLUMN_OPTION
            + " text not null, "+ COLUMN_VALUE+ " text not null "+ ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_OPTIONS);
        database.execSQL(DATABASE_CREATE_SCORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

}