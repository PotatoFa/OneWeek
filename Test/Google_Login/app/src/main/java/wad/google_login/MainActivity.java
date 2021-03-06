package wad.google_login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView text_acct;
    Button btn_facebook;
    GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;

    private static final String TAG = "MAIN_LOG";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        text_acct = (TextView)findViewById(R.id.text_acct);

        setGoogle_login();
        setFacebook_login();


    }


    private void setGoogle_login() {

        // https://developers.google.com/identity/sign-in/android/sign-in 레퍼런스 페이지
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        //기본 프로필을 이메일주소를 통해 가져오겠다는 GSO객체 생성.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this) //param1 = 로그인액티비티 생성할 부모액티비티, param2 = 연결실패리스너
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

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
            Log.d(TAG, "ACCT ID : " + acct.getId());
            Log.d(TAG, "ACCT MAIL : " + acct.getEmail());
            Log.d(TAG, "ACCT TOKEN : " + acct.getIdToken());
            Log.d(TAG, "ACCT DISP : " + acct.getDisplayName());

            String temp_acct = "ACCT ID : " + acct.getId()
                    +"\nACCT MAIL : " + acct.getEmail()
                    +"\nACCT TOKEN : " + acct.getIdToken();

            editor.putString("id", acct.getId());
            editor.commit();

            text_acct.setText(temp_acct);

            startActivity(new Intent(getApplicationContext(), JoinActivity.class));

        } else {
            // 요청 및 응답이 실패했을때.

            Log.d(TAG, "handleSignInResult: FALSE action");

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    private void setFacebook_login(){
        btn_facebook = (Button)findViewById(R.id.btn_facebook);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        text_acct.setText("facebook_success");
                        //로그인 제대로 성공했을때 데이터 요청
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.i("GRAPH RESULT ::::: ", response.toString());

                                        try{
                                            String id = (String) response.getJSONObject().get("id");
                                            editor.putString("id", id);
                                            editor.commit();
                                            startActivity(new Intent(getApplicationContext(), JoinActivity.class));
                                        }catch (Exception e){
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
                        text_acct.setText("facebook_cancel");

                    }

                    @Override
                    public void onError(FacebookException error) {
                        text_acct.setText("facebook_fail" + error.toString());

                    }
                });

            }
        });
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


}
