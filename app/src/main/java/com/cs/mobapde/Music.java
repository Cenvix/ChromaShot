package com.cs.mobapde;

/**
 * Created by testAcc on 4/5/2017.
 */

public class Music {
    private String music;
    private int music_id;
    private float value;

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    @Override
    public String toString() {
        return music;
    }
}
