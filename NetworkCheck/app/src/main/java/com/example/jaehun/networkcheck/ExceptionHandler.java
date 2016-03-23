package com.example.jaehun.networkcheck;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by jaehun on 16. 3. 23..
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {


    private final Activity mContext;

    public ExceptionHandler(Activity context) {
        super();
        mContext = context;

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        Log.i("ERROR : ", ex.toString());
        Log.i("ERROR : ", "SEEET");

    }
}
