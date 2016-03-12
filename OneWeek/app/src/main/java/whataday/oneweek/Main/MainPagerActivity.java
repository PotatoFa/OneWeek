package whataday.oneweek.Main;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import io.realm.Realm;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.Data.MatchedUser;
import whataday.oneweek.R;

public class MainPagerActivity extends SetFontActivity {

    ViewPager viewPager_main;
    Toolbar toolbar_main;
    MainPagerAdapter mainPagerAdapter;

    Realm realm;

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
        setSupportActionBar(toolbar_main);
        setTitle("OneWeek");
        toolbar_main.setTitleTextColor(Color.WHITE);
        toolbar_main.setBackgroundColor(Color.parseColor("#33000000"));
    }

    private void setTestData(){
        realm.beginTransaction();
        realm.clear(MatchedUser.class);
        for(int i = 0; i < 3; i++){
            MatchedUser matchedUser = realm.createObject(MatchedUser.class);
            matchedUser.setId("empty");
        }
        realm.commitTransaction();
        Log.i("REALM","CREATED TEST DATA");
    }

}
