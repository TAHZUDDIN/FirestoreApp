package com.all.firestorecounterapp.models;

import java.io.Serializable;

public class CounterModel implements Serializable {

    private String documentId;
    private String name;
    private long likes;
    private String desc;

    public CounterModel() {
        super();
    }

    public CounterModel(String name, long likes) {
        this.name = name;
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String toString() {
        return "CounterModel{" +
                "documentId='" + documentId + '\'' +
                ", name='" + name + '\'' +
                ", likes=" + likes +
                ", desc='" + desc + '\'' +
                '}';
    }
}
