package whataday.test_ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ScrollviewActivity extends AppCompatActivity {

    ScrollView content;
    LinearLayout scroll_linear;

    int view_width, view_height;
    int[] content_height = new int[3];

    int scroll_max_y;
    int linear_max, content_max;

    RelativeLayout.LayoutParams text_param_country =
            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    RelativeLayout.LayoutParams text_param_city =
            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    TextView[] textView_Country = new TextView[3];
    TextView[] textView_City = new TextView[3];
    RelativeLayout[] content_layout = new RelativeLayout[3];
    LinearLayout.LayoutParams[] content_layout_param = new LinearLayout.LayoutParams[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        view_width = dm.widthPixels;
        view_height = dm.heightPixels;
        //기기 pixel크기

        content_height[0] = view_width;
        content_height[1] = view_width/2;
        content_height[2] = view_width/2;
        //초기 뷰 크기 선언. 2/1/1 비율

        content = (ScrollView)findViewById(R.id.scrollview);
        scroll_linear = (LinearLayout)findViewById(R.id.scroll_linear);

        content_layout_param[0] = new LinearLayout.LayoutParams(view_width, content_height[0]);
        content_layout_param[1] = new LinearLayout.LayoutParams(view_width, content_height[1]);
        content_layout_param[2] = new LinearLayout.LayoutParams(view_width, content_height[2]);


        for(int i=0; i<3; i++){

            content_layout[i] = new RelativeLayout(this);
            scroll_linear.addView(content_layout[i], content_layout_param[i]);
            content_layout[i].setBackgroundResource(R.drawable.test2);

            textView_Country[i] = new TextView(this);
            textView_Country[i].setText("Country");
            textView_Country[i].setTextSize(50);
            textView_Country[i].setId(i);
            text_param_country.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            content_layout[i].addView(textView_Country[i]);
            textView_Country[i].setLayoutParams(text_param_country);

            textView_City[i] = new TextView(this);
            textView_City[i].setText("CITY");
            textView_City[i].setTextSize(30);
            text_param_city.addRule(RelativeLayout.BELOW, i);
            text_param_city.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            content_layout[i].addView(textView_City[i]);
            textView_City[i].setLayoutParams(text_param_city);

        }


        scroll_linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("ssSIZE h:", String.valueOf(scroll_linear.getHeight()));
                Log.i("ssSIZE w:", String.valueOf(scroll_linear.getWidth()));
                Log.i("ssSIZE max:", String.valueOf(scroll_linear.getHeight() - view_height));
                linear_max = scroll_linear.getHeight() - view_height;
            }
        });

        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("SIZE h:", String.valueOf(content.getHeight()));
                Log.i("SIZE w:", String.valueOf(content.getWidth()));
                Log.i("SIZE max:", String.valueOf(view_height - content.getHeight()));
                content_max = view_height - content.getHeight();

                scroll_max_y = linear_max + content_max;
                Log.i("scroll_max_y :", String.valueOf(scroll_max_y));

            }
        });


        content.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                int current_y = content.getScrollY();
                if (current_y < scroll_max_y / 2) {
                    //첫번째_decrease / 두번째_increase / 세번째 고정
                    //첫번째 = width~width/2 : 0~scroll_max_y/2
                    content_height[0] = convert_int_map(current_y, 0, scroll_max_y/2, view_width, view_width/2);
                    //두번째 = width/2~width : 0~scroll_max_y/2
                    content_height[1] = convert_int_map(current_y, 0, scroll_max_y/2, view_width/2, view_width);
                    //세번째 = width/2
                    content_height[2] = view_width/2;
                    Log.i("One :", String.valueOf(content_height[0])+"/"+String.valueOf(content_height[1])+"/"+String.valueOf(content_height[2]));
                } else if (current_y > scroll_max_y / 2) {
                    //첫번째_고정 / 두번째_decrease / 세번째_increase
                    //첫번째 = width/2 : scroll_max_y/2~scroll_max_y
                    content_height[0] = view_width/2;
                    //두번째 = width~width/2 : scroll_max_y/2~scroll_max_y
                    content_height[1] = convert_int_map(current_y, scroll_max_y/2, scroll_max_y, view_width, view_width/2);
                    //세번째 = width/2~width : scroll_max_y/2~scroll_max_y
                    content_height[2] = convert_int_map(current_y, scroll_max_y/2, scroll_max_y, view_width/2, view_width);
                    Log.i("Two :", String.valueOf(content_height[0])+"/"+String.valueOf(content_height[1])+"/"+String.valueOf(content_height[2]));
                }

                for(int i=0; i<3; i++){

                    content_layout_param[i].height = content_height[i];
                    content_layout[i].setLayoutParams(content_layout_param[i]);
                    textView_City[i].setLayoutParams(text_param_city);
                    textView_Country[i].setLayoutParams(text_param_country);
                }

                /*
                content_layout_param[0].height = content_height[0];
                content_layout_param[1].height = content_height[1];
                content_layout_param[2].height = content_height[2];

                content_layout[0].setLayoutParams(content_layout_param[0]);
                content_layout[1].setLayoutParams(content_layout_param[1]);
                content_layout[2].setLayoutParams(content_layout_param[2]);

                textView_City[i].setLayoutParams(text_param_city);
                textView_Country[i].setLayoutParams(text_param_country);
*/

            }
        });


    }

    int convert_int_map(int input, int input_min, int input_max, int convert_min, int convert_max){
        return ( ((input - input_min) * (convert_max - convert_min)) / (input_max - input_min) ) + convert_min;
    }
}
