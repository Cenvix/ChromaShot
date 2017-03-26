package com.cs.mobapde;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by Vincent on 03/13/2017.
 */

public class DifficultyActivity extends PopupWindow implements View.OnTouchListener, View.OnClickListener{


    protected Button easy, medium, hard;

    protected Context parent;
    RelativeLayout parentLayout;

    public DifficultyActivity(Context parent,View layout, RelativeLayout relativeLayout, int width, int height){


        super(layout,(int)(width*.9),(int)(width*.8),true);

        this.parent  = parent;
        this.parentLayout = relativeLayout;


        this.setElevation(24);
        this.setAnimationStyle(android.R.style.Animation_InputMethod);

        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setTouchInterceptor(this);

        layout.setOnTouchListener(this);

        easy = (Button)layout.findViewById(R.id.btn_easy);
        medium = (Button)layout.findViewById(R.id.btn_medium);
        hard = (Button)layout.findViewById(R.id.btn_hard);

        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        hard.setOnClickListener(this);



    }

    public void show(){
        showAtLocation(parentLayout, Gravity.CENTER,0,0);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            this.dismiss();
            return true;
        }




        //}



       // this.dismiss();
        return false;
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(parent, GameActivity.class);
        if(view.equals(easy)){
            intent.putExtra("mode", 0);
            intent.putExtra("modeString", "Easy");
            this.parent.startActivity(intent);
            System.out.println("WOW");
        }
        else if(view.equals(medium)){
            intent.putExtra("mode", 1);
            intent.putExtra("modeString", "Medium");
            this.parent.startActivity(intent);
        }
        else if(view.equals(hard)){
            intent.putExtra("mode", 2);
            intent.putExtra("modeString", "Hard");
            this.parent.startActivity(intent);
        }
        dismiss();
    }
}
