package whataday.oneweek.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;
import com.viewpagerindicator.CirclePageIndicator;

import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.Data.UserInfo;
import whataday.oneweek.R;

public class JoinActivity extends SetFontActivity {

    CustomViewPager viewpager_join;
    CirclePageIndicator viewpager_indicator;
    JoinPagerAdapter joinPagerAdapter;
    Toolbar toolbar_join;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        pref = getSharedPreferences("user", MODE_PRIVATE);
        editor = pref.edit();

        setIndicator();

        toolbar_join = (Toolbar)findViewById(R.id.toolbar_join);
        setSupportActionBar(toolbar_join);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);


    }

    private void setIndicator(){

        viewpager_join = (CustomViewPager) findViewById(R.id.viewpager_join);
        viewpager_indicator = (CirclePageIndicator) findViewById(R.id.viewpager_indicator);
        joinPagerAdapter = new JoinPagerAdapter(getSupportFragmentManager());
        viewpager_join.setAdapter(joinPagerAdapter);
        viewpager_indicator.setViewPager(viewpager_join);
        viewpager_join.setScrollEnabled(false);
        viewpager_join.setScrollDuration(700);
        viewpager_indicator.setGap(17);
        viewpager_indicator.setFillColor(Color.parseColor("#ffffff"));
        viewpager_indicator.setPageColor(Color.parseColor("#28ffffff"));
        viewpager_indicator.setStrokeWidth(0);
        viewpager_indicator.setRadius(10);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(viewpager_join.getCurrentItem() != 0){
                    viewpager_join.setCurrentItem(viewpager_join.getCurrentItem()-1, true);
                }else{
                    Toast.makeText(getApplicationContext(), "not enabled", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return false;
    }



}
