package company.twoweeks.twoweeks.Match;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.Controller.ServerInterface;
import company.twoweeks.twoweeks.Database.MatchUser;
import company.twoweeks.twoweeks.CallbackObject.MatchedUser;
import company.twoweeks.twoweeks.CallbackObject.Redirect;
import company.twoweeks.twoweeks.CallbackObject.userList;
import company.twoweeks.twoweeks.R;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hoon on 2015-10-05.
 */
public class MatchFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    MatchAdapter adapter;
    LinearLayoutManager manager;
    SharedPreferences pref;
    ServerInterface api;
    Realm realm;
    String id, gender;

    boolean test = true;

    public static MatchFragment newInstance() {
        MatchFragment fragment = new MatchFragment();
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_match, container, false);

        api = ApplicationController.getInstance().getServerInterface();

        pref = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        realm = ApplicationController.getInstance().getRealm();

        loadUserInfo();

        initView(rootView);

        return rootView;
    }

    private void loadUserInfo(){
        id = pref.getString("id","");
        gender = pref.getString("gender","");

    }

    private void initView(final View rootView){

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_match);


        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new MatchAdapter();

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData();
        setDataList();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout_match);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadData();
                        if (test) {
                            //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                            test = false;
                        } else {
                            //((AppCompatActivity)getActivity()).getSupportActionBar().show();
                            test = true;
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
                /*
                if(test){
                    ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
                    test = false;
                }else{
                    ((AppCompatActivity)getActivity()).getSupportActionBar().show();
                    test = true;
                }
                */
            }
        });


    }

    ArrayList<MatchUser> matchUsers;

    private void setDataList(){

        matchUsers = new ArrayList<MatchUser>();

        RealmResults<MatchUser> result_matUser = realm.where(MatchUser.class).findAll();
        for(MatchUser matchUser : result_matUser) {
            matchUsers.add(matchUser);
        }

        adapter.setSource(matchUsers, getActivity());

    }

    private void loadData(){

        ArrayList<String> matUser = new ArrayList<String>();

        RealmResults<MatchUser> result_matUser = realm.where(MatchUser.class).findAll();
        for(MatchUser matchUser : result_matUser){

            Log.i("matUser", matchUser.getId());

            if(matchUser.getId().equals("empty")||matchUser.getId().equals("searching")){

            }else{
                matUser.add(matchUser.getId());
            }

        }
        dataRedirect(matUser);
    }

    private void dataRedirect(ArrayList<String> matUser){

        api.postRedirect(new Redirect(id, matUser), new Callback<userList>() {
            @Override
            public void success(userList userList, Response response) {


                Log.i("userr", userList.getRedirect());
                //new BackTask().execute("100","100","100");

                if (userList.getRedirect().equals("UserDifferent")) {

                    Log.i("USERDIFFERENT","DIFFERENT");
                    Log.i("Userlist","count : "+String.valueOf(userList.getCount()));
                    Log.i("Userlist","length : "+String.valueOf(userList.getUserList().size()));

                    if(userList.getUserList().size() == 0){
                        //매칭유저 끝났을때 클라 매치유저 초기화
                        RealmResults<MatchUser> results = realm.where(MatchUser.class)
                                .notEqualTo("id", "empty").or()
                                .notEqualTo("id", "searching").findAll();
                        //results.clear();

                        ArrayList<String> removeId = new ArrayList<String>();
                        for(MatchUser removeMatuser : results){
                            removeId.add(removeMatuser.getId());
                        }

                        for(int i = 0; removeId.size()>i; i++){
                            realm.beginTransaction();
                            MatchUser removeUser = realm.where(MatchUser.class).equalTo("id",removeId.get(i)).findFirst();
                            removeUser.removeFromRealm();
                            Log.i("REDIRECT :", "matUser_remove :"+removeId.get(i));
                            MatchUser newMatchUser = realm.createObject(MatchUser.class);
                            newMatchUser.setId("empty");
                            Log.i("REDIRECT :", "create_empty_match");
                            realm.commitTransaction();

                        }
                        Log.i("REDIRECT :", "delete matUser count : "+String.valueOf(results.size()));
                        //id 할당된 DB 찾은다음 clear 후 empty로 재생성
                       /*
                        for(int i = 0; i > results.size(); i++){
                            realm.beginTransaction();
                            MatchUser newMatchUser = realm.createObject(MatchUser.class);
                            newMatchUser.setId("empty");
                            Log.i("REDIRECT :", "create_empty_match");
                            realm.commitTransaction();
                        }
*/
                        Log.i("REDIRECT :", "User_zero_reset");
                        setDataList();
                        Log.i("REDIRECT :", "reset_dataAdapter");
                    }else{
                        //매칭유저가 남았을때
                        Log.i("REDIRECT :", "User_anyone");

                        ArrayList<String> matUserId_server = new ArrayList<String>();
                        for(int i = 0;userList.getUserList().size()>i;i++){
                            matUserId_server.add(userList.getUserList().get(i).getId());
                            Log.i("REDIRECT :", "load_server_matchuser"+userList.getUserList().get(i).getId());
                        }
                        //matUserId_server에 서버리턴 유저정보 로드
                        Log.i("REDIRECT :", "load_server_matchuser_complete");

                        RealmResults<MatchUser> results = realm.where(MatchUser.class).notEqualTo("id","empty")
                                                            .or()
                                                            .notEqualTo("id", "searching")
                                                            .findAll();
                        ArrayList<String> matUserId_client = new ArrayList<String>();
                        ArrayList<String> remove_User = new ArrayList<String>();

                        for(MatchUser matchUser : results){
                            if(matUserId_server.contains(matchUser.getId())){
                                matUserId_client.add(matchUser.getId());
                                Log.i("REDIRECT :", "load_client_contain_true :"+matchUser.getId());
                                //클라에 있는 matchID가 서버에도 존재할때 matUser_client에 추가 - 디비유지
                            }else{
                                Log.i("REDIRECT :", "load_client_contain_false_removeDB :" + matchUser.getId());
                                //클라에 있는데 서버에 matchID가 없을때 - 디비삭제
                                remove_User.add(matchUser.getId());
                                /*TODO check db
                                realm.beginTransaction();
                                MatchUser matchUser_remove = realm.where(MatchUser.class).equalTo("id",matchUser.getId()).findFirst();
                                matchUser_remove.removeFromRealm();
                                //디비 삭제 하고 empty로 생성해주기.
                                MatchUser newMatchUser = realm.createObject(MatchUser.class);
                                newMatchUser.setId("empty");
                                realm.commitTransaction();
*/
                                Log.i("REDIRECT :", "load_client_contain_false_removeDB_Complete_and add empty DB");
                            }
                        }

                        for(int i = 0; remove_User.size()>i; i++){

                            realm.beginTransaction();
                            MatchUser removeUser = realm.where(MatchUser.class).equalTo("id",remove_User.get(i)).findFirst();
                            removeUser.removeFromRealm();
                            MatchUser newEmptyMatch = realm.createObject(MatchUser.class);
                            newEmptyMatch.setId("empty");
                            realm.commitTransaction();

                        }
                        //matUserId_client에 현재 로컬DB 유저정보 로드



                        Log.i("REDIRECT :", "start_update");
                        for(String checkId : matUserId_server){
                            if(matUserId_client.contains(checkId)){
                                //서버에 있고 클라이언트에 있는놈
                                Log.i("REDIRECT :", "containtrue_server / containtrue_client");
                            }else{
                                Log.i("REDIRECT :", "containtrue_server / containfalse_client");
                                //서버에 있고 클라이언트에 없는놈 - DB에 저장

                                Log.i("REDIRECT :", "start save DB");
                                //리턴 유저 배열에서 위치값 알기위한 작업
                                ArrayList<MatchedUser> matchedUsers = userList.getUserList();
                                ArrayList<String> matchedUser_id = new ArrayList<String>();
                                for(MatchedUser matchedUsers_temp : matchedUsers){
                                    matchedUser_id.add(matchedUsers_temp.getId());
                                }

                                Log.i("REDIRECT :", "get return userlist_object_index");
                                int position = matchedUser_id.indexOf(checkId);
                                //리턴 배열에서 클라에 새로 저장할 위치값
                                MatchedUser matchedUser = userList.getUserList().get(position);
                                Log.i("REDIRECT :", "start add DB id:"+matchedUser.getId());
                                realm.beginTransaction();
                                MatchUser matchUser = realm.where(MatchUser.class)
                                        .equalTo("id", "searching")
                                        .or()
                                        .equalTo("id", "empty").findFirst();
                                matchUser.setId(matchedUser.getId());
                                matchUser.setNick(matchedUser.getNick());
                                matchUser.setMatchedUTC(matchedUser.getUtc());
                                matchUser.setAge(matchedUser.getAge());
                                matchUser.setCity(matchedUser.getCity());
                                matchUser.setCountry(matchedUser.getCountry());
                                matchUser.setGender(matchedUser.getGender());

                                realm.commitTransaction();
                                Log.i("REDIRECT :", "complete add DB id:" + matchedUser.getId());
                                //디비에 matchuser 저장 및 메인 도시 이미지 요청
                                setDataList();
                                new BackTask().execute(matchedUser.getCountry(), matchedUser.getCity(), matchedUser.getId());

                            }
                        }
                        //id값 중복 확인해서 클라에 없는놈 삭제 후 서버에 있는놈 추가

                    }

                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.i("userrr ERROR", error.getMessage());
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



    public class BackTask extends AsyncTask<String, String, String> {
        String task_id;
        String cityImageDir;
        String cityImagePath;

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected String doInBackground(String... args) {

            String country = args[0];
            String city = args[1];
            task_id = args[2];

            cityImageDir = Environment.getExternalStorageDirectory().toString()+"/TwoWeeks/.cityImage";
            cityImagePath = cityImageDir+"/"+city+".jpg";
            //타임라인 이미지 저장경로 / 파일경로 불러오기.
            Log.i("Timeline Image DIR :", cityImageDir);
            Log.i("Timeline Image File :", cityImagePath);

            File path = new File(cityImageDir);
            if(!path.exists()){
                path.mkdir();
                Log.i("DIR", "CREATE");
                //저장경로 폴더가 존재하지 않으면 생성.
            }

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

            return "Task Complete";
        }

        @Override
        protected void onPostExecute(String result) {

            realm.beginTransaction();
            MatchUser matchUser = realm.where(MatchUser.class).equalTo("id",task_id).findFirst();
            matchUser.setCityImage(cityImagePath);
            realm.commitTransaction();

            setDataList();
            Log.i("ASYNCTASK","SUCCESS");

        }

    }


}
