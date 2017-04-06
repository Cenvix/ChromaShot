package com.cs.mobapde;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.sql.DataSource;

/**
 * Created by Vincent on 03/13/2017.
 */

public class OptionsActivity extends PopupWindow implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {

    Context parent;
    RelativeLayout parentLayout;
    Button reset;
    int resetStatus;
    ScoresDataSource scoresDataSource;
    MainActivity mainActivity;


    public OptionsActivity(Context parent,View layout, RelativeLayout relativeLayout,int width, int height){




        super(layout,(int)(width*.9),(int)(height*.6),true);

        this.parent  = parent;
        this.parentLayout = relativeLayout;

        mainActivity = (MainActivity)parent;

        this.setElevation(24);
        this.setAnimationStyle(android.R.style.Animation_InputMethod);

        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setTouchInterceptor(this);

        this.reset = (Button)layout.findViewById(R.id.reset_btn);
        this.reset.setOnClickListener(this);
        this.reset.setOnLongClickListener(this);

        this.resetStatus = 5;

        scoresDataSource = new ScoresDataSource(parent);

        layout.setOnTouchListener(this);




        //showAtLocation(relativeLayout,Gravity.CENTER,0,0);

    }

    public void show(){
        showAtLocation(parentLayout, Gravity.CENTER,0,0);
        this.resetStatus = 5;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            this.dismiss();
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(reset)&&resetStatus==5){
            Toast.makeText(parent,"Long Press Me.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.equals(reset)) {
            if(resetStatus>0){

                Toast.makeText(parent,"Long Press me "+resetStatus+" more times to reset.",Toast.LENGTH_SHORT).show();
                resetStatus--;
            }else if(resetStatus<=0){
                scoresDataSource.open();
                scoresDataSource.deleteAllScores();
                scoresDataSource.close();
                Toast.makeText(parent,"HighScores Reset!",Toast.LENGTH_SHORT).show();
                resetStatus = 5;
                mainActivity.setHighscore();
            }
        }
        return false;
    }
}
