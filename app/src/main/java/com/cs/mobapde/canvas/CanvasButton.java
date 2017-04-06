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

    private boolean visible;

    public CanvasButton(Resources resources, String type, int width) {
        this.color = type;
        this.width = width;
        this.height = 150;
        this.visible = true;

        switch(type) {
            case "red":
                xCoord = 20;
                yCoord = 20;
                sprite = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(resources, R.drawable.magenta_btn), width, height, true);
                break;
            case "green":
                xCoord = 20;
                yCoord = 120;
                sprite = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(resources, R.drawable.yellow_btn), width, height, true);
                break;
            case "blue":
                xCoord = 20;
                yCoord = 220;
                sprite = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(resources, R.drawable.cyan_btn ), width, height, true);
                break;
            case "pause":
                xCoord = 0;
                yCoord = 0;
                width = 128;
                height = 128;
                sprite = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(resources, R.drawable.pause), width, height, true);
                break;
            case "resume":
                xCoord = 0;
                yCoord = 0;
                width = 128;
                height = 128;
                sprite = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(resources, R.drawable.resume), width, height, true);
                visible = false;
                break;
            case "home":
                xCoord = 150;
                yCoord = 0;
                width = 128;
                height = 128;
                sprite = Bitmap.createScaledBitmap(new BitmapFactory().decodeResource(resources, R.drawable.home), width, height, true);
                visible = false;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
