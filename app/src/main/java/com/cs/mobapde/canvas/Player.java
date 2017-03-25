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

        width = 64;
        height = 64;
        radius = 32;

        rotation = 0;
        xCoord = 400;
        yCoord = 300;

        hp = 1;
        speed = 300;
        xVector = 0;
        yVector = 0;

        setSprite(new BitmapFactory().decodeResource(resources, R.drawable.player));
    }

    public void move(int speedModifier, float timeDelta, int minX, int maxX, int minY, int maxY) {
        if(xCoord + speed * xVector * speedModifier * timeDelta > minX && xCoord + speed * xVector * speedModifier * timeDelta < maxX) {
            xCoord += speed * xVector * speedModifier * timeDelta;
        }

        if(yCoord + speed * yVector * speedModifier * timeDelta > minY && yCoord + speed * yVector * speedModifier * timeDelta < maxY) {
            yCoord += speed * yVector * speedModifier * timeDelta;
        }
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
