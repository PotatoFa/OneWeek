package whataday.oneweek.Match;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import io.realm.Realm;
import io.realm.RealmResults;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomCameraActivity.CameraFragmentActivity;
import whataday.oneweek.CustomView.BaseActivity;
import whataday.oneweek.CustomView.ViewAnimation;
import whataday.oneweek.Data.ImageData;
import whataday.oneweek.Data.MatchedUser;
import whataday.oneweek.R;

public class DetailActivity extends BaseActivity {

    Intent intent;
    Realm realm;
    String matchId;
    MatchedUser matchedUser;

    int scroll_y = 0;


    @Bind(R.id.detail_refresh) PtrFrameLayout detail_refresh;
    @Bind(R.id.toolbar_detail) Toolbar toolbar_detail;
    @Bind(R.id.detail_toolbar_camera_icon) ImageView detail_toolbar_camera_icon;
    @Bind(R.id.text_location) TextView text_location;

    @Bind(R.id.recyclerview_detail) RecyclerView recyclerview_detail;

    LinearLayoutManager manager;
    DetailRecyclerAdapter adapter;

    ArrayList<ImageData> imageDatas = new ArrayList<ImageData>();
    RealmResults<ImageData> results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        realm = ApplicationController.getRealm();
        intent = getIntent();

        matchId = intent.getStringExtra("matchId");

        setData();

        setToolbar();

        initView();

    }



    private void initView(){
        setDetail_refresh();
        setRecyclerview_detail();

    }

    private void setRecyclerview_detail(){
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new DetailRecyclerAdapter();

        recyclerview_detail.setLayoutManager(manager);
        recyclerview_detail.setAdapter(adapter);
        recyclerview_detail.setItemAnimator(new DefaultItemAnimator());
        setScrollEvent();

        adapter.setSource(imageDatas, this, getResources().getDisplayMetrics().widthPixels);

    }

    private void setData(){
        matchedUser = realm.where(MatchedUser.class).equalTo("id", matchId).findFirst();

        results = realm.where(ImageData.class).equalTo("fromUserId", matchId).findAll();
        for(ImageData imageData : results){
            imageDatas.add(imageData);
        }
    }



    private void setToolbar(){
        setSupportActionBar(toolbar_detail);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        text_location.setText(matchedUser.getCity() + ", " + matchedUser.getCountry());
    }

    private void setDetail_refresh(){


        StoreHouseHeader header = new StoreHouseHeader(getApplicationContext());
        header.initWithPointList(getPointList());

        detail_refresh.setHeaderView(header);
        detail_refresh.addPtrUIHandler(header);
        detail_refresh.setDurationToCloseHeader(3000);
        detail_refresh.setEnabledNextPtrAtOnce(true);
        detail_refresh.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerview_detail, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //리프레시 시작 및 끝
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        detail_refresh.refreshComplete();
                        startActivity(new Intent(getApplicationContext(), CameraFragmentActivity.class));
                        overridePendingTransition(R.anim.down_top, R.anim.top_down);
                    }
                }, 100);

                detail_refresh.refreshComplete();
            }
        });
    }


    private void setScrollEvent(){
        recyclerview_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scroll_y += dy;
                Log.i("SCROLL", String.valueOf(scroll_y));
                changeCameraVisible();
            }

            /*
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (!toolbar_visible) {
                            show_toolbar();
                        }
                        Log.i("STATE : ", "stop");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if (toolbar_visible) {
                            hide_toolbar();
                        }
                        Log.i("STATE : ", "drag");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Log.i("STATE : ", "setting");
                        break;

                }

            }
            */
        });
    }

    private void changeCameraVisible(){
        if(scroll_y > 200){
            if(detail_toolbar_camera_icon.getVisibility() == View.VISIBLE){
                ViewAnimation.alphaOut(detail_toolbar_camera_icon, 1000);
                ViewAnimation.alphaIn(text_location, 1000);
            }
        }else{
            if(detail_toolbar_camera_icon.getVisibility() == View.GONE){
                ViewAnimation.alphaOut(text_location, 1000);
                ViewAnimation.alphaIn(detail_toolbar_camera_icon, 1000);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }


    private ArrayList<float[]> getPointList() {
        // this point is taken from https://github.com/cloay/CRefreshLayout
        List<Point> startPoints = new ArrayList<Point>();
        startPoints.add(new Point(240, 80));
        startPoints.add(new Point(270, 80));
        startPoints.add(new Point(240, 80));
        startPoints.add(new Point(270, 80));
        startPoints.add(new Point(240, 80));
        startPoints.add(new Point(270, 80));
        startPoints.add(new Point(240, 80));
        startPoints.add(new Point(270, 80));

        startPoints.add(new Point(320, 70));
        startPoints.add(new Point(313, 80));
        startPoints.add(new Point(240, 80));
        startPoints.add(new Point(270, 80));
        startPoints.add(new Point(330, 80));
        startPoints.add(new Point(240, 80));
        startPoints.add(new Point(270, 80));
        startPoints.add(new Point(315, 110));
        startPoints.add(new Point(240, 80));
        startPoints.add(new Point(270, 80));
        startPoints.add(new Point(363, 103));

        startPoints.add(new Point(375, 80));
        startPoints.add(new Point(375, 80));
        startPoints.add(new Point(425, 80));
        startPoints.add(new Point(380, 95));
        startPoints.add(new Point(400, 63));

        List<Point> endPoints = new ArrayList<Point>();
        endPoints.add(new Point(270, 80));
        endPoints.add(new Point(270, 110));
        endPoints.add(new Point(270, 110));
        endPoints.add(new Point(250, 110));
        endPoints.add(new Point(275, 107));
        endPoints.add(new Point(302, 80));
        endPoints.add(new Point(302, 107));
        endPoints.add(new Point(302, 107));

        endPoints.add(new Point(340, 70));
        endPoints.add(new Point(360, 80));
        endPoints.add(new Point(330, 80));
        endPoints.add(new Point(340, 87));
        endPoints.add(new Point(315, 100));
        endPoints.add(new Point(345, 98));
        endPoints.add(new Point(330, 120));
        endPoints.add(new Point(345, 108));
        endPoints.add(new Point(360, 120));
        endPoints.add(new Point(363, 75));
        endPoints.add(new Point(345, 117));

        endPoints.add(new Point(380, 95));
        endPoints.add(new Point(425, 80));
        endPoints.add(new Point(420, 95));
        endPoints.add(new Point(420, 95));
        endPoints.add(new Point(400, 120));
        ArrayList<float[]> list = new ArrayList<float[]>();

        int offsetX = Integer.MAX_VALUE;
        int offsetY = Integer.MAX_VALUE;

        for (int i = 0; i < startPoints.size(); i++) {
            offsetX = Math.min(startPoints.get(i).x, offsetX);
            offsetY = Math.min(startPoints.get(i).y, offsetY);
        }
        for (int i = 0; i < endPoints.size(); i++) {
            float[] point = new float[4];
            point[0] = startPoints.get(i).x - offsetX;
            point[1] = startPoints.get(i).y - offsetY;
            point[2] = endPoints.get(i).x - offsetX;
            point[3] = endPoints.get(i).y - offsetY;
            list.add(point);
        }
        return list;
    }

}
