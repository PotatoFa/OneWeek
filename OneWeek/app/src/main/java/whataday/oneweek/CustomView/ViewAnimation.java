package whataday.oneweek.CustomView;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import whataday.oneweek.R;

/**
 * Created by hoon on 2016-03-07.
 */
public class ViewAnimation {

    // To animate view slide out from left to right
    public void slideToRight(View view){
        TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }
    // To animate view slide out from right to left
    public void slideToLeft(View view){
        TranslateAnimation animate = new TranslateAnimation(0,-view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    // To animate view slide out from top to bottom
    public void slideToBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    public void slideInBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,-view.getHeight(),0);
        animate.setDuration(1000);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    // To animate view slide out from bottom to top
    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    public void alphaIn(View view){
        Animation animate = new AlphaAnimation(0, 1);
        animate.setDuration(1000);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public void alphaOut(View view){
        Animation animate = new AlphaAnimation(1, 0);
        animate.setDuration(300);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }
    public static void grayToYellow(View view){
        view.setBackgroundResource(R.drawable.color_graytoyellow);
        TransitionDrawable transitionDrawable = (TransitionDrawable) view.getBackground();
        transitionDrawable.startTransition(500);

    }
    public static void yellowToGray(View view){
        view.setBackgroundResource(R.drawable.color_yellowtogray);
        TransitionDrawable transitionDrawable = (TransitionDrawable) view.getBackground();
        transitionDrawable.startTransition(500);

    }

    public static void startTransition(View view){
        TransitionDrawable transitionDrawable = (TransitionDrawable) view.getBackground();
        transitionDrawable.startTransition(500);
    }


}
