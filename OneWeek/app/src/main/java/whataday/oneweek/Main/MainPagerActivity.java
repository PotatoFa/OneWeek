package whataday.oneweek.Main;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import whataday.oneweek.R;

public class MainPagerActivity extends AppCompatActivity {

    ViewPager viewPager_main;
    Toolbar toolbar_main;
    MainPagerAdapter mainPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pager);
        viewPager_main = (ViewPager)findViewById(R.id.viewpager_main);

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

}
