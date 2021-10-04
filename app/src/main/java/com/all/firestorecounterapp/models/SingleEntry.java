package com.all.firestorecounterapp.models;

public class SingleEntry {

    public String name;
    public long likes;

    @Override
    public String toString() {
        return "SingleEntry{" +
                "name='" + name + '\'' +
                ", likes=" + likes +
                '}';
    }
}
