package com.example.jaehun.networkcheck;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

public class TintActivity extends AppCompatActivity {

    ImageView testImage, image;
    SeekBar red, green, blue, aa;


    int[] colors;
    private static final int REQUEST_PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tint);
        testImage = (ImageView) findViewById(R.id.test_image);
        image = (ImageView) findViewById(R.id.image);
        red = (SeekBar) findViewById(R.id.red);
        green = (SeekBar) findViewById(R.id.green);
        blue = (SeekBar) findViewById(R.id.blue);
        aa = (SeekBar) findViewById(R.id.aa);

        aa.setProgress(255);
        red.setProgress(255);
        green.setProgress(255);
        blue.setProgress(255);

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
        aa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

    public void changeTint(View view){

        Log.i("COLOR : ", String.valueOf(colors[i]));
        if(i <= colors.length){
            testImage.setColorFilter(colors[i], PorterDuff.Mode.OVERLAY);
            i++;
        }else{
            i = 0;
        }
    }

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

    public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }


}
