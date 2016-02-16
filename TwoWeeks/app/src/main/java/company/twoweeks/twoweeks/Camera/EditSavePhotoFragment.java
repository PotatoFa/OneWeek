package company.twoweeks.twoweeks.Camera;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import company.twoweeks.twoweeks.AutoSizeText.AutoResizeEditText;
import company.twoweeks.twoweeks.CallbackObject.Regi;
import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.Controller.ServerInterface;
import company.twoweeks.twoweeks.R;
import company.twoweeks.twoweeks.Send.SendActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


/**
 *
 */
public class EditSavePhotoFragment extends Fragment {

    public static final String TAG = EditSavePhotoFragment.class.getSimpleName();
    public static final String BITMAP_KEY = "bitmap_byte_array";
    public static final String ROTATION_KEY = "rotation";
    public static final String IMAGE_INFO = "image_info";

    private ServerInterface api;
    private File photoFile;
    private ApplicationController applicationController;
    private AutoResizeEditText editText;
    private Button btn_editFocus, btn_select;
    boolean keyboard_status = false;
    private InputMethodManager inputMethodManager;
    private String file_path;
    private Point mSize;

    public static Fragment newInstance(byte[] bitmapByteArray, int rotation,
                                       @NonNull ImageParameters parameters) {
        Fragment fragment = new EditSavePhotoFragment();

        Bundle args = new Bundle();
        args.putByteArray(BITMAP_KEY, bitmapByteArray);
        args.putInt(ROTATION_KEY, rotation);
        args.putParcelable(IMAGE_INFO, parameters);

        fragment.setArguments(args);
        return fragment;
    }

    public EditSavePhotoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.squarecamera__fragment_edit_save_photo, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        applicationController = ApplicationController.getInstance();
        api = applicationController.getServerInterface();

        initView();

        int rotation = getArguments().getInt(ROTATION_KEY);
        byte[] data = getArguments().getByteArray(BITMAP_KEY);
        ImageParameters imageParameters = getArguments().getParcelable(IMAGE_INFO);

        if (imageParameters == null) {
            return;
        }

        final ImageView photoImageView = (ImageView) view.findViewById(R.id.photo);

        imageParameters.mIsPortrait =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        final View topView = view.findViewById(R.id.topView);
        if (imageParameters.mIsPortrait) {
            topView.getLayoutParams().height = imageParameters.mCoverHeight;
        } else {
            topView.getLayoutParams().width = imageParameters.mCoverWidth;
        }

        rotatePicture(rotation, data, photoImageView);
    }

    private void rotatePicture(int rotation, byte[] data, ImageView photoImageView) {
        Bitmap bitmap = ImageUtility.decodeSampledBitmapFromByte(getActivity(), data);
//        Log.d(TAG, "original bitmap width " + bitmap.getWidth() + " height " + bitmap.getHeight());
        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            bitmap = Bitmap.createBitmap(
                    oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
            );

            oldBitmap.recycle();
        }

        photoImageView.setImageBitmap(bitmap);
    }

    private void savePicture() {
        View view = getView();
        if (view == null) return;

        ImageView photoImageView = (ImageView) view.findViewById(R.id.photo);

        Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
        Uri photoUri = ImageUtility.savePicture(getActivity(), bitmap);

        // Get the bitmap in according to the width of the device
        bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), mSize.x, mSize.x);
        file_path = photoUri.getPath();
        photoFile = new File(file_path);

        ((CameraActivity) getActivity()).returnPhotoUri(photoUri);
    }




    private void initView(){
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        mSize = new Point();
        display.getSize(mSize);

        inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        editText = (AutoResizeEditText) getActivity().findViewById(R.id.camera_text);
        btn_editFocus = (Button) getActivity().findViewById(R.id.button_editfocus);
        btn_select = (Button) getActivity().findViewById(R.id.button_select);

        editText.bringToFront();

        btn_editFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyboard_status == true) {
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    keyboard_status = false;
                } else {
                    inputMethodManager.showSoftInput(editText, 0);
                    keyboard_status = true;
                }
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture();

                Intent intent = new Intent(getActivity().getApplicationContext(), SendActivity.class);
                intent.putExtra("text", editText.getText().toString());
                intent.putExtra("img_path", file_path);
                getActivity().startActivity(intent);

            }
        });


    }
}
