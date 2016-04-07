package com.example.jaehun.networkcheck;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class TintActivity extends AppCompatActivity {

    ImageView testImage, image;
    SeekBar red, green, blue, aa;


    int filter_red, filter_green, filter_blue;


    int thumbnail_size;


    int[] colors;
    private static final int REQUEST_PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tint);

        filter = (LinearLayout) findViewById(R.id.filter);
        testImage = (ImageView) findViewById(R.id.test_image);
        image = (ImageView) findViewById(R.id.image);

        image.setDrawingCacheEnabled(true);
        testImage.setDrawingCacheEnabled(true);
        //red = (SeekBar) findViewById(R.id.red);
        //green = (SeekBar) findViewById(R.id.green);
        //blue = (SeekBar) findViewById(R.id.blue);
        aa = (SeekBar) findViewById(R.id.aa);

        aa.setProgress(255);
        //red.setProgress(255);
        //green.setProgress(255);
        //blue.setProgress(255);
/*
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //convert_color = getIntFromColor(red.getProgress(), green.getProgress(), blue.getProgress());
                convert_color = android.graphics.Color.argb(aa.getProgress(), red.getProgress(), green.getProgress(), blue.getProgress());
                testImage.setColorFilter(convert_color, PorterDuff.Mode.OVERLAY);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                convert_color = android.graphics.Color.argb(aa.getProgress(), red.getProgress(), green.getProgress(), blue.getProgress());
                testImage.setColorFilter(convert_color, PorterDuff.Mode.OVERLAY);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                convert_color = android.graphics.Color.argb(aa.getProgress(), red.getProgress(), green.getProgress(), blue.getProgress());
                testImage.setColorFilter(convert_color, PorterDuff.Mode.OVERLAY);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        */
        aa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                convert_color = android.graphics.Color.argb(aa.getProgress(), filter_red, filter_green, filter_blue);
                testImage.setColorFilter(convert_color, PorterDuff.Mode.SCREEN);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        thumbnail_size = (int) getResources().getDimension(R.dimen.thumbnail);

        TypedArray ta = getResources().obtainTypedArray(R.array.filter_color_array);

        colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
    }

    int convert_color;

    int i = 0;

    public void changeTint(View view) {


        testImage.setDrawingCacheEnabled(true);
        testImage.buildDrawingCache(true);
        Bitmap bmap = Bitmap.createBitmap(testImage.getDrawingCache());
        testImage.setDrawingCacheEnabled(false);

        image.setImageBitmap(bmap);
    }


    private void setColorFilter(int targetColor){

        filter_red = Color.red(targetColor);
        filter_green = Color.green(targetColor);
        filter_blue = Color.blue(targetColor);

        convert_color = android.graphics.Color.argb(aa.getProgress(), filter_red, filter_green, filter_blue);

        testImage.setColorFilter(convert_color, PorterDuff.Mode.SCREEN);

    }

    Bitmap thumb_image;
    LinearLayout filter;


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try{
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                        testImage.setImageBitmap(bitmap);
                        image.setImageBitmap(bitmap);
                        thumb_image = Bitmap.createScaledBitmap(bitmap, thumbnail_size, thumbnail_size, true);

                        setFilter_thumbnail();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    finish();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }




    private void setFilter_thumbnail(){
        LinearLayout.LayoutParams thumbnail_param = new LinearLayout.LayoutParams(thumbnail_size,
                thumbnail_size);
        thumbnail_param.setMargins(5, 5, 5, 5);

        for(int i = 0 ; i < colors.length; i++){
            ImageView thumbnail = new ImageView(getApplicationContext());

            thumbnail.setLayoutParams(thumbnail_param);

            thumbnail.setImageBitmap(thumb_image);

            thumbnail.setColorFilter(colors[i], PorterDuff.Mode.SCREEN);

            final int color = colors[i];

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setColorFilter(color);
                }
            });

            filter.addView(thumbnail);
        }
    }



}
