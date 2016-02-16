package company.twoweeks.twoweeks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.Controller.ServerInterface;

public class ImageActivity extends AppCompatActivity {

    Button btn_imgtest, btn_imgtest2;
    ImageView imageView;
    ServerInterface api;
    String folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        api = ApplicationController.getInstance().getServerInterface();

        btn_imgtest = (Button) findViewById(R.id.btn_imgtest);
        btn_imgtest2 = (Button) findViewById(R.id.btn_imgtest2);
        imageView = (ImageView) findViewById(R.id.image_test);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        folder = Environment.getExternalStorageDirectory().toString();


        File path = new File(folder+"/TwoWeeks");
        if(!path.exists()){
            path.mkdir();
            Log.i("TRY", "mkdir");
        }

        btn_imgtest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(
                        Environment.getExternalStorageDirectory().toString()+"/TwoWeeks/image.jpg"));
            }
        });


        btn_imgtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Bitmap bitmap = imageView.getDrawingCache();
                OutputStream outputStream = null;

                File file = new File(folder+"/TwoWeeks/image.jpg");

                try{
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i("TRY", "try");

                    //MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), "TwoWeeks", "timeline");
                    //MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());


                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });


    }


}
