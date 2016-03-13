package whataday.oneweek.Main;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import io.realm.Realm;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.Data.ImageData;
import whataday.oneweek.Data.MatchedUser;
import whataday.oneweek.R;

public class MainPagerActivity extends SetFontActivity {

    ViewPager viewPager_main;
    Toolbar toolbar_main;
    MainPagerAdapter mainPagerAdapter;
    ImageView toolbar_camera_icon;

    Realm realm;


    String[] strings = {"Hello world", "Lucky day", "My job",
            "Merry christmassdgsdgsdgs", "Wet tissue shitshitshit", "Good morning",
            "Shtttttt", "Hello world", "LuckyLucky dayday", "My job"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pager);
        viewPager_main = (ViewPager)findViewById(R.id.viewpager_main);

        realm = ApplicationController.getRealm();
        setTestData();

        setToolbar();
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager_main.setAdapter(mainPagerAdapter);
        viewPager_main.setCurrentItem(1);

    }


    private void setToolbar(){
        toolbar_main = (Toolbar)findViewById(R.id.toolbar_main);
        toolbar_camera_icon = (ImageView)findViewById(R.id.toolbar_camera_icon);
        setSupportActionBar(toolbar_main);
        setTitle("OneWeek");
        toolbar_main.setTitleTextColor(Color.WHITE);
    }

    public void setVisibleCamera(boolean visible){
        if(visible){
            toolbar_camera_icon.setVisibility(View.VISIBLE);
        }else{
            toolbar_camera_icon.setVisibility(View.INVISIBLE);
        }
    }


    private void setTestData(){
        realm.beginTransaction();
        realm.clear(MatchedUser.class);
        realm.clear(ImageData.class);
        for(int i = 0; i < 3; i++){
            MatchedUser matchedUser = realm.createObject(MatchedUser.class);
            matchedUser.setId("1");
            matchedUser.setCity("SEOUL");
            matchedUser.setCountry("South korea");
            if(i == 1){
                matchedUser.setId("2");
                matchedUser.setCity("Osaka");
                matchedUser.setCountry("Japan");
            }else if(i == 2){
                matchedUser.setId("empty");
            }


            for(int j = 0; j < 10; j++){
                ImageData imageData = realm.createObject(ImageData.class);
                imageData.setFromUserId(matchedUser.getId());
                imageData.setText(strings[j]);
            }

        }

        realm.commitTransaction();
        Log.i("REALM","CREATED TEST DATA");

    }


}
