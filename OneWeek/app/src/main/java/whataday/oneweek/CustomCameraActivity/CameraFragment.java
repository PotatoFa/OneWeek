package whataday.oneweek.CustomCameraActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import whataday.oneweek.Camera.EditSavePhotoFragment;
import whataday.oneweek.R;

/**
 * Created by jaehun on 16. 3. 18..
 */
public class CameraFragment extends android.support.v4.app.Fragment {

    View rootView;

    boolean frontCameraEnabled, backCameraEnabled;

    int cameraId_back;
    int cameraId_front;
    int cameraId_current;

    RelativeLayout top_cover, bottom_cover;
    LinearLayout flash;
    ImageView capture_image_button, change_camera, flash_icon, gallery;
    TextView text_flash;

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




    public static CameraFragment newInstance() {
        CameraFragment cameraFragment = new CameraFragment();
        return cameraFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout)inflater.inflate(R.layout.camera_fragment_camera, container, false);
        this.rootView = rootView;
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCameraId();

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        view_width = dm.widthPixels;
        view_height = dm.heightPixels;
        camera_height = view_width * 4/3;
        top_height = (int)getResources().getDimension(R.dimen.dp_56dp);
        bottom_height = (int)getResources().getDimension(R.dimen.dp_300dp);

        Log.i(TAG, "size " + view_width + "/" + camera_height + "/" + top_height + "/" + bottom_height);

        change_height = dm.heightPixels - (camera_height + top_height);


        top_cover = (RelativeLayout) rootView.findViewById(R.id.top_cover);
        bottom_cover = (RelativeLayout) rootView.findViewById(R.id.bottom_cover);
        capture_image_button = (ImageView) rootView.findViewById(R.id.capture_image_button);
        change_camera = (ImageView) rootView.findViewById(R.id.change_camera);
        gallery = (ImageView) rootView.findViewById(R.id.gallery);
        flash_icon = (ImageView) rootView.findViewById(R.id.flash_icon);
        flash = (LinearLayout) rootView.findViewById(R.id.flash);
        text_flash = (TextView) rootView.findViewById(R.id.text_flash);


        change_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cameraId_current == cameraId_back){
                    if(frontCameraEnabled){
                    cameraId_current = cameraId_front;
                    }
                }else if(cameraId_current == cameraId_front){
                    if(backCameraEnabled) {
                        cameraId_current = cameraId_back;
                    }
                }
                Log.i("CURRENT CAMERA :", String.valueOf(cameraId_current));

                mCamera.release();
                mCamera = null;
                mCamera = Camera.open(cameraId_current);

                mCameraView.setCamera(mCamera);

            }
        });

        capture_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO 설정한 카메라 오토포커싱기능 체크 후 일반촬영/포커싱촬영

                if(mCamera.getParameters().getSupportedFocusModes().contains(
                        Camera.Parameters.FOCUS_MODE_AUTO)){
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                takePicture();
                            }
                        }
                    });
                }else{
                    takePicture();
                }


            }
        });


        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cameraId_current == 0){
                    if(mCameraView.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)){
                        mCameraView.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        text_flash.setText("On");
                    }else if(mCameraView.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)){
                        mCameraView.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        text_flash.setText("Off");
                    }
                    Log.i("FLASH MODE :", mCameraView.getFlashMode());
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        ((CameraFragmentActivity)getActivity()).PICK_IMAGE_REQUEST);
            }
        });


        resizeBottomCover(bottom_height);

    }

    Matrix matrixFront;

    float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1};
    Matrix matrixMirrorY = new Matrix();

    private void setCameraId(){
        int numberOfCameras = Camera.getNumberOfCameras();
        for(int i = 0; i < numberOfCameras; i++){
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                cameraId_front = i;
                frontCameraEnabled = true;
                matrixMirrorY.setValues(mirrorY);

            }else if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                cameraId_back = i;
                backCameraEnabled = true;
            }

            if(backCameraEnabled){
                cameraId_current = cameraId_back;
            }

        }

        Log.i("FRONT CAMERA :", String.valueOf(cameraId_front));
        Log.i("BACK CAMERA :", String.valueOf(cameraId_back));
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
    private void setCamera(int i){

        try{
            mCamera = Camera.open(i);//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null) {
            mCameraView = new CameraView(getActivity(), mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout)rootView.findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout


        }
    }



    @Override
    public void onPause() {
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
        setCamera(cameraId_current);

        if(mOrientationEventListener == null){
            mOrientationEventListener = new OrientationEventListener(getActivity(), SensorManager.SENSOR_DELAY_NORMAL) {
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

        matrixFront = new Matrix();
        matrixFront.postConcat(matrixMirrorY);
        matrixFront.postRotate(rotate);

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


            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                    data.length);


            Log.i("bitmap width", String.valueOf(bitmap.getWidth()));
            Log.i("bitmap heigh", String.valueOf(bitmap.getHeight()));


            Log.i("bitmap top_height", String.valueOf(top_height));
            Log.i("bitmap view_height", String.valueOf(view_height));
            Log.i("bitmap bottom_param", String.valueOf(bottom_param.height));
            Log.i("bitmap view_width", String.valueOf(view_width));




            int bitmap_top_cover =
                    convert_int_map(top_height, 0, view_height, 0, bitmap.getWidth());

            Log.i("bitmap top cover :", String.valueOf(bitmap_top_cover));


            int bitmap_bottom_cover =
                    convert_int_map(bottom_param.height, 0, view_height, 0, bitmap.getWidth());

            Log.i("bitmap bottom cover :", String.valueOf(bitmap_bottom_cover));

            int bitmap_resize_width =
                    bitmap.getWidth() - (bitmap_top_cover + bitmap_bottom_cover);

            int bitmap_resize_height =
                    bitmap_resize_width * 3 / 4;

            int crop_height = bitmap.getHeight() - bitmap_resize_height;
            int crop_start_height = crop_height/2;
            int crop_stop_height = bitmap.getHeight() - crop_start_height;
            Log.i("crop_start_height :", String.valueOf(crop_start_height)+ " / "+ String.valueOf(crop_stop_height));


            Log.i("resize size :", String.valueOf(bitmap_resize_width) + "/" + String.valueOf(bitmap_resize_height));

            Bitmap bitmap_resize;

            if(cameraId_current != cameraId_front){
                bitmap_resize = Bitmap.createBitmap(bitmap,
                        bitmap_top_cover, crop_height/2,
                        bitmap.getWidth() - (bitmap_top_cover + bitmap_bottom_cover), bitmap_resize_height
                        , rotate_matrix, true);
            }else{
                bitmap_resize = Bitmap.createBitmap(bitmap,
                        bitmap_top_cover, crop_height/2,
                        bitmap.getWidth() - (bitmap_top_cover + bitmap_bottom_cover), bitmap_resize_height
                        , matrixFront, true);
            }

            ((CameraFragmentActivity)getActivity()).bitmap = bitmap_resize;

            bitmap.recycle();

            Log.i("resize width :", String.valueOf(bitmap_resize.getWidth()));

            Log.i("resize height :", String.valueOf(bitmap_resize.getHeight()));
            bitmap_resize.recycle();


            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                    .replace(
                            R.id.custom_fragment_container,
                            EditSaveFragment.newInstance(view_width))
                    .addToBackStack(null)
                    .commit();

        }
    };


    int convert_int_map(int input, int input_min, int input_max, int convert_min, int convert_max){
        return ( ((input - input_min) * (convert_max - convert_min)) / (input_max - input_min) ) + convert_min;
    }


}