package whataday.oneweek.CustomCameraActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import whataday.oneweek.CustomView.BaseActivity;
import whataday.oneweek.R;

public class CameraFragmentActivity extends BaseActivity {

    final static int PICK_IMAGE_REQUEST = 1;
    final static int CROP_IMAGE_REQUEST = 2;


    int view_width;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.squarecamera__CameraFullScreenTheme);

        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_camera_fragment);


        final DisplayMetrics dm = getResources().getDisplayMetrics();
        view_width = dm.widthPixels;

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.custom_fragment_container, CameraFragment.newInstance())
                    .setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                    .commit();

        }
    }


    public void onCancel(View view) {
        if (view == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.custom_fragment_container);
            if (fragment instanceof CameraFragment) {
                finish();
                overridePendingTransition(R.anim.return_bottom, R.anim.remove_top);
            }else if (fragment instanceof EditSaveFragment) {
                getSupportFragmentManager().popBackStack();

            }else if (fragment instanceof SendFragment){
                getSupportFragmentManager().popBackStack();
            }
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        onCancel(null);

    }

    Matrix matrix;
    private void setRotate(int rotate){
        matrix = new Matrix();
        matrix.postRotate(rotate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case PICK_IMAGE_REQUEST: {

                Uri uri = data.getData();
                int orientation = getOrientation(getApplicationContext(), uri);
                Log.d("Orien :", String.valueOf(orientation));
                setRotate(orientation);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    Bitmap rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    bitmap.recycle();

                    Log.d("PATH :", uri.getPath());



                    Log.d("activity result", String.valueOf(bitmap.getByteCount()));

                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setType("image/*");
                    //intent.putExtra("return-data", true);
                    //intent.putExtra("output", uri);
                    intent.putExtra("scale", true);
                    intent.setData(uri);

                    if(rotate_bitmap.getWidth() < rotate_bitmap.getHeight()){
                        intent.putExtra("aspectX", 3);
                        intent.putExtra("aspectY", 4);

                    }else{
                        intent.putExtra("aspectX", 4);
                        intent.putExtra("aspectY", 3);
                    }

                    startActivityForResult(intent, CROP_IMAGE_REQUEST);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case CROP_IMAGE_REQUEST: {
                Uri cropUri = data.getData();
                try {
                    Bitmap cropBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cropUri);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                            .replace(R.id.custom_fragment_container, EditSaveFragment.newInstance(cropBitmap, view_width))
                            .addToBackStack(null).commitAllowingStateLoss();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

}
