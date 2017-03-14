package com.cs.mobapde;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Created by Vincent on 03/13/2017.
 */

public class OptionsActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //setPopWindow();
    }

    protected void setPopWindow(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int)(dm.widthPixels*.5),(int)(dm.heightPixels*.5));
    }


}
