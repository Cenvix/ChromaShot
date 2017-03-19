package com.cs.mobapde.canvas;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import com.cs.mobapde.R;

/**
 * Created by rafe0 on 3/12/2017.
 */

public class Player extends GameObject {

    private float rotation;

    public Player(Resources resources) {
        name = "player";

        width = 32;
        height = 32;
        radius = 16;

        rotation = 0;
        xCoord = 400;
        yCoord = 300;

        hp = 1;
        speed = 300;
        xVector = 0;
        yVector = 0;

        setSprite(new BitmapFactory().decodeResource(resources, R.drawable.player));
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
