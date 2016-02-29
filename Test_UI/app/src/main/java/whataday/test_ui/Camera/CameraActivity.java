package whataday.test_ui.Camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import whataday.test_ui.R;
import whataday.test_ui.SendActivity;


public class CameraActivity extends AppCompatActivity {

    public static final String TAG = CameraActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.squarecamera__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.squarecamera__activity_camera);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance(), CameraFragment.TAG)
                    .commit();
        }
    }

    public void returnPhotoUri(Uri uri) {
        Intent data = new Intent();
        data.setData(uri);

        if (getParent() == null) {
            setResult(RESULT_OK, data);
        } else {
            getParent().setResult(RESULT_OK, data);
        }


        //TODO 사진 촬영 후 저장/센드 액티비티 이동 전

        startActivity(new Intent(getApplicationContext(), SendActivity.class));
        finish();
        //TODO 전환 애니메이션 추가.


    }

    public void onCancel(View view) {
        if (view == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof EditSavePhotoFragment) {
                // Do nothing
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onCancel(null);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.return_bottom, R.anim.remove_top);
    }
}
