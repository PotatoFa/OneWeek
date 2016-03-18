package whataday.oneweek.CustomCameraActivity;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
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
import java.io.OutputStream;

import whataday.oneweek.R;

public class CustomCameraAcivity extends AppCompatActivity {

    RelativeLayout top_cover, bottom_cover;
    ImageView capture_image_button;

    String TAG = "CAMERA TEST : ";

    int view_width, view_height;

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    int camera_height, bottom_height, top_height, change_height;
    RelativeLayout.LayoutParams bottom_param;

    String Dir_path = Environment.getExternalStorageDirectory().toString()+"/OneWeek";

    private OrientationEventListener mOrientationEventListener;
    private int mOrientation =  -1;

    private static final int ORIENTATION_PORTRAIT_NORMAL =  1;
    private static final int ORIENTATION_PORTRAIT_INVERTED =  2;
    private static final int ORIENTATION_LANDSCAPE_NORMAL =  3;
    private static final int ORIENTATION_LANDSCAPE_INVERTED =  4;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.squarecamera__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        view_width = dm.widthPixels;
        view_height = dm.heightPixels;
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
            for(int i=0; i < value; i++){
                bottom_param.height ++;
                bottom_cover.setLayoutParams(bottom_param);
            }
        }else if(current_height > change_height){
            int value = current_height-change_height;
            for(int i=0; i < value; i++){
                bottom_param.height --;
                bottom_cover.setLayoutParams(bottom_param);
            }
        }
        Log.d("Bottom :",String.valueOf(bottom_param.height));





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
        mOrientationEventListener.disable();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCamera = null;
        setCamera();

        if(mOrientationEventListener == null){
            mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
                @Override
                public void onOrientationChanged(int orientation) {

                    // determine our orientation based on sensor response
                    int lastOrientation = mOrientation;

                    if (orientation >= 315 || orientation < 45) {
                        if (mOrientation != ORIENTATION_PORTRAIT_NORMAL) {
                            mOrientation = ORIENTATION_PORTRAIT_NORMAL;
                        }
                    }
                    else if (orientation < 315 && orientation >= 225) {
                        if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
                            mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
                        }
                    }
                    else if (orientation < 225 && orientation >= 135) {
                        if (mOrientation != ORIENTATION_PORTRAIT_INVERTED) {
                            mOrientation = ORIENTATION_PORTRAIT_INVERTED;
                        }
                    }
                    else { // orientation <135 && orientation > 45
                        if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
                            mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
                        }
                    }

                    if (lastOrientation != mOrientation) {
                        changeRotation(mOrientation, lastOrientation);
                    }
                }
            };
        }

        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }


    private Drawable getRotatedImage(int drawableId, int degrees) {
        Bitmap original = BitmapFactory.decodeResource(getResources(), drawableId);
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        Bitmap rotated = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
        return new BitmapDrawable(rotated);
    }
    public Matrix rotate_matrix;

    private void setRotate_matrix(int rotate){
        rotate_matrix = new Matrix();
        rotate_matrix.postRotate(rotate);
    }

    private void changeRotation(int orientation, int lastOrientation) {
        switch (orientation) {
            case ORIENTATION_PORTRAIT_NORMAL:
                //TODO set button  rotation 270, 0, 90, 180
                //mSnapButton.setImageDrawable(getRotatedImage(android.R.drawable.ic_menu_camera, 270));
                Log.v("CameraActivity", "Orientation = 90");
                setRotate_matrix(90);
                break;
            case ORIENTATION_LANDSCAPE_NORMAL:
                Log.v("CameraActivity", "Orientation = 0");
                setRotate_matrix(0);
                break;
            case ORIENTATION_PORTRAIT_INVERTED:
                Log.v("CameraActivity", "Orientation = 270");
                setRotate_matrix(270);
                break;
            case ORIENTATION_LANDSCAPE_INVERTED:
                Log.v("CameraActivity", "Orientation = 180");
                setRotate_matrix(180);
                break;
        }
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

            try {
                // Populate image metadata

                ContentValues image = new ContentValues();
                // additional picture metadata
                image.put(MediaStore.Images.Media.DISPLAY_NAME, String.format(
                        "/Oneweek/%d.jpg", System.currentTimeMillis()));
                image.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                image.put(MediaStore.Images.Media.TITLE, "OneWeek");

                // do not rotate image, just put rotation info in
                switch (mOrientation) {
                    case ORIENTATION_PORTRAIT_NORMAL:
                        image.put(MediaStore.Images.Media.ORIENTATION, 90);
                        break;
                    case ORIENTATION_LANDSCAPE_NORMAL:
                        image.put(MediaStore.Images.Media.ORIENTATION, 0);
                        break;
                    case ORIENTATION_PORTRAIT_INVERTED:
                        image.put(MediaStore.Images.Media.ORIENTATION, 270);
                        break;
                    case ORIENTATION_LANDSCAPE_INVERTED:
                        image.put(MediaStore.Images.Media.ORIENTATION, 180);
                        break;
                }

                // store the picture
                Uri uri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);

                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    Log.i("bitmap width", String.valueOf(bitmap.getWidth()));
                    Log.i("bitmap heigh", String.valueOf(bitmap.getHeight()));

                    Log.i("bitmap top_height", String.valueOf(top_height));
                    Log.i("bitmap view_height", String.valueOf(view_height));
                    Log.i("bitmap bottom_param", String.valueOf(bottom_param.height));
                    Log.i("bitmap view_width", String.valueOf(view_width));


                    Bitmap bitmap_resize = Bitmap.createBitmap(bitmap,
                            top_height, 0,
                            view_height-(top_height+bottom_param.height), view_width
                            ,rotate_matrix, true);

                    /*
                    Bitmap bitmap_resize = Bitmap.createBitmap(bitmap,
                            top_height, 0,
                            view_height-bottom_param.height, view_width,
                            rotate_matrix, true);
                    Bitmap bitmap_resize = Bitmap.createBitmap(bitmap,
                            0, top_height,
                            view_width, view_height-bottom_param.height,
                            rotate_matrix, true);
                    */
                    Log.i("bitmap width", String.valueOf(bitmap_resize.getWidth()));
                    Log.i("bitmap heigh", String.valueOf(bitmap_resize.getHeight()));

                    OutputStream out = getContentResolver().openOutputStream(
                            uri);
                    boolean success = bitmap_resize.compress(
                            Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                    Log.d("Create Image ", "path:" + uri.getPath());

                    if (!success) {
                        Log.d("Create Image ", "Fail");

                        //finish(); // image output failed without any error,
                        // silently finish
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // handle exceptions
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //finish();

            /*
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
            */

        }
    };


}
