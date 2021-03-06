package com.cs.mobapde.canvas;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import com.cs.mobapde.R;
/**
 * Created by rafe0 on 3/12/2017.
 */

public class Shot extends GameObject {

    private String color;

    public Shot(Resources resources, String color, Player player) {
        name = "shot";

        width = 48;
        height = 48;
        radius = 24;

        xCoord = player.getxCoord();
        yCoord = player.getyCoord();

        hp = 1;
        speed = 1000;
        xVector = (float)Math.cos((player.getRotation())*(Math.PI/180));
        yVector = (float)Math.sin((player.getRotation())*(Math.PI/180));

        this.color = color;

        switch(color) {
            case "red":
                setSprite(new BitmapFactory().decodeResource(resources, R.drawable.target_magenta));
                break;
            case "blue":
                setSprite(new BitmapFactory().decodeResource(resources, R.drawable.target_cyan));
                break;
            case "green":
                setSprite(new BitmapFactory().decodeResource(resources, R.drawable.target_yellow));
                break;
            case "chroma":
                setSprite(new BitmapFactory().decodeResource(resources, R.drawable.target_black));
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
