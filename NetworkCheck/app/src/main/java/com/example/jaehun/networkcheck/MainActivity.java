package com.example.jaehun.networkcheck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jaehun.networkcheck.Data.Test;
import com.example.jaehun.networkcheck.Data.User;
import com.example.jaehun.networkcheck.Network.NetworkActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends SetFontActiviry {

    Realm realm;

    TextView text_view;

    String TAG = "LOG";

    @Bind(R.id.mail) EditText mail;
    @Bind(R.id.name) EditText name;
    @Bind(R.id.number) EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "ONCREATE");
        realm = Realm.getDefaultInstance();

        setContentView(R.layout.activity_main);

        startActivity(new Intent(getApplicationContext(), ImageActivity.class));
        finish();

        text_view = (TextView)findViewById(R.id.text_view);


    }

    @OnClick(R.id.add)
    public void add(){

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

    @OnClick(R.id.view)
    public void view(){
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
