package com.example.jaehun.networkcheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;

public class CheckActivity extends AppCompatActivity {

    String TAG = "LOG";

    GPUImage mGPUImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void change(View view){

    }

}
