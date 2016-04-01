package com.example.jaehun.networkcheck;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by jaehun on 16. 3. 29..
 */
public class MyView extends View {


    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int view_height, view_width;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        view_width = getMeasuredWidth();
        view_height = getMeasuredHeight();


        canvas.drawColor(Color.parseColor("#000000"));
        Path path= new Path();
        Paint paint= new Paint();

        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#ffffff"));
        paint.setStyle(Paint.Style.STROKE);

        Log.i("MyView Circle X :", String.valueOf(view_width / 2));
        Log.i("MyView Circle Y :", String.valueOf(view_height / 2));

        path.addCircle(50, 50, 20, Path.Direction.CW);


        canvas.drawPath(path, paint);

    }


}
