package com.cs.mobapde;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by testAcc on 4/5/2017.
 */

public class SoundDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {DatabaseHelper.SOUND_ID, DatabaseHelper.COLUMN_SOUND};

    SoundDataSource(Context context)    {dbHelper = new DatabaseHelper(context); }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public int setValue(int value){
        return 0;
    }

    public void close() {
        dbHelper.close();
    }


}
