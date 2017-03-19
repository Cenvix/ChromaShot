package com.cs.mobapde.canvas;

import android.graphics.Bitmap;

/**
 * Created by rafe0 on 3/8/2017.
 */

public class GameObject {

    protected String name;

    protected int hp;
    protected int width;
    protected int height;
    protected int radius;

    protected float speed;
    protected float xCoord;
    protected float yCoord;

    protected float xVector;
    protected float yVector;

    protected Bitmap sprite;

    public void move(int speedModifier, float timeDelta) {
        xCoord += speed * xVector * speedModifier * timeDelta;
        yCoord += speed * yVector * speedModifier * timeDelta;
    }

    public boolean isDead() {
        if(hp <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void decrementHp() {
        this.hp--;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
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

    public float getxVector() {
        return xVector;
    }

    public void setxVector(float xVector) {
        this.xVector = xVector;
    }

    public float getyVector() {
        return yVector;
    }

    public void setyVector(float yVector) {
        this.yVector = yVector;
    }

    public Bitmap getSprite() {
        return sprite;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = Bitmap.createScaledBitmap(sprite, width, height, true);
    }
}
