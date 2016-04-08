package com.example.jaehun.networkcheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageTestActivity extends AppCompatActivity {

    @OnClick(R.id.back)
    public void back(){
        startActivity(new Intent(getApplicationContext(), ImageActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_test);
        ButterKnife.bind(this);
    }
}
