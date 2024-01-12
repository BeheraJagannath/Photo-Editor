package com.example.neweditor.newa;

import android.graphics.Bitmap;

public class User {
    private String name;
    private int nameofpainter;

    public User(String name,int nameofpainter) {
        this.name = name;
        this.nameofpainter = nameofpainter;

    }

    public int getNameofpainter() {
        return nameofpainter;
    }

    public void setNameofpainter(int nameofpainter) {
        this.nameofpainter = nameofpainter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
