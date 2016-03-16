package whataday.oneweek.CustomCameraActivity;

import android.hardware.Camera;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import whataday.oneweek.R;

public class CustomCameraAcivity extends AppCompatActivity {

    RelativeLayout top_cover, bottom_cover;
    ImageView capture_image_button;

    String TAG = "CAMERA TEST : ";

    int view_width;

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    int camera_height, bottom_height, top_height, change_height;
    RelativeLayout.LayoutParams bottom_param;

    String Dir_path = Environment.getExternalStorageDirectory().toString()+"/OneWeek";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.squarecamera__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        view_width = dm.widthPixels;
        camera_height = view_width * 4/3;
        top_height = (int)getResources().getDimension(R.dimen.dp_56dp);
        bottom_height = (int)getResources().getDimension(R.dimen.dp_300dp);

        Log.i(TAG, "size " + view_width + "/" + camera_height+ "/" + top_height+ "/" + bottom_height);

        change_height = dm.heightPixels - (camera_height + top_height);

        setContentView(R.layout.activity_custom_camera_acivity);

        top_cover = (RelativeLayout) findViewById(R.id.top_cover);
        bottom_cover = (RelativeLayout) findViewById(R.id.bottom_cover);
        capture_image_button = (ImageView) findViewById(R.id.capture_image_button);

        capture_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        //setCamera();

        resizeBottomCover(bottom_height);

    }



    private void takePicture() {
        mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    private void resizeBottomCover(int current_height){

        bottom_param = new RelativeLayout.LayoutParams(view_width, current_height);
        bottom_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        if(current_height < change_height){
            int value = change_height-current_height;
            for(int i=1; i < value; i++){
                bottom_param.height ++;
                bottom_cover.setLayoutParams(bottom_param);
            }
        }else if(current_height > change_height){
            int value = current_height-change_height;
            for(int i=1; i < value; i++){
                bottom_param.height --;
                bottom_cover.setLayoutParams(bottom_param);
            }
        }




    }
    private void setCamera(){

        try{
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout

        }
    }



    @Override
    protected void onPause() {
            super.onPause();
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCameraView.getHolder().removeCallback(mCameraView);
                mCamera.release();
            }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCamera = null;
        setCamera();
    }



    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File path = new File(Dir_path);
            if(!path.exists()){
                path.mkdir();
                Log.i("DIR", "CREATE");
                Log.i("DIR PATH", Dir_path);
                //저장경로 폴더가 존재하지 않으면 생성.
            }

            File image_file = new File(Dir_path+String.format(
                    "/%d.jpg", System.currentTimeMillis()));

            if(!image_file.exists()){
                try {
                    FileOutputStream outStream = new FileOutputStream(image_file);
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Log.d("Log", "onPictureTaken - jpeg");
            }

        }
    };


}
