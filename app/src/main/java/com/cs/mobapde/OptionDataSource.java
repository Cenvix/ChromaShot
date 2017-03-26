package com.cs.mobapde;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by testAcc on 3/26/2017.
 */

public class OptionDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.OPTION_ID, DatabaseHelper.COLUMN_OPTION,
            DatabaseHelper.COLUMN_VALUE};

    public OptionDataSource(Context context) {dbHelper = new DatabaseHelper(context); }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void updateOptionsByID(int id, String option, int value){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_OPTION,option);

        String selection = DatabaseHelper.OPTION_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        
        List<Options> options = new ArrayList<Options>();
        database.update(DatabaseHelper.TABLE_OPTIONS, values, selection, selectionArgs);

    }

    private Options cursorToComment(Cursor cursor) {
        Options options = new Options();
        options.setId(cursor.getInt(0));
        options.setOption(cursor.getString(1));

        System.out.println(cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3));

        return options;
    }



}
