package com.example.jaehun.networkcheck;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by jaehun on 16. 3. 22..
 */
public class SetFontActiviry extends AppCompatActivity{

    @Override
    protected void onPause() {
        ApplicationController.getInstance().finishReceiver();
        super.onPause();
    }

    @Override
    protected void onResume() {
        ApplicationController.getInstance().startReceiver();
        super.onResume();
    }
}
