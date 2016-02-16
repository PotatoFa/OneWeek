package company.twoweeks.twoweeks.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import company.twoweeks.twoweeks.CallbackObject.Regi;
import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.Controller.GPSTracker;
import company.twoweeks.twoweeks.Controller.ServerInterface;
import company.twoweeks.twoweeks.CallbackObject.LoginStatus;
import company.twoweeks.twoweeks.CallbackObject.MapAddress;
import company.twoweeks.twoweeks.Database.MatchUser;
import company.twoweeks.twoweeks.CallbackObject.User;
import company.twoweeks.twoweeks.Database.UserInfo;
import company.twoweeks.twoweeks.MainActivity;
import company.twoweeks.twoweeks.R;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class JoinActivity extends AppCompatActivity {
    ServerInterface api;
    EditText edit_join_nickname;
    TextInputLayout textinputlayout_join_nickname;
    Button btn_join_join, btn_join_checkednickname, btn_join_datepicker;
    GPSTracker gpsTracker;
    SharedPreferences pref, login_status;
    SharedPreferences.Editor login_status_edit;
    Realm realm;

    GoogleCloudMessaging googleCloudMessaging;
    String token;

    boolean nickCheck = false;

    TextView text_year;
    TextView test_text;
    String lon, lat, id, gender, age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        registerInBackground();

        loadData();

        initView();

    }


    String SENDER_ID = "243787068094";
    private void registerInBackground(){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {

                try {
                    if(googleCloudMessaging==null){
                        googleCloudMessaging = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    token = googleCloudMessaging.register(SENDER_ID);
                    Log.i("GCM TOKEN ::", token);


                }catch (IOException e){
                    e.printStackTrace();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.i("GCM TOKEN ::", "Regitered Success");
            }
        }.execute(null, null, null);
    }

    private void loadData(){
        pref = getSharedPreferences("user_info", MODE_PRIVATE);
        login_status = getSharedPreferences("login_status", MODE_PRIVATE);
        login_status_edit = login_status.edit();

        api = ApplicationController.getInstance().getServerInterface();
        gpsTracker = ApplicationController.getInstance().getGpsTracker();
        realm = ApplicationController.getInstance().getRealm();

        lon = String.valueOf(gpsTracker.getLongitude());
        lat = String.valueOf(gpsTracker.getLatitude());
        id = pref.getString("id", "");
        gender = pref.getString("gender", "");
        checkedData(lat, lon, id, gender);

    }

    private void init_matUser(){

        realm.beginTransaction();
        realm.clear(MatchUser.class);
        realm.commitTransaction();

        realm.beginTransaction();
        realm.where(MatchUser.class).findAll().clear();

        MatchUser matchUser1 = realm.createObject(MatchUser.class);
        matchUser1.setId("empty");

        MatchUser matchUser2 = realm.createObject(MatchUser.class);
        matchUser2.setId("empty");

        MatchUser matchUser3 = realm.createObject(MatchUser.class);
        matchUser3.setId("empty");

        realm.commitTransaction();

    }

    private void createUserDB(String id, String nick, String gender,
                              String lat, String lon, String city, String country,
                              int age, String token, int count){

        realm.beginTransaction();
        realm.where(UserInfo.class).findAll().clear();
        UserInfo userInfo = realm.createObject(UserInfo.class);

        userInfo.setId(id);
        userInfo.setNick(nick);
        userInfo.setGender(gender);
        userInfo.setAge(age);
        userInfo.setLatitude(lat);
        userInfo.setLongitude(lon);
        userInfo.setCity(city);
        userInfo.setCountry(country);
        userInfo.setToken(token);
        userInfo.setCount(count);

        realm.commitTransaction();
    }

    String city, country;

    private void initView(){

        text_year = (TextView)findViewById(R.id.text_year);
        textinputlayout_join_nickname = (TextInputLayout)findViewById(R.id.textinputlayout_join_nickname);
        edit_join_nickname = (EditText)findViewById(R.id.edit_join_nickname);
        btn_join_join = (Button)findViewById(R.id.btn_join_join);
        btn_join_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                age = parseAge(text_year.getText().toString());

                if(checkedAge(age)){

                    if(nickCheck){

                        User user = new User(id, gender, age, edit_join_nickname.getText().toString(), lat, lon, token);
                        init_matUser();

                        api.getMapAddress(lat, lon, new Callback<MapAddress>() {
                            @Override
                            public void success(MapAddress mapAddress, Response response) {
                                city = mapAddress.getMap_city();
                                country = mapAddress.getMap_country();

                                createUserDB(id, edit_join_nickname.getText().toString(),
                                        gender, lat, lon, city, country, Integer.parseInt(age), token, 0);
                                Log.i("USER CREATE::", "USER "+city+country);

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("USER CREATE ERROR::", error.getMessage());

                            }
                        });

                        login_status_edit.putBoolean("join_token", true);
                        login_status_edit.commit();

                        //TODO testing direct mainActivity, delete under code
                        //startActivity(new Intent(getApplicationContext(), GcmRegisterActivity.class));
                        //finish();

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                        api.postRegister(user, new Callback<Regi>() {
                            @Override
                            public void success(Regi regi, Response response) {
                                Log.i("REGI:::", regi.getRegi());

                                if(regi.getRegi().equals("regi_success")){

                                    init_matUser();

                                    api.getMapAddress(lat, lon, new Callback<MapAddress>() {
                                        @Override
                                        public void success(MapAddress mapAddress, Response response) {
                                            city = mapAddress.getMap_city();
                                            country = mapAddress.getMap_country();

                                            createUserDB(id, edit_join_nickname.getText().toString(),
                                                    gender, lat, lon, city, country, Integer.parseInt(age), token, 0);
                                            Log.i("USER CREATE::", "USER "+city+country);

                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.i("USER CREATE ERROR::", error.getMessage());

                                        }
                                    });

                                    login_status_edit.putBoolean("join_token", true);
                                    login_status_edit.commit();
                                    startActivity(new Intent(getApplicationContext(), GcmRegisterActivity.class));
                                    finish();

                                }


                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("ERROR REGI :::", error.getMessage());

                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(), "Checked input nick-name", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });


        btn_join_checkednickname = (Button)findViewById(R.id.btn_join_checkednickname);
        btn_join_checkednickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedNickname();
            }
        });

        test_text = (TextView)findViewById(R.id.test_text);
        test_data = "lon : "+lon+"\nlat : "+lat+"\nid : "+id+"\ngender : " + gender+"\nbirthday : "+age+"\ntoken : "+token;
        test_text.setText(test_data);

        btn_join_datepicker = (Button)findViewById(R.id.btn_join_datepicker);
        btn_join_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }
    String test_data;
    private void checkedData(String mlat, String mlon, String mid,String mgender){


        if(mlat != null && mlon != null && mid != null && mgender != null){
            if(mgender.equals("male")){
                gender = "m";
            }else{
                gender = "g";
            }
            Toast.makeText(getApplicationContext(), "Notting Problem", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Data have Problem", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkedAge(String age){
        if(age != null){
            return true;
        }else{
            Toast.makeText(getApplicationContext(), "Please Select your birthday", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void checkedNickname(){
        api.getNickCheck(id, edit_join_nickname.getText().toString(), new Callback<LoginStatus>() {
            @Override
            public void success(LoginStatus loginStatus, Response response) {
                Log.i("NICKCHECK ::", loginStatus.getNick_search());
                if(loginStatus.getNick_search().equals("search_unexist")){
                    textinputlayout_join_nickname.setHint("Registered possible nickname");
                    nickCheck = true;
                }else if(loginStatus.getNick_search().equals("search_exist")){
                    textinputlayout_join_nickname.setHint("Already exists. input another nickname");
                    nickCheck = false;
                }else if(loginStatus.getNick_search().equals("search_err_fail")){
                    textinputlayout_join_nickname.setHint("Request failed. Please try Again");
                    nickCheck = false;
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.i("NICKCHECK ERROR" , error.getMessage());
                textinputlayout_join_nickname.setHint("Request Error. please checked setting");

            }
        });
    }

    private String parseAge(String birthday){
        // dd/mm/yyyy 타입 받아서 현재 미국기준 년도로 빼서 리턴.
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String var = dateFormat.format(date);

        int birth_year = Integer.parseInt(birthday.substring(6,10));
        int current = Integer.parseInt(var);
        int age = current-birth_year;

        String return_value = String.valueOf(age);

        return return_value;
    }


}
