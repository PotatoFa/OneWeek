package whataday.oneweek.Main;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import io.realm.Realm;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.CustomView.ViewAnimation;
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

        viewPager_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("pagechange ", "scrolled "+position);
                changeToolbar(position);
            }
            @Override
            public void onPageSelected(int position) {
                Log.i("pagechange ", "selected "+position);

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("pagechange ", "state "+state);
            }
        });
    }
    private void changeToolbar(int i){
        if(i == 0){
            if(toolbar_camera_icon.getVisibility() == View.VISIBLE){
                ViewAnimation.alphaInvisible(toolbar_camera_icon, 300);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                ViewAnimation.alphaIn(toolbar_menu_back, 300);
            }
        }else{
            if(toolbar_camera_icon.getVisibility() == View.INVISIBLE){
                ViewAnimation.alphaOut(toolbar_menu_back, 100);
                ViewAnimation.alphaIn(toolbar_camera_icon, 300);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            }
        }

    }

    ImageButton toolbar_menu_back;

    private void setToolbar(){
        toolbar_main = (Toolbar)findViewById(R.id.toolbar_main);
        toolbar_camera_icon = (ImageView)findViewById(R.id.toolbar_camera_icon);
        toolbar_menu_back = (ImageButton)findViewById(R.id.toolbar_menu_back);
        toolbar_menu_back.setVisibility(View.GONE);
        setSupportActionBar(toolbar_main);
        setTitle("OneWeek");
        toolbar_main.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                viewPager_main.setCurrentItem(0, true);

                break;
        }
        return false;
    }



}
