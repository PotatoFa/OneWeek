package com.example.jaehun.networkcheck;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.jaehun.networkcheck.Data.Match;
import com.example.jaehun.networkcheck.Data.Test;
import com.example.jaehun.networkcheck.Data.User;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import io.realm.annotations.RealmModule;

/**
 * Created by jaehun on 16. 3. 22..
 */
public class ApplicationController extends Application {

    NetworkChangeReceiver networkChangeReceiver;
    private static ApplicationController applicationController;
    public static ApplicationController getInstance() {return applicationController;}
    IntentFilter intentFilter;


    @Override
    public void onCreate() {
        super.onCreate();
        this.applicationController = this;
        networkChangeReceiver = null;

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("jaehun.realm")
                .schemaVersion(4)
                .setModules(new com.example.jaehun.networkcheck.Data.RealmModule())
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);


    }

    public void startReceiver(){

        if(networkChangeReceiver == null){
            networkChangeReceiver = new NetworkChangeReceiver();
            registerReceiver(networkChangeReceiver, intentFilter);
            Log.i("TET", "register");
        }

    }

    public void finishReceiver(){

        try {
            if(networkChangeReceiver != null) {
                unregisterReceiver(networkChangeReceiver);
                networkChangeReceiver = null;
                Log.i("TET", "unregister");
            }

        }catch (IllegalStateException ex){
            ex.printStackTrace();
        }

    }

    RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            RealmSchema schema = realm.getSchema();

            if (oldVersion == 0) {
                schema.get("User")
                        .addField("mail", String.class);
                oldVersion++;
            }
            if (oldVersion == 1){
                schema.get("User").addField("address", String.class);
                oldVersion++;

            }
            if (oldVersion == 2){
                schema.get("User").removeField("address");
            }
            if (oldVersion == 3){
                schema.remove("Test");
            }
        }
    };

}
