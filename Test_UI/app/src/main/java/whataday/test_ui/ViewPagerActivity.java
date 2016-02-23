package whataday.test_ui;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import whataday.test_ui.Fragment.PagerAdapter;

public class ViewPagerActivity extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);


    }

}
