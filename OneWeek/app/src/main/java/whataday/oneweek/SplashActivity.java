package whataday.oneweek;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.Login.AccountActivity;
import whataday.oneweek.Service.GPSTracker;

public class SplashActivity extends SetFontActivity {

    private static final String TAG = "SPLASH_LOG";

    String SENDER_ID = "243787068094";
    GoogleCloudMessaging gcm;
    String regid, current_regid;
    GPSTracker gpsTracker;

    Handler delay_handler;
    ImageView image_splash;
    int count = 0;
    int image_resource[] = { R.drawable.splash1, R.drawable.splash2, R.drawable.splash3 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        image_splash = (ImageView)findViewById(R.id.image_splash);

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
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
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
        if(regid.equals(current_regid)){
            //등록받은 token값이 일치할 경우.
        }else{
            //등록받은 token값이 일치하지 않는 경우. pref/서버에 갱신 요청.
            //TODO 이슈. 가끔 바뀌는 경우가 있음. 기존 유저의 경우 서버에 변경요청 해야함.
        }
        //TODO 이전 로그인 기록 검사 후 Main 또는 Login 화면 전환.
        //이전 로그인 기록 검사를 서버에 실시간으로 해야하는지??
        //pref - user - history 필드 사용

    }
}
