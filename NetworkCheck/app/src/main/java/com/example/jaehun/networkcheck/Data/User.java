package com.example.jaehun.networkcheck.Data;

import io.realm.RealmObject;

/**
 * Created by jaehun on 16. 3. 25..
 */
public class User extends RealmObject {
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;

}
