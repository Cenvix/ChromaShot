package com.cs.mobapde.canvas;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.Image;
import com.cs.mobapde.R;
import java.util.Random;

/**
 * Created by rafe0 on 3/8/2017.
 */

public class Powerup extends GameObject{

    private Image img;

    public Powerup (Resources resources) {
        width = 24;
        height = 24;
        radius = 12;

        xCoord = new Random().nextFloat();
        yCoord = new Random().nextFloat();

        hp = 1;
        speed = 0;
        xVector = 0;
        yVector = 0;

        int randTemp = (int) Math.floor(Math.random()*3);

        if(randTemp == 0) {
            name = "shield";
            setSprite(new BitmapFactory().decodeResource(resources, R.drawable.powerup_shield));
        }
        else if(randTemp == 1) {
            name = "slow";
            setSprite(new BitmapFactory().decodeResource(resources, R.drawable.powerup_slow));
        }
        else if(randTemp == 2) {
            name = "haste";
            setSprite(new BitmapFactory().decodeResource(resources, R.drawable.powerup_haste));
        }
        else if(randTemp == 3) {
            name = "void";
            setSprite(new BitmapFactory().decodeResource(resources, R.drawable.powerup_void));
        }
        else{
            name = "push";
            setSprite(new BitmapFactory().decodeResource(resources, R.drawable.powerup_push));
        }

    }
}
