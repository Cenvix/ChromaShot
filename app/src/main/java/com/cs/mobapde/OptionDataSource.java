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

    public void updateOptionsByID(int id, int value){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_VALUE,value);

        String selection = DatabaseHelper.OPTION_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        
        List<Options> options = new ArrayList<Options>();
        database.update(DatabaseHelper.TABLE_OPTIONS, values, selection, selectionArgs);

    }

    public Options addOptions(String name,int id, int value){

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.COLUMN_OPTION, name);
            values.put(DatabaseHelper.OPTION_ID, id);
            values.put(DatabaseHelper.COLUMN_VALUE, value);

            long insertId = database.insert(DatabaseHelper.TABLE_OPTIONS, null, values);

            Cursor cursor = database.query(DatabaseHelper.TABLE_OPTIONS, allColumns,
                    DatabaseHelper.OPTION_ID + " = " + insertId, null, null, null, null);

            cursor.moveToFirst();
            Options newOptions = cursorToOption(cursor);
            cursor.close();


        return newOptions;


    }

    public List<Options> initializeOptions(){
        List<Options> options = new ArrayList<Options>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_OPTIONS,allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Options option = cursorToOption(cursor);
            options.add(option);
            cursor.moveToNext();
        }
        cursor.close();

        boolean foundSound=false;
        boolean foundMusic=false;

        for (Options opt : options) {
            if(opt.getId()==1){
                foundMusic=true;
                break;
            }
            if(opt.getId()==2){
                foundSound=true;
                break;
            }
        }

        if(!foundMusic){
            //insert Music
            options.add(addOptions("Music",1,1));
            //options.add(new Music)
        }
        if(!foundSound){
            //insert Sound
            //options.add(new Sound)
            options.add(addOptions("Sound",2,1));
        }





        return options;
    }

    private Options cursorToOption(Cursor cursor) {
        Options options = new Options();
        options.setId(cursor.getInt(0));
        options.setOption(cursor.getString(1));
        options.setValue(cursor.getInt(2));

//        System.out.println(cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3));

        return options;
    }



}
