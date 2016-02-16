package company.twoweeks.twoweeks.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.MainActivity;
import company.twoweeks.twoweeks.R;

public class LoginActivity extends AppCompatActivity {

    ApplicationController applicationController;
    SharedPreferences pref, login_status;
    SharedPreferences.Editor editor;
    Button btn_login_facebook;
    CallbackManager callbackManager;
    String id = "";
    String name = "";
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        applicationController = ApplicationController.getInstance();
        pref = getSharedPreferences("user_info", MODE_PRIVATE);

        login_status = getSharedPreferences("login_status", MODE_PRIVATE);

        editor = pref.edit();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

//        if (AccessToken.getCurrentAccessToken() != null && login_status.getBoolean("join_token", false)){
        if (login_status.getBoolean("join_token", false)){

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            Log.i("ACC", AccessToken.getCurrentAccessToken().toString());
            finish();

        }
        btn_login_facebook = (Button)findViewById(R.id.btn_login_facebook);
        btn_login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            Log.i("GRAPH RESULT ::::: ", response.toString());
                                            id = (String) response.getJSONObject().get("id");
                                            name = (String) response.getJSONObject().get("name");
                                            gender = (String) response.getJSONObject().get("gender");
                                            editor.putString("id", id);
                                            editor.putString("name", name);
                                            editor.putString("gender", gender);
                                            editor.commit();

                                            applicationController.setLoginManager(LoginManager.getInstance());
                                            startActivity(new Intent(getApplicationContext(), JoinActivity.class));
                                            finish();

                                        } catch (JSONException e) {
                                            Log.i("GRAPH ERROR :::: ", e.getMessage());
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
                        Toast.makeText(getApplicationContext(), "Login - Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {

                        if (error instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Login - Error -" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("LOGIN ERROR :::::: ", error.getMessage());
                    }
                });
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
