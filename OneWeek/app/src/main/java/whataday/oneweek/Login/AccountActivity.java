package whataday.oneweek.Login;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONObject;

import java.util.Arrays;

import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.CustomView.ViewAnimation;
import whataday.oneweek.R;

public class AccountActivity extends SetFontActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;

    Button btn_login_facebook, btn_login_google, btn_assign_exit, btn_assign_agree;
    RelativeLayout box_assign, box_login_button;
    ImageView background_image;


    TextView text_asign;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "ACCOUNT_LOG";

    private boolean isLogin_google;
    private boolean isLogin_facebook;
    private boolean isLogin_server;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        // https://developers.google.com/identity/sign-in/android/sign-in 레퍼런스 페이지
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //기본 프로필을 이메일주소를 통해 가져오겠다는 GSO객체 생성.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this) //param1 = 로그인액티비티 생성할 부모액티비티, param2 = 연결실패리스너
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        getIsServerLogin();


        initView();


        //TODO 로그인 과정에서 분기




    }


    private void initView(){

        background_image = (ImageView)findViewById(R.id.background_image);

        text_asign = (TextView)findViewById(R.id.text_asign);

        box_assign = (RelativeLayout)findViewById(R.id.box_assign);
        box_login_button = (RelativeLayout)findViewById(R.id.box_login_button);

        btn_login_facebook = (Button)findViewById(R.id.btn_login_facebook);
        btn_login_google = (Button)findViewById(R.id.btn_login_google);
        btn_assign_exit = (Button)findViewById(R.id.btn_assign_exit);
        btn_assign_agree = (Button)findViewById(R.id.btn_assign_agree);


        setFacebook_login();
        setGoogle_Login();



        text_asign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TermsActivity.class));
                overridePendingTransition(R.anim.down_top, R.anim.top_down);
            }
        });


        btn_assign_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Log.i("TEET", status.toString());
                            }
                        });
            }
        });

        btn_assign_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAnimation.slideToBottom(box_assign);
                ViewAnimation.alphaIn(box_login_button, 1000);
                ViewAnimation.alphaOut(background_image, 500);
            }
        });

    }

    private void setFacebook_login(){

        btn_login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(AccountActivity.this, Arrays.asList("public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //로그인 제대로 성공했을때 데이터 요청
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.i("GRAPH RESULT ::::: ", response.toString());

                                        try {
                                            String id = (String) response.getJSONObject().get("id");
                                            Log.i(TAG, "FacebookID:" + id.toString());
                                            startActivity(new Intent(getApplicationContext(), JoinActivity.class));
                                            finish();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.i(TAG, "Facebook Cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {

                        Log.i(TAG, "Facebook Error :" + error.toString());
                    }
                });
            }
        });

        setIsFacebookLogin();
    }
    private void setGoogle_Login(){
        btn_login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        setIsGoogleLogin();
    }



    private void checkedLogin(){
        Log.i(TAG, "Check Login");
        Log.i(TAG, "facebook" + String.valueOf(isLogin_facebook));
        Log.i(TAG, "google" + String.valueOf(isLogin_google));


        if(isLogin_google && isLogin_facebook){

            LoginManager.getInstance().logOut();
            Log.i(TAG, "LOGOUT");
            //둘다 로그인 되어 있을경우.
            //둘의 로그인 정보를 초기화


        }else if(isLogin_google || isLogin_facebook){
            //둘중 하나 로그인 되어있을때
        }else if(!isLogin_google && !isLogin_facebook){
            //둘다 로그인 안되어 있을때.
        }
    }

    private boolean getIsServerLogin(){
        isLogin_server = true;
        return true;
    }
    private void setIsFacebookLogin(){
        if( AccessToken.getCurrentAccessToken() != null ){
            Log.i("isLogin : ", "Facebook true");
            isLogin_facebook = true;
        }else{
            Log.i("isLogin : ", "Facebook false");
            isLogin_facebook = false;
        }
    }

    private void setIsGoogleLogin(){

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if(opr.isDone()){

            GoogleSignInResult result = opr.get();

            handleSignInResult(result);

        }
/*
        opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
            @Override
            public void onResult(GoogleSignInResult googleSignInResult) {
                if (googleSignInResult.isSuccess()) {
                    Log.i(TAG, "isSuccess True");
                    handleSignInResult(googleSignInResult);

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {

                                }
                            });


                } else {
                    Log.i(TAG, "isSuccess False");
                }


            }
        });
        */


        /*
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.i("isLogin : ", "Google true");
            GoogleSignInResult result = opr.get();

            isLogin_google = true;

            //handleSignInResult(result);

            return true;

            //handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    Log.d(TAG, "DON't");
                    //handleSignInResult(googleSignInResult);
                }
            });

            Log.i("isLogin : ", "Google false");

            isLogin_google = false;


            return false;
        }
        */
    }


    @Override
    public void onStart() {
        super.onStart();

    }




    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Log.d(TAG, "sign in SUCCESS");
            // 요청 및 응답이 성공적으로 이루어졌을때. DB저장/UI업데이트 등
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, "ACCT ID TOKEN : " + acct.getIdToken());
            Log.d(TAG, "ACCT ID : " + acct.getId());
            Log.d(TAG, "ACCT MAIL : " + acct.getEmail());
            Log.d(TAG, "ACCT TOKEN : " + acct.getIdToken());
            Log.d(TAG, "ACCT DISP : " + acct.getDisplayName());

            //startActivity(new Intent(getApplicationContext(), JoinActivity.class));
            //finish();

        } else {

            Toast.makeText(getApplicationContext(), "Failed login, checked network", Toast.LENGTH_SHORT).show();
            Log.d(TAG, result.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            //리턴받은 requestCode가 일치하면 프로필 데이터 요청
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}
