package whataday.oneweek.CustomCameraActivity;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by jaehun on 16. 3. 16..
 */


public class CameraView extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder mHolder;
    private Camera mCamera;


    String TAG = "CAMERAVIEW ";

    public CameraView(Context context, Camera camera){
        super(context);

        mCamera = camera;
        //get the holder and set this class as the callback, so we can get camera data here
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        Log.i(TAG, "create");

    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if(mHolder.getSurface() == null) //check if the surface is ready to receive camera data

            return;

        try{
            mCamera.stopPreview();
            Log.i(TAG, "change stop");

        } catch (Exception e){
            //this will happen when you are trying the camera if it's not running
        }

        //now, recreate the camera preview
        try{
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            Log.i(TAG, "change start");

        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());
        }

    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        try {
            if (mCamera == null) {
                mCamera = Camera.open();
                Log.i(TAG, "create null");

            }

            if (mCamera != null) {
                Log.i(TAG, "create not null");

                mCamera.setPreviewDisplay(mHolder);

                Camera.Parameters parameters = mCamera.getParameters();
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        //before changing the application orientation, you need to stop the preview, rotate and then start it again
        if(mHolder.getSurface() == null) //check if the surface is ready to receive camera data

            return;

        try{
            mCamera.stopPreview();
            Log.i(TAG, "change stop");

        } catch (Exception e){
            //this will happen when you are trying the camera if it's not running
        }

        //now, recreate the camera preview
        try{
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            Log.i(TAG, "change start");

        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            this.getHolder().removeCallback(this);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
