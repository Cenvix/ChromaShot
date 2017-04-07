package com.cs.mobapde;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Vincent on 03/13/2017.
 */

public class HowToActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {


    Button closeBtn;
    MainActivity mainActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);

        hideActionBar();

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getWindow().setLayout((int)Math.round(dm.widthPixels*.8),(int)Math.round(dm.heightPixels*.8));


        closeBtn = (Button)findViewById(R.id.close_button);
        this.closeBtn.setOnClickListener(this);

        mainActivity = (MainActivity)getParent();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            finish();
        }

        return false;

    }

    @Override
    public void onClick(View v) {
        if(v.equals(closeBtn)){

            finish();
        }
    }
    public void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


    }
}
