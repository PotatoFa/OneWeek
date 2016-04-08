package com.example.jaehun.networkcheck;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.jaehun.networkcheck.Data.Image;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmObject;

public class ImageActivity extends AppCompatActivity {

    Realm realm;
    Bitmap bitmap;
    Byte[] bytes;

    @OnClick(R.id.incoding)
    public void incoding(){
        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.splash1)).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] binary = stream.toByteArray();

        realm.beginTransaction();
        Image image = realm.createObject(Image.class);
        image.setBinary(binary);
        realm.commitTransaction();
        Log.i("Image Test : ", "Byte Length " + binary.length);
        binary = null;

        try {
            stream.close();
            Log.i("Image Test : ", "Stream close ");
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i("Image Test : ", "Realm length : " + realm.where(Image.class).findFirst().getBinary().length);


    }

    @OnClick(R.id.recycle)
    public void recycle(){

        imageView.setImageBitmap(null);
        bmp.recycle();
        //recycleBitmap(imageView);
        Log.i("Image Test : ", "recycle");

    }

    Bitmap bmp;

    @OnClick(R.id.setimage)
    public void setimage(){

        Log.i("Image Test : ", "setImage");
        byte[] byteArray = realm.where(Image.class).findFirst().getBinary();
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Log.i("Image Test : ", String.valueOf(byteArray.length));
        imageView.setImageBitmap(bmp);


    }

    @OnClick(R.id.change)
    public void change(){

        startActivity(new Intent(getApplicationContext(), ImageTestActivity.class));
        finish();

    }

    @Bind(R.id.image) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();


    }

    private static void recycleBitmap(ImageView iv) {
        Drawable d = iv.getDrawable();
        if (d instanceof BitmapDrawable) {
            Log.i("Image Test : ", "re");

            Bitmap b = ((BitmapDrawable)d).getBitmap();
            b = null;
        } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.

        d.setCallback(null);
    }

    @Override
    protected void onDestroy() {
        recycleBitmap(imageView);

        super.onDestroy();

    }
}
