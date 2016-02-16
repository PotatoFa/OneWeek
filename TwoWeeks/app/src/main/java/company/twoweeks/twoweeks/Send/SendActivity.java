package company.twoweeks.twoweeks.Send;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.Controller.GPSTracker;
import company.twoweeks.twoweeks.Controller.ServerInterface;
import company.twoweeks.twoweeks.CallbackObject.Upload;
import company.twoweeks.twoweeks.Database.MatchUser;
import company.twoweeks.twoweeks.Database.UserInfo;
import company.twoweeks.twoweeks.R;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by hoon on 2015-10-05.
 */
public class SendActivity extends AppCompatActivity{

    Button btn_sendimage;
    ListView listView;
    SendAdapter adapter;
    ArrayList<MatchUser> sendDatas;
    GPSTracker gpsTracker;
    ServerInterface api;
    Intent getIntent;
    String file_path, text, lon, lat;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        getIntent = getIntent();
        api = ApplicationController.getInstance().getServerInterface();
        gpsTracker = ApplicationController.getInstance().getGpsTracker();

        sendDatas = new ArrayList<MatchUser>();
        realm = ApplicationController.getInstance().getRealm();

        getIntentData();

        initView();

    }


    private void getIntentData(){
        file_path = getIntent.getStringExtra("img_path");
        text = getIntent.getStringExtra("text");
        lon = String.valueOf(gpsTracker.getLongitude());
        lat = String.valueOf(gpsTracker.getLatitude());
        Toast.makeText(getApplicationContext(), file_path+" / "+text+" / "+lon+" / "+lat, Toast.LENGTH_SHORT).show();
        Log.i("SendData", "FILE_PATH : "+file_path+"\n TEXT : "+text+"\n LONGITUDE : "+lon+"\n LATITUDE : "+lat);

    }

    ArrayList<String> toUser;


    private void initView(){
        toUser = new ArrayList<String>();

        listView = (ListView) findViewById(R.id.listview_send);
        btn_sendimage = (Button)findViewById(R.id.btn_sendimage);

        RealmResults<MatchUser> realmResults = realm.where(MatchUser.class).findAll();

        for(MatchUser user : realmResults){
            if(user.getId().equals("empty") || user.getId().equals("searching")){

            }else{
                sendDatas.add(user);
            }
        }

        adapter = new SendAdapter(SendActivity.this , R.layout.item_send, sendDatas);

        listView.setAdapter(adapter);

        //position에 따라서 체크변수하나 할당해서 선택한 애들 선별해야함.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (view.findViewById(R.id.senditem_cover).getVisibility() == View.INVISIBLE) {
                    view.findViewById(R.id.senditem_cover).setVisibility(View.VISIBLE);

                    if(toUser.contains(sendDatas.get(position).getId())){
                        toUser.remove(sendDatas.get(position).getId());
                    }

                } else {
                    toUser.add(sendDatas.get(position).getId());
                    view.findViewById(R.id.senditem_cover).setVisibility(View.INVISIBLE);
                }
            }
        });

        btn_sendimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UserInfo userInfo = realm.where(UserInfo.class).findFirst();

                //TODO repositioning AsyncTask
                api.postFileUpload(new TypedString(userInfo.getId()), toUser,
                        new TypedString(text), new TypedString(lat), new TypedString(lon),
                        new TypedFile("image/jpeg", new File(file_path)), new Callback<Upload>() {
                            @Override
                            public void success(Upload upload, Response response) {
                                Log.i("::UPLOAD::", upload.getUpload());
                                Log.i("::FileName::", upload.getFileName());
                                //save photo, DB

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                error.printStackTrace();

                            }
                        });

                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //finish();
                //post fileupload

            }
        });



    }



}
