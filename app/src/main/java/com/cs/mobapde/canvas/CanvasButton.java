package com.cs.mobapde.canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.cs.mobapde.R;

/**
 * Created by Vincent on 03/13/2017.
 */

public class CanvasButton {

    private String color;

    private float xCoord;
    private float yCoord;

    private int width;
    private int height;

    private Bitmap sprite;

    public CanvasButton(Resources resources, String color) {
        this.color = color;
        this.width = 128;
        this.height = 128;

        switch(color) {
            case "red":
                xCoord = 20;
                yCoord = 20;
                sprite = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(resources, R.drawable.button_red), width, height, true);
                break;
            case "green":
                xCoord = 20;
                yCoord = 120;
                sprite = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(resources, R.drawable.button_green), width, height, true);
                break;
            case "blue":
                xCoord = 20;
                yCoord = 220;
                sprite = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(resources, R.drawable.button_blue ), width, height, true);
                break;
        }
    }

    public float getxCoord() {
        return xCoord;
    }

    public void setxCoord(float xCoord) {
        this.xCoord = xCoord;
    }

    public float getyCoord() {
        return yCoord;
    }

    public void setyCoord(float yCoord) {
        this.yCoord = yCoord;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getSprite() {
        return sprite;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }
}
