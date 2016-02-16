package company.twoweeks.twoweeks.TimeLine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import company.twoweeks.twoweeks.CallbackObject.FileList;
import company.twoweeks.twoweeks.CallbackObject.Img;
import company.twoweeks.twoweeks.CallbackObject.return_FileList;
import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.Controller.ServerInterface;
import company.twoweeks.twoweeks.Database.ImageInfo;
import company.twoweeks.twoweeks.Database.UserInfo;
import company.twoweeks.twoweeks.R;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TimelineActivity extends AppCompatActivity {

    Intent intent;
    String matchId, country, city, myId;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ImageInfo> timelineDatas;
    TimelineAdapter adapter;
    Realm realm;
    ServerInterface api;

    LinearLayoutManager manager;
    RecyclerView recyclerView;
    SharedPreferences pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        realm = ApplicationController.getInstance().getRealm();
        api = ApplicationController.getInstance().getServerInterface();

        pref = getSharedPreferences("user_info", MODE_PRIVATE);
        intent = getIntent();
        country = intent.getStringExtra("Country");
        city = intent.getStringExtra("City");
        matchId = intent.getStringExtra("match_id");
        myId = pref.getString("id", "");

        initview();

        api.getFilelist(myId, matchId, "empty", new Callback<return_FileList>() {
            @Override
            public void success(return_FileList return_fileList, Response response) {
                Log.i("FILELIST::",return_fileList.getList());
                if(return_fileList.getList().equals("success")){
                    for(int i=0; i<return_fileList.getFileList().size(); i++){

                        FileList fileList = return_fileList.getFileList().get(i);
                        realm.beginTransaction();
                        ImageInfo imageInfo = realm.createObject(ImageInfo.class);
                        imageInfo.setFileName(fileList.getFilename());
                        imageInfo.setFromUserId(fileList.getFromUserId());
                        imageInfo.setToUserId(fileList.getToUserId());
                        imageInfo.setFromLTC(fileList.getFromLTC());
                        imageInfo.setToLTC(fileList.getToLTC());
                        imageInfo.setUTC(fileList.getUTC());
                        //TODO lat/lon empty callback
                        realm.commitTransaction();
                        new DownloadTask().execute(imageInfo.getFileName(), matchId);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void setData(){

        timelineDatas = new ArrayList<ImageInfo>();

        RealmResults<ImageInfo> results_imageInfo = realm.where(ImageInfo.class)
                                                    .equalTo("toUserId", matchId).or()
                                                    .equalTo("fromUserId", matchId).findAll();

        for (ImageInfo imageInfo : results_imageInfo){
            timelineDatas.add(imageInfo);
        }

        adapter.setSource(timelineDatas);

    }

    private void initview(){

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipelayout_timeline);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_timeline);
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new TimelineAdapter();

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(country+" / "+city);


        setData();



/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/


    }

    public class DownloadTask extends AsyncTask<String, String, String>{
        String fileName, matchId;
        String TimelineImageDir, TimelineImagePath;
        File image_file;

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected String doInBackground(String... args) {

            fileName = args[0];
            matchId = args[1];
            TimelineImageDir = Environment.getExternalStorageDirectory().toString()+"/TwoWeeks/."+matchId;
            File path = new File(TimelineImageDir);
            if(!path.exists()){
                path.mkdir();
                Log.i("TIMELINE DIR", "CREATE");
            }
            TimelineImagePath = TimelineImageDir+"/"+fileName+".jpg";
            image_file = new File(TimelineImagePath);

            if(!image_file.exists()){
                api.getDownload(fileName, new Callback<Img>() {
                    @Override
                    public void success(Img img, Response response) {
                        Log.i("FILE DOWNLOAD :", "callback success");
                        byte[] buf = img.getImg();
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(buf);
                        try {
                            Log.i("FILE DOWNLOAD :", "start");
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            int len;
                            while ((len = inputStream.read(buf, 0, 15000)) != -1){
                                byteArrayOutputStream.write(buf, 0, len);
                            }
                            buf = byteArrayOutputStream.toByteArray();

                            FileOutputStream fileOutputStream = new FileOutputStream(image_file);
                            fileOutputStream.write(buf);
                            fileOutputStream.close();
                            Log.i("Image File Write :", "Complete");

                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

                //TODO timeline image download and save
            }
            /*

            File image_file = new File(cityImagePath);

            if(!image_file.exists()){
                //같은 이름의 도시 사진이 존재하지 않을떄.
                Response res = api.getTimeline(country, city);
                try{
                    InputStream inputStream = res.getBody().in();
                    Log.i("FILE DOWNLOAD :", "start");

                    byte[] buf = new byte[15000];
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int len;
                    while ((len = inputStream.read(buf, 0, 15000)) != -1){
                        byteArrayOutputStream.write(buf, 0, len);
                    }
                    buf = byteArrayOutputStream.toByteArray();

                    FileOutputStream fileOutputStream = new FileOutputStream(image_file);
                    fileOutputStream.write(buf);
                    fileOutputStream.close();
                    Log.i("Image File Write :", "Complete");

                }catch (IOException e){
                    e.printStackTrace();
                }
            }else{
                Log.i("Image File :", "Already image_file :"+cityImagePath);
            }
            */

            return "Task Complete";
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }



}
