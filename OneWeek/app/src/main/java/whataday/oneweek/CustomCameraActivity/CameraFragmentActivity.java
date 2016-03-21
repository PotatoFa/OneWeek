package whataday.oneweek.CustomCameraActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.R;

public class CameraFragmentActivity extends SetFontActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.squarecamera__CameraFullScreenTheme);

        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_camera_fragment);

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

}
