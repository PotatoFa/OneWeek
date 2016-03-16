package whataday.oneweek.CustomCameraActivity;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraViewAppActivity extends ActionBarActivity {

    private Preview mPreview;


    class Preview extends SurfaceView implements SurfaceHolder.Callback {
        Camera mCamera;
        SurfaceHolder mHolder;

        public void close() {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }

        public Preview(Context context) {
            super(context);

            if (mCamera == null) {
                mCamera = Camera.open();
            }

            mHolder = getHolder();
            mHolder.addCallback(this);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(width, height);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            try {
                if (mCamera == null) {
                    mCamera = Camera.open();
                }

                if (mCamera != null) {
                    mCamera.setPreviewDisplay(holder);

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
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreview = new Preview(this);

        setContentView(mPreview);
    }



}