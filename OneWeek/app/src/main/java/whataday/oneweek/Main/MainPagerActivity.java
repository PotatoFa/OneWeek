package whataday.oneweek.Main;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import butterknife.Bind;
import io.realm.Realm;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomView.BaseActivity;
import whataday.oneweek.Data.ImageData;
import whataday.oneweek.Data.MatchedUser;
import whataday.oneweek.Login.CustomViewPager;
import whataday.oneweek.R;

public class MainPagerActivity extends BaseActivity {

    MainPagerAdapter mainPagerAdapter;
    Realm realm;


    String[] strings = {"Hello world", "Lucky day", "My job",
            "Merry christmassdgsdgsdgs", "Wet tissue shitshitshit", "Good morning",
            "Shtttttt", "Hello world", "LuckyLucky dayday", "My job"};

    @Bind(R.id.viewpager_main) CustomViewPager viewpager_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pager);

        realm = ApplicationController.getRealm();
        setTestData();

        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewpager_main.setAdapter(mainPagerAdapter);
        viewpager_main.setScrollDuration(500);
        viewpager_main.setCurrentItem(1);

    }


    public void changeViewPager(int i){
        viewpager_main.setCurrentItem(i);
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
