package whataday.test_ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import whataday.test_ui.Camera.CameraActivity;

public class ScrollviewActivity extends AppCompatActivity {

    ScrollView content;
    LinearLayout scroll_linear;

    int view_width, view_height;
    int[] content_height = new int[3];

    int scroll_max_y;
    int linear_max, content_max;
    int focus_item;
    Float min_alpha = new Float(0);
    Float max_alpha = new Float(0.6);

    Bitmap bitmap_image;
    Bitmap resize_Bitmap;

    ImageView[] imageView_background = new ImageView[3];
    ImageView[] imageView_background_dark = new ImageView[3];
    float[] dark_alpha = new float[3];

    TextView[] textView_Country = new TextView[3];
    TextView[] textView_City = new TextView[3];
    RelativeLayout[] content_layout = new RelativeLayout[3];
    LinearLayout.LayoutParams[] content_layout_param = new LinearLayout.LayoutParams[3];
    RelativeLayout.LayoutParams[] country_textview_param = new RelativeLayout.LayoutParams[3];
    RelativeLayout.LayoutParams[] city_textview_param = new RelativeLayout.LayoutParams[3];

    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrollview);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        view_width = dm.widthPixels;
        view_height = dm.heightPixels;
        //기기 pixel크기
        getResizeBitmap();

        content_height[0] = view_width;
        content_height[1] = view_width/2;
        content_height[2] = view_width/2;
        //초기 뷰 크기 선언. 2/1/1 비율

        dark_alpha[0] = min_alpha;
        dark_alpha[1] = max_alpha;
        dark_alpha[2] = max_alpha;

        content = (ScrollView)findViewById(R.id.scrollview);
        scroll_linear = (LinearLayout)findViewById(R.id.scroll_linear);
        focus_item = 0;

        for(int i=0; i<3; i++){

            country_textview_param[i] =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            city_textview_param[i] =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            content_layout_param[i] = new LinearLayout.LayoutParams(view_width, content_height[i]);
            //Vertical로 선언된 LinearLayout에 들어갈 각 content(RelativeLayout)의 초기 Param설정(크기).

            content_layout[i] = new RelativeLayout(this);
//            content_layout[i].setBackgroundResource(R.drawable.test2);
            //content(RelativeLayout)생성 후 백그라운드 이미지 설정.
            imageView_background[i] = new ImageView(this);
            //imageView_background[i].setBackgroundResource(R.drawable.test);
            imageView_background[i].setImageBitmap(resize_Bitmap);
            imageView_background[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView_background[i].setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            content_layout[i].addView(imageView_background[i]);

            imageView_background_dark[i] = new ImageView(this);
            imageView_background_dark[i].setBackgroundColor(Color.BLACK);
            imageView_background_dark[i].setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView_background_dark[i].setAlpha(dark_alpha[i]);
            content_layout[i].addView(imageView_background_dark[i]);

            textView_Country[i] = new TextView(this);
            textView_Country[i].setText("Country"+i);
            textView_Country[i].setTextSize(50);
            textView_Country[i].setId(i+1);
            country_textview_param[i].addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            textView_Country[i].setLayoutParams(country_textview_param[i]);
            //content의 중앙에 위치하게끔 Country Param을 설정.
            //Country텍스트뷰에 설정한 Param을 설정.
            content_layout[i].addView(textView_Country[i]);
            //content에 country Add

            textView_City[i] = new TextView(this);
            textView_City[i].setText("CITY"+i);
            textView_City[i].setTextSize(30);
            city_textview_param[i].addRule(RelativeLayout.BELOW, i+1);
            city_textview_param[i].addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            textView_City[i].setLayoutParams(city_textview_param[i]);
            //Country 텍스트뷰 아래쪽에 위치하게끔 City텍스트뷰 Param을 설정
            content_layout[i].addView(textView_City[i]);
            //content에 city Add

            scroll_linear.addView(content_layout[i], content_layout_param[i]);
            //Linear에 content Add

        }


        scroll_linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linear_max = scroll_linear.getHeight() - view_height;
                Log.i("Linear_max ::", String.valueOf(linear_max));

            }
        });

        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                content_max = view_height - content.getHeight();
                Log.i("content ::", String.valueOf(content_max));
                scroll_max_y = linear_max + content_max;
                Log.i("scroll ::", String.valueOf(scroll_max_y));


            }
        });

        content.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int current_y = content.getScrollY();
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
                    // Log.i("Two :", String.valueOf(content_height[0])+"/"+String.valueOf(content_height[1])+"/"+String.valueOf(content_height[2]));
                }

                for (int i = 0; i < 3; i++) {
                    content_layout_param[i].height = content_height[i];
                    content_layout[i].setLayoutParams(content_layout_param[i]);
                    dark_alpha[i] = convert_float_map(content_height[i], view_width / 2, view_width, max_alpha, min_alpha);
                    imageView_background_dark[i].setAlpha(dark_alpha[i]);
                }
                Log.i("FOCUS :", String.valueOf(current_y));

            }
        });

        swipe = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                overridePendingTransition(R.anim.down_top, R.anim.top_down);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();

    }



    int convert_int_map(int input, int input_min, int input_max, int convert_min, int convert_max){
        return ( ((input - input_min) * (convert_max - convert_min)) / (input_max - input_min) ) + convert_min;
    }

    float convert_float_map(int input, int input_min, int input_max, float convert_min, float convert_max){
        return ( ((input - input_min) * (convert_max - convert_min)) / (input_max - input_min) ) + convert_min;
    }

    public void getResizeBitmap(){
        bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.test2);
        resize_Bitmap = Bitmap.createScaledBitmap(bitmap_image, view_width, view_width, true);
    }
}
