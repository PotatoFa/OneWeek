package whataday.test_ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;

import com.nirhart.parallaxscroll.views.ParallaxListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ParallaxListView parallaxListView;
    ArrayList<ListData> arrdatas;
    ListAdapter adapter;
    int focus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(getApplicationContext(), ScrollviewActivity.class));

        arrdatas = new ArrayList<ListData>();

        for(int i = 1; i < 4; i++){
            ListData listData = new ListData(i*222222);
            arrdatas.add(listData);
        }
        adapter = new ListAdapter(getApplicationContext(), R.layout.item_list, arrdatas);

        parallaxListView = (ParallaxListView) findViewById(R.id.parallaxlistview);


        parallaxListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        parallaxListView.setAdapter(adapter);


    }
}
