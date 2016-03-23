package com.example.jaehun.networkcheck;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends SetFontActiviry {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

    }

    public void unregist(View view){
        ApplicationController.getInstance().finishReceiver();
    }

    public void regist(View view){
        ApplicationController.getInstance().startReceiver();
    }

}
