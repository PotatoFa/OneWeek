package whataday.oneweek.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by hoon on 2016-02-26.
 */
public class MyScrollView extends ScrollView {
    //Stop리스너 추가한 커스텀 스크롤뷰
    private int initialPosition;
    private Runnable scrollerTask;
    private int newCheck = 500;

    public MyScrollView(Context context) {
        super(context);
    }

    public interface OnScrollStoppedListener{
        void onScrollStopped();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        scrollerTask = new Runnable() {

            public void run() {

                int newPosition = getScrollY();
                if(initialPosition - newPosition == 0){//has stopped

                    if(onScrollStoppedListener!=null){

                        onScrollStoppedListener.onScrollStopped();
                    }
                }else{
                    initialPosition = getScrollY();
                    MyScrollView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }


    private OnScrollStoppedListener onScrollStoppedListener;

    public void setOnScrollStoppedListener(MyScrollView.OnScrollStoppedListener listener){
        onScrollStoppedListener = listener;
    }

    public void startScrollerTask(){

        initialPosition = getScrollY();
        MyScrollView.this.postDelayed(scrollerTask, newCheck);
    }


}
