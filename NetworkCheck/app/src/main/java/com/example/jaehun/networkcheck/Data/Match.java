package com.example.jaehun.networkcheck.Data;

import io.realm.RealmObject;

/**
 * Created by jaehun on 16. 3. 25..
 */
public class Match extends RealmObject {
    private String match_id;

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }
}
