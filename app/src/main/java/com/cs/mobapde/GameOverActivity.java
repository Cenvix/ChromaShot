package com.cs.mobapde;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Vincent on 03/13/2017.
 */

public class GameOverActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    GameActivity parent;


    TextView score;
    Button retry, home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        retry = (Button) findViewById(R.id.btn_retry);
        home = (Button) findViewById(R.id.btn_home);
        score = (TextView) findViewById(R.id.score);

        score.setText("Score: "+getIntent().getStringExtra("score"));

        retry.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }
    //
//    Context parent;
//    RelativeLayout parentLayout;
//
//    public GameOverActivity(Context parent, View layout, RelativeLayout relativeLayout, int width, int height){
//
//        super(layout,(int)(width*.9),(int)(width*.8),true);
//
//        this.parent  = parent;
//        this.parentLayout = relativeLayout;
//
//        this.setElevation(24);
//        this.setAnimationStyle(android.R.style.Animation_InputMethod);
//
//        this.setTouchable(true);
//        this.setOutsideTouchable(true);
//        this.setTouchInterceptor(this);
//
//        layout.setOnTouchListener(this);
//
//
//        //showAtLocation(relativeLayout,Gravity.CENTER,0,0);
//    }
//
//    public void show(SurfaceView parentLayout){
//        showAtLocation(parentLayout, Gravity.CENTER,0,0);
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//
//        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(retry)){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result","retry");
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
        else if(v.equals(home)){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result","home");
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }

    }
}
