package com.cs.mobapde;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button start, options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  ReplaceFont.replaceDefaultFont(this,"DEFAULT","main/res/fonts/Nexa Bold.otf");

        hideActionBar();

        start = (Button)findViewById(R.id.start_btn);
        start.setOnClickListener(this);

        options = (Button)findViewById(R.id.options_btn);
        options.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        if(view.equals(options)){
            Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
            startActivity(intent);
        }
        else if(view.equals(start)){
            Intent intent = new Intent(MainActivity.this, DifficultyActivity.class);
            startActivity(intent);
        }
    }

    public void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }
}
