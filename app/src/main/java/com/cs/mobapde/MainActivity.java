package com.cs.mobapde;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button start, options;

    ImageView background;
    TextView highscore;
    ValueAnimator animator;

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

        //dataSource.deleteAllScores();

        highscore.setText("HighScore: "+dataSource.queryTopScore().getScore());

        initiatePopUps();

        background = (ImageView) findViewById(R.id.background);

        background.setOnClickListener(this);
        randBG();

//        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setInterpolator(new LinearInterpolator());
//        animator.setDuration(100000L);
//        Random r = new Random();
//        final int startRand = r.nextInt(1080)%1080;
//        animator.setCurrentPlayTime(100000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                final float progress = (float) animation.getAnimatedValue();
//                final float height = 2160+1080;
//                final float translationX = height * progress + startRand;
//                background.setScrollY((int)translationX);
//
//            }
//        });
//        animator.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        highscore.setText("HighScore: "+dataSource.queryTopScore().getScore());
        randBG();
        //animator.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //animator.start();
    }

    protected void randBG(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Random r = new Random();


        int startRand = r.nextInt(1080)%1080 + dm.widthPixels * r.nextInt(5)* ((r.nextInt()==0)?-1:1);

        background.setScrollY(startRand);
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
        else
            randBG();
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
