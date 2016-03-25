package com.example.jaehun.networkcheck.Data;

import io.realm.RealmObject;

/**
 * Created by jaehun on 16. 3. 25..
 */
public class Test extends RealmObject {
    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    private String test_id;

}
