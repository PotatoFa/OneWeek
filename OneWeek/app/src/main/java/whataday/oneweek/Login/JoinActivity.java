package whataday.oneweek.Login;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import whataday.oneweek.R;

public class JoinActivity extends AppCompatActivity {

    ViewPager viewpager_join;
    JoinPagerAdapter joinPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        viewpager_join = (ViewPager) findViewById(R.id.viewpager_join);
        joinPagerAdapter = new JoinPagerAdapter(getSupportFragmentManager());
        viewpager_join.setAdapter(joinPagerAdapter);

    }
}
