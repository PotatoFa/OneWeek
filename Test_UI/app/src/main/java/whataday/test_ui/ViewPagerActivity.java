package whataday.test_ui;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import whataday.test_ui.Fragment.PagerAdapter;

public class ViewPagerActivity extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    Toolbar toolbar_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        initToolbar();

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

    }

    private void initToolbar(){
        toolbar_main = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar_main);
        setTitle("OneWeek");
        toolbar_main.setTitleTextColor(Color.WHITE);
        toolbar_main.setBackgroundColor(Color.parseColor("#33000000"));
    }

}
