package whataday.oneweek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import io.realm.Realm;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.Login.AccountActivity;
import whataday.oneweek.Login.JoinActivity;
import whataday.oneweek.Main.MainPagerActivity;
import whataday.oneweek.Service.GPSTracker;

public class SplashActivity extends SetFontActivity {

    private static final String TAG = "SPLASH_LOG";

    String SENDER_ID = "243787068094";
    GoogleCloudMessaging gcm;
    String regid, current_regid;
    GPSTracker gpsTracker;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    Intent intent;

    Handler delay_handler;
    ImageView image_splash;
    int count = 0;
    int image_resource[] = { R.drawable.splash1, R.drawable.splash2, R.drawable.splash3 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        image_splash = (ImageView)findViewById(R.id.image_splash);

        pref = getSharedPreferences("user", MODE_PRIVATE);
        current_regid = pref.getString("gcm_token", null);
        editor = pref.edit();


        gpsTracker = ApplicationController.getGpsTracker();
        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        registerInBackground();

        delay_handler = new Handler(Looper.getMainLooper());
        delay_handler.postDelayed(changeImage, 30);

    }

    Runnable changeImage = new Runnable() {
        @Override
        public void run() {
            if( count > image_resource.length-1 ){
                delay_handler.removeCallbacks(changeImage);
                //startActivity(intent);
                startActivity(new Intent(getApplicationContext(), JoinActivity.class));

                finish();
            }else{
                image_splash.setImageResource(image_resource[count++]);
                delay_handler.postDelayed(changeImage, 30);
            }
        }
    };


    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    regid = gcm.register(SENDER_ID);
                    sendRegistrationIdToBackend(regid);
                } catch (IOException ex) {
                }
                return "Registed GCM: " + regid;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend(String regid) {

        if(pref.getBoolean("login_flag", false)){
            //로그인 했던 사람
            if(!current_regid.equals(regid)){
                //로그인 했던사람인데 token바꼈을때
                //TODO 서버, pref에 변경요청
                editor.putString("gcm_token", regid);
                editor.commit();
            }
            //MainPagerActivity로 보내야함
            intent = new Intent(getApplicationContext(), MainPagerActivity.class);

        }else{
            //로그인 경험 없는 사람
            editor.putString("gcm_token", regid);
            editor.commit();
            intent = new Intent(getApplicationContext(), JoinActivity.class);

        }

    }


}
