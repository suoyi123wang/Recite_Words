package com.example.lenovo.recitewords.Data;

import android.graphics.Bitmap;

public class WordList {
    private Bitmap pic;
    private String number;
    private String from;
    private String name;

    public WordList(String number, String from, String name) {
        this.number = number;
        this.from = from;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
