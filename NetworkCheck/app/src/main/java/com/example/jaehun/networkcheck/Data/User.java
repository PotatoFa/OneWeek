package com.example.jaehun.networkcheck.Data;

import io.realm.RealmObject;

/**
 * Created by jaehun on 16. 3. 25..
 */
public class User extends RealmObject {
    private String name;
    private String number;


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    private String mail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
