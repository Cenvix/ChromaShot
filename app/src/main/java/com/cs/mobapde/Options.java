package com.cs.mobapde;

/**
 * Created by testAcc on 3/25/2017.
 */

public class Options {
    private String option;
    private int option_id;
    private int value;





    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getId() {
        return option_id;
    }

    public void setId(int option_id) {
        this.option_id = option_id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return option;
    }

    public boolean isOn(){
        if(value>=1)return true;
        else return false;
    }
}
