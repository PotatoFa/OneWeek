package whataday.oneweek.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.viewpagerindicator.CirclePageIndicator;

import whataday.oneweek.R;

public class JoinActivity extends AppCompatActivity {

    CustomViewPager viewpager_join;
    CirclePageIndicator viewpager_indicator;
    JoinPagerAdapter joinPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        viewpager_join = (CustomViewPager) findViewById(R.id.viewpager_join);
        viewpager_indicator = (CirclePageIndicator) findViewById(R.id.viewpager_indicator);
        joinPagerAdapter = new JoinPagerAdapter(getSupportFragmentManager());
        viewpager_join.setAdapter(joinPagerAdapter);
        viewpager_indicator.setViewPager(viewpager_join);
        viewpager_join.setScrollEnabled(false);
        viewpager_join.setScrollDuration(700);


    }

    public void nextPage(){
        viewpager_join.setCurrentItem(viewpager_join.getCurrentItem() + 1);
    }

    public void prePage() { viewpager_join.setCurrentItem(viewpager_join.getCurrentItem() - 1);}

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(viewpager_join.getCurrentItem() == 0) {
            finish();
        }else{
            prePage();
        }
    }
}
