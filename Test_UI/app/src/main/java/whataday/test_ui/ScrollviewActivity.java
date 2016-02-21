package whataday.test_ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class ScrollviewActivity extends AppCompatActivity {

    ScrollView content;
    LinearLayout scroll_linear;

    int view_width, view_height;
    int[] content_height = new int[3];

    int scroll_max_y;
    int linear_max, content_max;

    ImageView content_first, content_second , content_third;

    LinearLayout.LayoutParams content_first_param, content_second_param, content_third_param;


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

        content_first = new ImageView(this);
        content_second = new ImageView(this);
        content_third = new ImageView(this);

        content_first_param = new LinearLayout.LayoutParams(view_width, content_height[0]);
        content_second_param = new LinearLayout.LayoutParams(view_width, content_height[1]);
        content_third_param = new LinearLayout.LayoutParams(view_width, content_height[2]);

        scroll_linear.addView(content_first, content_first_param);
        scroll_linear.addView(content_second, content_second_param);
        scroll_linear.addView(content_third, content_third_param);

        content_first.setBackgroundResource(R.drawable.test);
        content_second.setBackgroundResource(R.drawable.test2);
        content_third.setBackgroundResource(R.drawable.test);

        content_first.setScaleType(ImageView.ScaleType.MATRIX);
        content_second.setScaleType(ImageView.ScaleType.MATRIX);
        content_third.setScaleType(ImageView.ScaleType.MATRIX);


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

                content_first_param.height = content_height[0];
                content_second_param.height = content_height[1];
                content_third_param.height = content_height[2];

                content_first.setLayoutParams(content_first_param);
                content_second.setLayoutParams(content_second_param);
                content_third.setLayoutParams(content_third_param);


/*
                int sd = convert_int_map(current_y, 0, scroll_max_y, view_width/2, view_width);
                // imageView.setLayoutParams(new LinearLayout.LayoutParams(width, imageView.getHeight()-content.getScrollY()));
                //Log.i("contentsize:", String.valueOf(sd));

                String content_size = String.valueOf(content_height[0]) + "/"
                        + String.valueOf(content_height[1]) + "/"
                        + String.valueOf(content_height[2]);
                //Log.i("contentsize:", content_size);
*/
            }
        });


    }

    int convert_int_map(int input, int input_min, int input_max, int convert_min, int convert_max){
        return ( ((input - input_min) * (convert_max - convert_min)) / (input_max - input_min) ) + convert_min;
    }
}
