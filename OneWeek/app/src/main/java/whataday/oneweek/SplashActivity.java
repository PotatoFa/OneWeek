package whataday.oneweek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.Login.AccountActivity;
import whataday.oneweek.Main.MainPagerActivity;
import whataday.oneweek.Service.NetworkUtil;

public class SplashActivity extends SetFontActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "SPLASH_LOG";

    String SENDER_ID = "243787068094";
    String regid, current_regid;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    GoogleCloudMessaging gcm;
    GoogleApiClient mGoogleApiClient;


    private boolean isLogin_google;
    private boolean isLogin_facebook;
    private boolean isLogin_server;


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
        editor = pref.edit();
        current_regid = pref.getString("gcm_token", null);

        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //기본 프로필을 이메일주소를 통해 가져오겠다는 GSO객체 생성.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)//param1 = 로그인액티비티 생성할 부모액티비티, param2 = 연결실패리스너
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        delay_handler = new Handler(Looper.getMainLooper());
        delay_handler.postDelayed(changeImage, 30);





    }

    Runnable changeImage = new Runnable() {
        @Override
        public void run() {
            if( count > image_resource.length-1 ){
                delay_handler.removeCallbacks(changeImage);
                //startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                //startActivity(new Intent(getApplicationContext(), CustomCameraAcivity.class));
                //startActivity(new Intent(getApplicationContext(), MainPagerActivity.class));
                //startActivity(new Intent(getApplicationContext(), CameraFragmentActivity.class));
                registerInBackground();

            }else{
                image_splash.setImageResource(image_resource[count++]);
                delay_handler.postDelayed(changeImage, 30);
            }
        }
    };


    private void registerInBackground() {

        AsyncTask registerGCM = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                checkLogin();
                Log.i("NETWORK : ", String.valueOf(NetworkUtil.getConnectivityStatus(getApplicationContext())));

                if(NetworkUtil.getConnectivityStatus(getApplicationContext()) == 0){
                    //네트워크 없음. 연결유도
                    Log.i("NETWORK : ", "not connected Network");

                    return null;

                }else{
                    try {
                        regid = gcm.register(SENDER_ID);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return "Registed GCM: " + regid;

                }
            }
            @Override
            protected void onPostExecute(String msg) {
                if(msg == null){
                    //gcm이 null값일때
                    Toast.makeText(getApplicationContext(), "don't regist gcmService", Toast.LENGTH_SHORT).show();
                    //다시시도 유도

                }else{
                    Log.i(TAG, msg);
                    sendRegistrationIdToBackend(regid);

                }


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
            intent = new Intent(getApplicationContext(), AccountActivity.class);
        }


        startActivity(intent);
        finish();

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.

        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void checkLogin(){

        if( AccessToken.getCurrentAccessToken() != null ){
            AccessToken.getCurrentAccessToken().getUserId();
            Log.i("isLogin : ", "Facebook login");
            Log.i("isLogin : ", AccessToken.getCurrentAccessToken().getUserId());

            isLogin_facebook = true;
        }else{
            Log.i("isLogin : ", "Facebook not login");
            isLogin_facebook = false;
        }


        final OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
            @Override
            public void onResult(GoogleSignInResult googleSignInResult) {

                if (opr.isDone()) {
                    Log.i(TAG, "GOOGLE is DOne");
                    isLogin_google = true;

                } else {
                    Log.i(TAG, "GOOGLE is Don t");
                    isLogin_google = false;
                }

                checkedLogin();
            }
        });


    }

    private void checkedLogin(){
        Log.i(TAG, "facebook" + String.valueOf(isLogin_facebook));
        Log.i(TAG, "google" + String.valueOf(isLogin_google));


        if(isLogin_google && isLogin_facebook){

            Google_logout();
            Facebook_logout();

            Log.i(TAG, "Google, Facebook logout");
            //둘다 로그인 되어 있을경우.
            //둘의 로그인 정보를 초기화


        }else if(isLogin_google || isLogin_facebook){
            if(isLogin_google){
                Google_logout();
                Log.i("Sign out", "Google sign out");

            }else if(isLogin_facebook){
                Facebook_logout();
                Log.i("Sign out", "Facebook logOUT");
            }
            //둘중 하나 로그인 되어있을때
        }else if(!isLogin_google && !isLogin_facebook){
            //둘다 로그인 안되어 있을때.
        }
    }

    private void Google_logout(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.i("Sign out", status.toString());

                    }
                });
    }

    private void Facebook_logout(){
        LoginManager.getInstance().logOut();
    }

}
