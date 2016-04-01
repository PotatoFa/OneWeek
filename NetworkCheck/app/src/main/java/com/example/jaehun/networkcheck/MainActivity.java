package com.example.jaehun.networkcheck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jaehun.networkcheck.Data.Test;
import com.example.jaehun.networkcheck.Data.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends SetFontActiviry {

    Realm realm;

    EditText name, number, mail;
    TextView text_view;

    String TAG = "LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "ONCREATE");
        realm = Realm.getDefaultInstance();


        setContentView(R.layout.activity_main);

        mail = (EditText)findViewById(R.id.mail);
        name = (EditText)findViewById(R.id.name);
        number = (EditText)findViewById(R.id.number);
        text_view = (TextView)findViewById(R.id.text_view);

        startActivity(new Intent(getApplicationContext(), TintActivity.class));
        finish();


    }
    public void add(View view){

        realm.beginTransaction();

        User user = realm.createObject(User.class);
        user.setName(name.getText().toString());
        user.setNumber(number.getText().toString());
        user.setMail(mail.getText().toString());
        realm.commitTransaction();
        Log.i(TAG, "Add user");

        name.setText(null);
        number.setText(null);

    }

    public void view(View view){
        text_view.setText("");
        RealmResults<User> results = realm.where(User.class).findAll();
        for(User user : results){
            text_view.append(user.getName() + "/"
                        + user.getNumber() + "/"
                        + user.getMail() + "\n");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "DESTROY");

        realm.close();
    }


}
