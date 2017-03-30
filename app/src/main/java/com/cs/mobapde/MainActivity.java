package com.cs.mobapde;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button start, options;

    TextView highscore;


    OptionsActivity optionsActivity = null;
    LayoutInflater inflater;
    ScoresDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new ScoresDataSource(this);
        dataSource.open();

        //  ReplaceFont.replaceDefaultFont(this,"DEFAULT","main/res/fonts/Nexa Bold.otf");
        AssetManager am = this.getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Nexa Bold.otf"));
        Typeface typeface2 = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Nexa Light.otf"));
        hideActionBar();

        start = (Button)findViewById(R.id.start_btn);
        start.setOnClickListener(this);
        start.setTypeface(typeface);

        options = (Button)findViewById(R.id.options_btn);
        options.setOnClickListener(this);
        options.setTypeface(typeface);


        highscore = (TextView)findViewById(R.id.main_highscore);
        highscore.setTypeface(typeface2);




        dataSource = new ScoresDataSource(this);
        dataSource.open();

        highscore.setText("HighScore: "+dataSource.queryTopScore().getScore());

        initiatePopUps();

    }

    @Override
    public void onClick(View view) {
        if(view.equals(options)){
//            Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
//            startActivity(intent);

            optionsActivity.show();
        }
        else if(view.equals(start)){
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            this.startActivity(intent);
        }
    }

    public void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    public void initiatePopUps(){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View optionLayout = (ViewGroup)inflater.inflate(R.layout.activity_options,null);
        optionsActivity = new OptionsActivity(this,optionLayout,(RelativeLayout)findViewById(R.id.activity_main),width,height);

   }
}
