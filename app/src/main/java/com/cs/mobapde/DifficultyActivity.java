package com.cs.mobapde;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

/**
 * Created by Vincent on 03/13/2017.
 */

public class DifficultyActivity extends Activity implements View.OnClickListener{


    protected Button easy, medium, hard;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        easy = (Button)findViewById(R.id.btn_easy);
        medium = (Button)findViewById(R.id.btn_medium);
        hard = (Button)findViewById(R.id.btn_hard);

        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        hard.setOnClickListener(this);
    }

    protected void setPopWindow(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int)(dm.widthPixels*.5),(int)(dm.heightPixels*.5));

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(DifficultyActivity.this, GameActivity.class);

        if(view.equals(easy)){
            intent.putExtra("mode", 0);
            intent.putExtra("modeString", "Easy");
        }
        else if(view.equals(medium)){
            intent.putExtra("mode", 1);
            intent.putExtra("modeString", "Medium");
        }
        else if(view.equals(hard)){
            intent.putExtra("mode", 2);
            intent.putExtra("modeString", "Hard");
        }

        startActivity(intent);

        this.finish();
    }
}
