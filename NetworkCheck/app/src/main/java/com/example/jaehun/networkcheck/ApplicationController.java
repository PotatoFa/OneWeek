package com.example.jaehun.networkcheck;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jaehun on 16. 3. 22..
 */
public class ApplicationController extends Application {

    NetworkChangeReceiver networkChangeReceiver;
    private static ApplicationController applicationController;
    public static ApplicationController getInstance() {return applicationController;}
    IntentFilter intentFilter;

    Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        this.applicationController = this;
        networkChangeReceiver = null;

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();

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

}
