package whataday.test_ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.nirhart.parallaxscroll.views.ParallaxListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(getApplicationContext(), PullActivity.class));

        //startActivity(new Intent(getApplicationContext(), ViewPagerActivity.class));
        //startActivity(new Intent(getApplicationContext(), ScrollviewActivity.class));

    }
}
