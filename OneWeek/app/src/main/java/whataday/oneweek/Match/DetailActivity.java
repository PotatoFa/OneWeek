package whataday.oneweek.Match;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.Data.ImageData;
import whataday.oneweek.Data.MatchedUser;
import whataday.oneweek.R;

public class DetailActivity extends SetFontActivity {

    Toolbar toolbar_detail;
    Intent intent;
    Realm realm;
    String matchId;
    MatchedUser matchedUser;

    RecyclerView recyclerview_detail;
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

        matchedUser = realm.where(MatchedUser.class).equalTo("id", matchId).findFirst();

        setToolbar();

        results = realm.where(ImageData.class).equalTo("fromUserId", matchId).findAll();
        for(ImageData imageData : results){
            imageDatas.add(imageData);
        }

        recyclerview_detail = (RecyclerView)findViewById(R.id.recyclerview_detail);

        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new DetailRecyclerAdapter();

        recyclerview_detail.setLayoutManager(manager);
        recyclerview_detail.setAdapter(adapter);
        recyclerview_detail.setItemAnimator(new DefaultItemAnimator());

        adapter.setSource(imageDatas, this, getResources().getDisplayMetrics().widthPixels);



    }

    TextView text_location;
    Button btn_back;


    private void setToolbar(){
        toolbar_detail = (Toolbar)findViewById(R.id.toolbar_detail);
        text_location = (TextView)findViewById(R.id.text_location);
        btn_back = (Button)findViewById(R.id.btn_back);
        setSupportActionBar(toolbar_detail);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        text_location.setText(matchedUser.getCity() + ", " + matchedUser.getCountry());
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
