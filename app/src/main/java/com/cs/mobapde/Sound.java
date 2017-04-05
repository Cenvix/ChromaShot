package com.cs.mobapde;

/**
 * Created by testAcc on 4/5/2017.
 */

public class Sound {
    private String sound;
    private int sound_id;
    private int value;

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getSound_id() {
        return sound_id;
    }

    public void setSound_id(int sound_id) {
        this.sound_id = sound_id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return sound;
    }
}
