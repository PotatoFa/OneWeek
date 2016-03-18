package whataday.oneweek.CustomCameraActivity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import whataday.oneweek.R;

public class CameraFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.squarecamera__CameraFullScreenTheme);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_camera_fragment);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.custom_fragment_container, CameraFragment.newInstance())
                    .commit();
        }
    }


    public void onCancel(View view) {
        if (view == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.custom_fragment_container);
            if (fragment instanceof EditSaveFragment) {
                // Do nothing
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.return_bottom, R.anim.remove_top);
    }

}
