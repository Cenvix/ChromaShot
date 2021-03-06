package com.cs.mobapde.canvas;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.cs.mobapde.R;

/**
 * Created by rafe0 on 3/12/2017.
 */

public class Target extends GameObject {

    private String color;

    public Target(Resources resources) {
        name = "target";

        width = 64;
        height = 64;
        radius = 32;

        xCoord = 400;
        yCoord = 300;

        hp = 1;
        speed = 200;
        xVector = 0;
        yVector = 0;

        int randTemp = (int)Math.floor(Math.random()*3);
        if(randTemp == 0) {
            color = "red";
            setSprite(new BitmapFactory().decodeResource(resources, R.drawable.target_magenta));
        }
        else if (randTemp == 1) {
            color = "green";
            setSprite(new BitmapFactory().decodeResource(resources, R.drawable.target_yellow));
        }
        else {
            color = "blue";
            setSprite(new BitmapFactory().decodeResource(resources, R.drawable.target_cyan));
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void empower() {
        speed = (float)(speed + 50);
    }
}
