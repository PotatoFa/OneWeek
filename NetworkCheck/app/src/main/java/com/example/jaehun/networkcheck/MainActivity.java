package com.example.jaehun.networkcheck;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jaehun.networkcheck.Data.Match;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends SetFontActiviry {

    Realm realm;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));


    }
    public void status(View view){
        Log.i("STATUS : ", String.valueOf(NetworkUtil.getConnectivityStatus(this)));
    }

    public void unregist(View view){
        ApplicationController.getInstance().finishReceiver();
    }

    public void regist(View view){
        ApplicationController.getInstance().startReceiver();
    }

    public void realm(View view){

        i++;

        Realm realm1 = Realm.getInstance(getApplicationContext());
        Log.i("Realm Path :", realm1.getPath());


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getApplicationContext())
                .name("name"+i+".realm")
                .build();

        realm = Realm.getInstance(realmConfiguration);

        Log.i("Realm Path :", realm.getPath());

        RealmResults<Match> results = realm.where(Match.class).findAll();
        for(Match match : results){
            Log.i("match_id :", match.getMatch_id());
        }
    }

    public void test(View view){
        String path = realm.getPath();
        File file = new File(path);

        File folder = new File(file.getParent());
        Log.i("Realm Folder Path :", file.getParent());

        File files[] = folder.listFiles();

        for (int i=0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }


    }

}
