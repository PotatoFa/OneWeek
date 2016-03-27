package whataday.oneweek.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import io.realm.Realm;
import io.realm.RealmResults;
import whataday.oneweek.Camera.CameraActivity;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomCameraActivity.CameraFragmentActivity;
import whataday.oneweek.CustomCameraActivity.CustomCameraAcivity;
import whataday.oneweek.CustomView.MyScrollView;
import whataday.oneweek.CustomView.ViewAnimation;
import whataday.oneweek.Data.MatchedUser;
import whataday.oneweek.Match.DetailActivity;
import whataday.oneweek.R;

/**
 * Created by hoon on 2016-02-23.
 */
public class MatchFragment extends android.support.v4.app.Fragment {

    PtrFrameLayout store_house_ptr_frame;
    StoreHouseHeader header;

    Handler handler;
    View rootView;

    MyScrollView match_scroll;
    LinearLayout match_vertical;

    int view_width, view_height;
    int[] content_height = new int[3];


    private int scrolled_distance = 0;
    private static final int HIDE_THRESHOLD = 10;
    private boolean toolbar_visible = true;

    int scroll_max_y, current_y, pre_y;
    int linear_max, content_max;
    int focus_item;
    int count = 0;

    Float min_alpha = new Float(0.15);
    Float max_alpha = new Float(0.6);
    int max_time_text;

    Bitmap bitmap_image;
    Bitmap[] resize_background = new Bitmap[3];

    ImageView[] imageView_background = new ImageView[3];
    ImageView[] imageView_background_dark = new ImageView[3];
    float[] dark_alpha = new float[3];
    int[] centerMargin = new int[3];

    TextView[] textView_Country = new TextView[3];
    TextView[] textView_City = new TextView[3];
    TextView[] textView_time = new TextView[3];

    RelativeLayout[] content_layout = new RelativeLayout[3];
    RelativeLayout[] box_text = new RelativeLayout[3];


    LinearLayout.LayoutParams[] content_layout_param = new LinearLayout.LayoutParams[3];
    RelativeLayout.LayoutParams[] country_textview_param = new RelativeLayout.LayoutParams[3];
    RelativeLayout.LayoutParams[] city_textview_param = new RelativeLayout.LayoutParams[3];
    RelativeLayout.LayoutParams[] time_textview_param = new RelativeLayout.LayoutParams[3];
    RelativeLayout.LayoutParams[] box_text_param = new RelativeLayout.LayoutParams[3];


    RelativeLayout toolbar_match;

    Realm realm;

    public static MatchFragment newInstance() {
        MatchFragment matchFragment = new MatchFragment();
        return matchFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout)inflater.inflate(R.layout.main_fragment_match, container, false);
        this.rootView = rootView;
        return rootView;
    }


    String[] matched_id = new String[3];
    String TAG = "MATCHTEST";
    RealmResults<MatchedUser> matchedUsers;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //뷰 추가 작업
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        view_width = dm.widthPixels;
        view_height = dm.heightPixels;

        realm = ApplicationController.getRealm();

        matchedUsers = realm.where(MatchedUser.class).findAll();
        for(int i = 0; i < 3; i++){
            matched_id[i] = matchedUsers.get(i).getId();
            Log.i(TAG, " append "+i+" / "+matched_id[i]);
        }
        getResizeBitmap();
        initView();

    }

    String set_matchid;
    Intent intent;
    ImageView toolbar_camera_icon;
    RelativeLayout btn_match_menu;


    private void addEmptyView(int i){


        box_text[i] = new RelativeLayout(getActivity());
        box_text[i].setGravity(Gravity.CENTER);
        box_text[i].setLayoutParams(box_text_param[i]);

        textView_City[i] = new TextView(getActivity());
        textView_City[i].setTextSize(0);
        content_layout[i].addView(textView_City[i]);

        textView_Country[i] = new TextView(getActivity());
        textView_Country[i].setTextSize(0);
        content_layout[i].addView(textView_Country[i]);

        Button icon = new Button(getActivity());
        icon.setBackgroundResource(R.drawable.icon_empty);

        RelativeLayout.LayoutParams iconLayoutParam =
                new RelativeLayout.LayoutParams((int) getActivity().getResources().getDimension(R.dimen.dp_52dp),
                        (int) getActivity().getResources().getDimension(R.dimen.dp_52dp));
        iconLayoutParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        icon.setLayoutParams(iconLayoutParam);
        icon.setId(i+1);
        content_layout[i].addView(icon);

        TextView textView_waiting = new TextView(getActivity());
        textView_waiting.setTypeface(Typeface.createFromAsset((getActivity().getAssets()), "Radnika-SemiBold.otf"));
        textView_waiting.setText(R.string.string_empty);
        textView_waiting.setTextColor(Color.parseColor("#ffffff"));
        textView_waiting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        textView_waiting.setSingleLine(true);

        RelativeLayout.LayoutParams textLayoutParam =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParam.addRule(RelativeLayout.BELOW, icon.getId());
        textLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        textView_waiting.setLayoutParams(textLayoutParam);

        content_layout[i].addView(textView_waiting);

        textView_time[i] = new TextView(getActivity());
        textView_time[i].setText("TIME" + i);
        textView_time[i].setTypeface(Typeface.createFromAsset((getActivity().getAssets()), "Radnika-SemiBold.otf"));
        textView_time[i].setTextSize(0);
        time_textview_param[i].addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        //시간 변경 추가. 쓰레드 초단위로
        textView_time[i].setLayoutParams(time_textview_param[i]);

        content_layout[i].addView(textView_time[i]);

        match_vertical.addView(content_layout[i], content_layout_param[i]);
        //Linear에 content Add
    }
    private void addWaitingView(int i){


        box_text[i] = new RelativeLayout(getActivity());
        box_text[i].setGravity(Gravity.CENTER);
        box_text[i].setLayoutParams(box_text_param[i]);

        textView_City[i] = new TextView(getActivity());
        textView_City[i].setTextSize(0);
        content_layout[i].addView(textView_City[i]);

        textView_Country[i] = new TextView(getActivity());
        textView_Country[i].setTextSize(0);
        content_layout[i].addView(textView_Country[i]);

        ImageView icon = new ImageView(getActivity());
        icon.setBackgroundColor(Color.parseColor("#464646"));

        RelativeLayout.LayoutParams iconLayoutParam =
                new RelativeLayout.LayoutParams((int) getActivity().getResources().getDimension(R.dimen.dp_52dp),
                        (int) getActivity().getResources().getDimension(R.dimen.dp_52dp));
        iconLayoutParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        icon.setLayoutParams(iconLayoutParam);
        content_layout[i].addView(icon);

        TextView textView_waiting = new TextView(getActivity());
        textView_waiting.setTypeface(Typeface.createFromAsset((getActivity().getAssets()), "Radnika-SemiBold.otf"));
        textView_waiting.setText(R.string.string_search);
        textView_waiting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        textView_waiting.setTextColor(Color.parseColor("#ffffff"));
        textView_waiting.setSingleLine(true);

        RelativeLayout.LayoutParams textLayoutParam =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParam.addRule(RelativeLayout.BELOW, icon.getId());
        textLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        textView_waiting.setLayoutParams(textLayoutParam);

        content_layout[i].addView(textView_waiting);

        textView_time[i] = new TextView(getActivity());
        textView_time[i].setText("TIME" + i);
        textView_time[i].setTypeface(Typeface.createFromAsset((getActivity().getAssets()), "Radnika-SemiBold.otf"));
        textView_time[i].setTextSize(0);
        time_textview_param[i].addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        //시간 변경 추가. 쓰레드 초단위로
        textView_time[i].setLayoutParams(time_textview_param[i]);

        content_layout[i].addView(textView_time[i]);

        match_vertical.addView(content_layout[i], content_layout_param[i]);
        //Linear에 content Add

    }
    private void addMatchingView(int i){

        set_matchid = matchedUsers.get(i).getId();


        box_text[i] = new RelativeLayout(getActivity());
        box_text[i].setGravity(Gravity.CENTER);
        box_text[i].setLayoutParams(box_text_param[i]);
        content_layout[i].addView(box_text[i]);

        textView_City[i] = new TextView(getActivity());
        textView_City[i].setText(matchedUsers.get(i).getCity());
        textView_City[i].setTextSize(27);
        if(i==0){
            textView_City[i].setId(R.id.match_city0);
        }else if(i==1){
            textView_City[i].setId(R.id.match_city1);
        }else if(i==2){
            textView_City[i].setId(R.id.match_city2);
        }
        textView_City[i].setTextColor(Color.parseColor("#ffffff"));
        textView_City[i].setTypeface(Typeface.createFromAsset((getActivity().getAssets()), "Radnika-Light.otf"));
        city_textview_param[i].addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        textView_City[i].setLayoutParams(city_textview_param[i]);
        //Country 텍스트뷰 아래쪽에 위치하게끔 City텍스트뷰 Param을 설정
        box_text[i].addView(textView_City[i]);

        textView_Country[i] = new TextView(getActivity());
        textView_Country[i].setText(matchedUsers.get(i).getCountry());
        textView_Country[i].setTextColor(Color.parseColor("#ffffff"));
        textView_Country[i].setTypeface(Typeface.createFromAsset((getActivity().getAssets()), "Radnika-Bold.otf"));
        textView_Country[i].setTextSize(40);
        country_textview_param[i].addRule(RelativeLayout.ABOVE, textView_City[i].getId());
        country_textview_param[i].addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        textView_Country[i].setLayoutParams(country_textview_param[i]);

        box_text[i].addView(textView_Country[i]);
        //content에 country Add

        textView_time[i] = new TextView(getActivity());
        textView_time[i].setText("TIME" + i);
        textView_time[i].setTextColor(Color.parseColor("#ffffff"));
        textView_time[i].setTypeface(Typeface.createFromAsset((getActivity().getAssets()), "Radnika-SemiBold.otf"));
        textView_time[i].setTextSize(16);
        textView_time[i].setVisibility(View.INVISIBLE);
        //textView_time[i].setGravity(Gravity.CENTER);
        time_textview_param[i].addRule(RelativeLayout.BELOW, textView_City[i].getId());
        time_textview_param[i].addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        time_textview_param[i].topMargin = (int) getActivity().getResources().getDimension(R.dimen.dp_10dp);
        //시간 변경 추가. 쓰레드 초단위로
        textView_time[i].setLayoutParams(time_textview_param[i]);
        box_text[i].addView(textView_time[i]);
        match_vertical.addView(content_layout[i], content_layout_param[i]);
        //Linear에 content Add

        max_time_text = textView_time[i].getHeight();

    }

    private void initView(){

        store_house_ptr_frame = (PtrFrameLayout) rootView.findViewById(R.id.store_house_ptr_frame);
        toolbar_camera_icon = (ImageView) rootView.findViewById(R.id.toolbar_camera_icon);
        btn_match_menu = (RelativeLayout) rootView.findViewById(R.id.btn_match_menu);

        btn_match_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPagerActivity)getActivity()).changeViewPager(0);
            }
        });

        header = new StoreHouseHeader(getActivity().getApplicationContext());
        header.initWithPointList(getPointList());

        store_house_ptr_frame.setDurationToCloseHeader(3000);
        store_house_ptr_frame.setHeaderView(header);
        store_house_ptr_frame.addPtrUIHandler(header);
        store_house_ptr_frame.setEnabledNextPtrAtOnce(true);
        store_house_ptr_frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, match_scroll, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //리프레시 시작 및 끝
                Log.i("REFRESH", "ON REFRESH BEGIN");
                //카메라 전송 등의 액션
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        store_house_ptr_frame.refreshComplete();
                        getActivity().startActivity(new Intent(getActivity(), CameraFragmentActivity.class));
                        //getActivity().startActivity(new Intent(getActivity(), CameraActivity.class));

                        getActivity().overridePendingTransition(R.anim.down_top, R.anim.top_down);
                    }
                }, 100);

                store_house_ptr_frame.refreshComplete();
                //리프레시 완료
            }
        });

        toolbar_match = (RelativeLayout)rootView.findViewById(R.id.toolbar_match);
        match_scroll = (MyScrollView)rootView.findViewById(R.id.match_scroll);
        match_vertical = (LinearLayout)rootView.findViewById(R.id.match_vertical);



        focus_item = 0;

        content_height[0] = view_width;
        content_height[1] = view_width/2;
        content_height[2] = view_width/2;
        //초기 뷰 크기 선언. 2/1/1 비율

        dark_alpha[0] = min_alpha;
        dark_alpha[1] = max_alpha;
        dark_alpha[2] = max_alpha;

        for(int i=0; i<3; i++){

            box_text_param[i] =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            country_textview_param[i] =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            city_textview_param[i] =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            time_textview_param[i] =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            content_layout_param[i] =
                    new LinearLayout.LayoutParams(view_width, content_height[i]);
            //Vertical로 선언된 LinearLayout에 들어갈 각 content(RelativeLayout)의 초기 Param설정(크기).

            content_layout[i] = new RelativeLayout(getActivity());
            imageView_background[i] = new ImageView(getActivity());
            imageView_background[i].setImageBitmap(resize_background[i]);
            //사진설정
            imageView_background[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView_background[i].setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            content_layout[i].addView(imageView_background[i]);

            imageView_background_dark[i] = new ImageView(getActivity());
            imageView_background_dark[i].setBackgroundColor(Color.BLACK);
            imageView_background_dark[i].setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView_background_dark[i].setAlpha(dark_alpha[i]);
            content_layout[i].addView(imageView_background_dark[i]);


            if(matched_id[i].equals("empty")){
                addEmptyView(i);
            }else if(matched_id[i].equals("search")){
                addWaitingView(i);
            }else{
                addMatchingView(i);
            }
        }

        handler = new Handler();
        handler.post(timer_text);

        hide_tiem_text(focus_item);

        match_vertical.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linear_max = match_vertical.getHeight() - view_height;
                Log.i("Linear_max ::", String.valueOf(linear_max));
            }
        });

        match_scroll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                content_max = view_height - match_scroll.getHeight();
                Log.i("content ::", String.valueOf(content_max));
                scroll_max_y = linear_max + content_max;
                Log.i("scroll ::", String.valueOf(scroll_max_y));
            }
        });
        match_scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(!toolbar_visible){
                        show_toolbar();
                    }
                    match_scroll.startScrollerTask();
                }else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(toolbar_visible){
                        hide_toolbar();
                    }
                    match_scroll.startScrollerTask();
                }
                return false;
            }
        });

        match_scroll.setOnScrollStoppedListener(new MyScrollView.OnScrollStoppedListener() {
            @Override
            public void onScrollStopped() {
                if (!toolbar_visible) {
                    //show_toolbar();

                }
            }
        });

        match_scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                pre_y = current_y;
                current_y = match_scroll.getScrollY();

                if (current_y < 40) {
                    if(!(toolbar_camera_icon.getVisibility() == View.VISIBLE)){
                        ViewAnimation.alphaIn(toolbar_camera_icon, 300);
                    }
                }else {
                    if(toolbar_camera_icon.getVisibility() == View.VISIBLE){
                        ViewAnimation.alphaOut(toolbar_camera_icon, 300);
                    }
                }

                if (toolbar_visible) {
                    //툴바가 보여지고 있을때
                    scrolled_distance += current_y - pre_y;
                    if (Math.abs(scrolled_distance) > HIDE_THRESHOLD) {
                        //이동시킨 스크롤 값이 문턱치 이상일때
                        //hide_toolbar();
                        scrolled_distance = 0;
                        //툴바를 숨기고 이동거리를 초기화. -> 툴바가 보여지는 부분은 stopListener
                    }
                }

                if (current_y < scroll_max_y / 2) {
                    //첫번째_decrease / 두번째_increase / 세번째 고정
                    //첫번째 = width~width/2 : 0~scroll_max_y/2
                    content_height[0] = convert_int_map(current_y, 0, scroll_max_y / 2, view_width, view_width / 2);
                    //두번째 = width/2~width : 0~scroll_max_y/2
                    content_height[1] = convert_int_map(current_y, 0, scroll_max_y / 2, view_width / 2, view_width);
                    //세번째 = width/2
                    content_height[2] = view_width / 2;

                    if (content_height[0] < content_height[1]) {
                        focus_item = 1;
                    } else {
                        focus_item = 0;
                    } //포커스 이동
                    //Log.i("One :", String.valueOf(content_height[0])+"/"+String.valueOf(content_height[1])+"/"+String.valueOf(content_height[2]));
                } else if (current_y > scroll_max_y / 2) {
                    //첫번째_고정 / 두번째_decrease / 세번째_increase
                    //첫번째 = width/2 : scroll_max_y/2~scroll_max_y
                    if (current_y > scroll_max_y)
                        content_height[0] = view_width / 2;
                    //두번째 = width~width/2 : scroll_max_y/2~scroll_max_y
                    content_height[1] = convert_int_map(current_y, scroll_max_y / 2, scroll_max_y, view_width, view_width / 2);
                    //세번째 = width/2~width : scroll_max_y/2~scroll_max_y
                    content_height[2] = convert_int_map(current_y, scroll_max_y / 2, scroll_max_y, view_width / 2, view_width);

                    if (content_height[1] < content_height[2]) {
                        focus_item = 2;
                    } else {
                        focus_item = 1;
                    }
                }

                for (int i = 0; i < 3; i++) {
                    content_layout_param[i].height = content_height[i];
                    content_layout[i].setLayoutParams(content_layout_param[i]);
                    dark_alpha[i] = convert_float_map(content_height[i], view_width / 2, view_width, max_alpha, min_alpha);
                    imageView_background_dark[i].setAlpha(dark_alpha[i]);


                }
                Log.i("FOCUS :", String.valueOf(current_y));
                hide_tiem_text(focus_item);
            }
        });

        setClick();

    }


    private void setClick(){
        content_layout[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("matchId", matched_id[0]);
                getActivity().startActivity(intent);
            }
        });
        content_layout[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("matchId", matched_id[1]);
                getActivity().startActivity(intent);
            }
        });
        content_layout[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("matchId", matched_id[2]);
                getActivity().startActivity(intent);
            }
        });
    }
    private void hide_toolbar(){
        toolbar_match.animate().translationY(-toolbar_match.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        toolbar_visible = false;
    }
    private void show_toolbar(){
        toolbar_match.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        toolbar_visible = true;
    }
    private void hide_tiem_text(int focus){

        for(int i = 0; i < 3; i++){

            centerMargin[i] = convert_int_map(content_height[i], view_width / 2, view_width, 56, 1);

            box_text_param[i].topMargin = centerMargin[i];
            box_text[i].setLayoutParams(box_text_param[i]);

            if(focus != i){
                content_layout[i].setEnabled(false);
                if(textView_time[i].getVisibility() == View.VISIBLE){
                    ViewAnimation.alphaInvisible(textView_time[i], 300);
                }
            }else{
                content_layout[i].setEnabled(true);
                if(textView_time[i].getVisibility() == View.INVISIBLE){
                    ViewAnimation.alphaIn(textView_time[i], 300);
                }
            }
        }
    }

    private Runnable timer_text = new Runnable() {
        @Override
        public void run() {

            Date now = new Date();
            SimpleDateFormat time_format = new SimpleDateFormat("a HH:mm:ss", Locale.UK);
            SimpleDateFormat date_format = new SimpleDateFormat("MMM.dd.yyyy", Locale.UK);

            String string_time_date = time_format.format(now)+"\n"+date_format.format(now);

            for(int i = 0; i < 3; i++){
                textView_time[i].setText(string_time_date.toUpperCase());

            }
            handler.postDelayed(timer_text, 1000);
        }
    };

    int convert_int_map(int input, int input_min, int input_max, int convert_min, int convert_max){
        return ( ((input - input_min) * (convert_max - convert_min)) / (input_max - input_min) ) + convert_min;
    }
    float convert_float_map(int input, int input_min, int input_max, float convert_min, float convert_max){
        return ( ((input - input_min) * (convert_max - convert_min)) / (input_max - input_min) ) + convert_min;
    }
    public void getResizeBitmap(){
        //MatchUser 검색 후 백그라운드 이미지 리사이징
        for(int i=0; i < matched_id.length; i++){
            if(matchedUsers.get(i).getId().equals("empty") || matchedUsers.get(i).getId().equals("search")){
                bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.home_shadow);
            }else if(matchedUsers.get(i).getCountry().equals("Japan")){
                bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.city_japan);
            }else if(matchedUsers.get(i).getCountry().equals("South korea")) {
                bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.city_korea);
            }
            resize_background[i] = Bitmap.createScaledBitmap(bitmap_image, view_width, view_width, true);

        }
    }

    @Override
    public void onPause() {
        if (handler != null)
            handler.removeCallbacks(timer_text);

        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();

        if (handler != null)
            handler.post(timer_text);
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
