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

/**
 * Created by Vincent on 03/13/2017.
 */

public class OptionsActivity extends PopupWindow implements View.OnTouchListener {

    View parent;

    public OptionsActivity(View layout, RelativeLayout relativeLayout,int width, int height){




        super(layout,(int)(width*.9),(int)(width*.8),true);

        this.parent  = layout;


        this.setElevation(24);
        this.setAnimationStyle(android.R.style.Animation_InputMethod);

        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setTouchInterceptor(this);

        parent.setOnTouchListener(this);


        showAtLocation(relativeLayout,Gravity.CENTER,0,0);
    }

    protected void setPopWindow(){
        //DisplayMetrics dm = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dm);

    //    getWindow().setLayout((int)(dm.widthPixels*.5),(int)(dm.heightPixels*.5));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            this.dismiss();
            return true;
        }
        return false;
    }
}
