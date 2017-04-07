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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.sql.DataSource;

/**
 * Created by Vincent on 03/13/2017.
 */

public class OptionsActivity extends PopupWindow implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {

    Context parent;
    RelativeLayout parentLayout;
    Button reset;

    int resetStatus;

    Switch soundSwitch, musicSwitch;

    ScoresDataSource scoresDataSource;
    OptionDataSource optionDataSource;
    MainActivity mainActivity;
    ArrayList<Options> optionList;

    public OptionsActivity(Context parent, View layout, RelativeLayout relativeLayout, int width, int height, ArrayList<Options> optionList){




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
        optionDataSource = new OptionDataSource(parent);


        layout.setOnTouchListener(this);

        this.optionList = optionList;


        this.musicSwitch = (Switch)layout.findViewById(R.id.switch_music);
        if(optionList.get(0).isOn())
            this.musicSwitch.setChecked(true);
        else
            this.musicSwitch.setChecked(false);
        this.musicSwitch.setOnClickListener(this);

        this.soundSwitch = (Switch)layout.findViewById(R.id.switch_sound);
        if(optionList.get(1).isOn())
            this.soundSwitch.setChecked(true);
        else
            this.soundSwitch.setChecked(false);

        this.soundSwitch.setOnClickListener(this);

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
        if(v.equals(musicSwitch)){
            this.optionDataSource.open();
            if(musicSwitch.isChecked()){
                this.optionList.get(0).setValue(1);
                this.optionDataSource.updateOptionsByID(1,1);
            }else {
                this.optionList.get(0).setValue(0);
                this.optionDataSource.updateOptionsByID(1,0);
            }
            this.mainActivity.updateSounds();
            this.optionDataSource.close();
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
