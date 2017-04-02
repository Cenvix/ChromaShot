package com.cs.mobapde;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by testAcc on 3/26/2017.
 */

public class ScoresDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.SCORE_ID,DatabaseHelper.COLUMN_SCORE};

    public ScoresDataSource(Context context) {dbHelper = new DatabaseHelper(context); }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public Scores addScore(int score){
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_SCORE, score);
        long insertId = database.insert(DatabaseHelper.TABLE_SCORES,null,values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_SCORES,allColumns,
                DatabaseHelper.SCORE_ID + " = " +insertId, null, null, null, null);

        cursor.moveToFirst();
        Scores newScore = cursorToScore(cursor);
        cursor.close();

        return  null;

    }

    public void deleteScore(Scores score){
        int id = score.getId();
        System.out.println("deleted score id: " + id);
        database.delete(DatabaseHelper.TABLE_SCORES,DatabaseHelper.SCORE_ID + " = "
                + id, null );

    }

    public void deleteAllScores(){
        database.delete(DatabaseHelper.TABLE_SCORES,null, null );

    }

    public List<Scores> queryAllScores(){
        List<Scores> scores = new ArrayList<Scores>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_SCORES,allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Scores score = cursorToScore(cursor);
            scores.add(score);
            cursor.moveToNext();
        }



        cursor.close();
        return scores;


    }

    public Scores queryTopScore(){
        List<Scores> scores = new ArrayList<Scores>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_SCORES,allColumns, null, null,
                null, null,DatabaseHelper.COLUMN_SCORE + " DESC ");


        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Scores score = cursorToScore(cursor);
            scores.add(score);
            cursor.moveToNext();
        }



        cursor.close();

        if(scores.size()>0)
            return scores.get(0);
        else{
            Scores s = new Scores();
            s.setScore(0);
            return s;
        }


    }




    private Scores cursorToScore(Cursor cursor){
        Scores score = new Scores();
        score.setId(cursor.getInt(0));
        score.setScore(cursor.getInt(1));


        return score;

    }
}
