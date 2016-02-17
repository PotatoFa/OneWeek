package wad.google_login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SPLASH_LOG";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String SENDER_ID = "243787068094";
    //구글 콘솔홈페이지에서 등록한 프로젝트 번호
    GoogleCloudMessaging gcm;
    String regid, current_regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = getSharedPreferences("user", MODE_PRIVATE);
        editor = pref.edit();
        current_regid = pref.getString("gcm_token", null);

        //TODO pref/DB에 저장된 regid값이 발급받은 id값과 동일한지 검사.

        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        //GCM 객체 불러온 후 GCM서비스의 디바이스 token값을 요청.
        registerInBackground();

    }

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
            Log.i(TAG, "same reg_id");
        }else{
            //등록받은 token값이 일치하지 않는 경우. pref/서버에 갱신 요청.
            editor.putString("gcm_token", regid);
            editor.commit();
            //TODO 이슈. 가끔 바뀌는 경우가 있음. 기존 유저의 경우 서버에 변경요청 해야함.
        }
        //TODO 이전 로그인 기록 검사 후 Main 또는 Login 화면 전환.
        //이전 로그인 기록 검사를 서버에 실시간으로 해야하는지??
        //pref - user - history 필드 사용

    }

}
