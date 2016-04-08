package com.example.jaehun.networkcheck.Data;

import io.realm.RealmObject;

/**
 * Created by jaehun on 16. 4. 8..
 */
public class Image extends RealmObject{
    private byte[] binary;

    public byte[] getBinary() {
        return binary;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }
}
