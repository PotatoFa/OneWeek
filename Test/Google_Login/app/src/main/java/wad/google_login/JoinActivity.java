package wad.google_login;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class JoinActivity extends AppCompatActivity {

    ApplicationController applicationController;
    ServerInterface api;
    GPSTracker gpsTracker;
    SharedPreferences sharedPreferences;
    String user_id;
    Button btn_logout, btn_login, btn_location;
    TextView text_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getString("id", "error");

        applicationController = ApplicationController.getInstance();
        applicationController.buildServerInterface("ec2-54-92-43-111.ap-northeast-1.compute.amazonaws.com");

        api = applicationController.getServerInterface();
        gpsTracker = applicationController.getGpsTracker();

        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_location = (Button)findViewById(R.id.btn_location);
        text_location = (TextView)findViewById(R.id.text_location);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.get_login(user_id, "0123456789", new Callback<Return>() {
                    @Override
                    public void success(Return aReturn, Response response) {
                        Log.i("LOGIN : ", aReturn.getId_status());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("LOGIN : ", "FAIL");
                        error.printStackTrace();
                    }
                });
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    api.get_logout(user_id, new Callback<Return>() {
                        @Override
                        public void success(Return aReturn, Response response) {
                            Log.i("LOGOUT : ", aReturn.getLogoutStatus());
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.i("LOGOUT : ", "FAIL");
                            error.printStackTrace();
                        }
                    });
            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_load();
            }
        });

    }

    private void location_load(){
        if(gpsTracker.canGetLocation()){
            String temp_text = "Latitude : " + gpsTracker.getLatitude()
                    +"\nLongitude : " + gpsTracker.getLongitude()
                    +"\nUsing Provider : " + gpsTracker.getUsingProvider();
            text_location.setText(temp_text);
        }else{
            text_location.setText("can't get Location \n"+gpsTracker.canGetLocation());
            //TODO showing dialog GPS ON
        }
    }
}
