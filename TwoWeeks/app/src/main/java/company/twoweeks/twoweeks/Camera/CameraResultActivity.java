package company.twoweeks.twoweeks.Camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
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

public class CameraResultActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;

    ServerInterface api;
    ApplicationController applicationController;
    AutoResizeEditText editText;
    Button btn_editFocus, btn_select;
    boolean keyboard_status = false;
    private InputMethodManager inputMethodManager;
    private Point mSize;
    Intent intent;
    String file_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_result);

        applicationController = ApplicationController.getInstance();
        api = applicationController.getServerInterface();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Display display = getWindowManager().getDefaultDisplay();
        mSize = new Point();
        display.getSize(mSize);

        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);

        initView();

    }


    private void initView(){

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        editText = (AutoResizeEditText) findViewById(R.id.camera_text);
        btn_editFocus = (Button) findViewById(R.id.button_editfocus);
        btn_select = (Button) findViewById(R.id.button_select);

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


                Intent intent = new Intent(getApplicationContext(), SendActivity.class);
                intent.putExtra("text", editText.getText().toString());
                intent.putExtra("img_path", file_path);
                startActivity(intent);
            }
        });
    }

    File photoFile;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            Uri photoUri = data.getData();
            // Get the bitmap in according to the width of the device
            Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), mSize.x, mSize.x);
            file_path = photoUri.getPath();
            photoFile = new File(file_path);
            ((ImageView) findViewById(R.id.camera_image)).setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        if (keyboard_status == true) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            keyboard_status = false;
        } else {
            super.onBackPressed();
        }
        //push backbutton
    }

}
