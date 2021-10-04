package com.all.firestorecounterapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;


@IgnoreExtraProperties
public class CounterModel implements Serializable {

    private SingleEntry one;
    private SingleEntry two;
    private SingleEntry three;


    public CounterModel() {
        super();
    }


    public SingleEntry getOne() {
        return one;
    }

    public void setOne(SingleEntry one) {
        this.one = one;
    }

    public SingleEntry getTwo() {
        return two;
    }

    public void setTwo(SingleEntry two) {
        this.two = two;
    }

    public SingleEntry getThree() {
        return three;
    }

    public void setThree(SingleEntry three) {
        this.three = three;
    }

    @Override
    public String toString() {
        return "CounterModel{" +
                "one=" + one +
                ", two=" + two +
                ", three=" + three +
                '}';
    }
}


