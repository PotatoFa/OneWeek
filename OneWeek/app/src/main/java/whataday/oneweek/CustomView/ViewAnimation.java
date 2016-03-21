package whataday.oneweek.CustomView;

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
    public static void slideToRight(View view){
        TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }
    // To animate view slide out from right to left
    public static void slideToLeft(View view){
        TranslateAnimation animate = new TranslateAnimation(0,-view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    // To animate view slide out from top to bottom
    public static void slideToBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    public static void slideInBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,-view.getHeight(),0);
        animate.setDuration(1000);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    // To animate view slide out from bottom to top
    public static void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    public static void alphaIn(View view, int duration){
        Animation animate = new AlphaAnimation(0, 1);
        animate.setDuration(duration);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public static void alphaOut(View view, int duration){
        Animation animate = new AlphaAnimation(1, 0);
        animate.setDuration(duration);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }


    public static void alphaInvisible(View view, int duration){
        Animation animate = new AlphaAnimation(1, 0);
        animate.setDuration(duration);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }

    public static void grayToYellow(View view){
        view.setBackgroundResource(R.drawable.color_graytoyellow);
        TransitionDrawable transitionDrawable = (TransitionDrawable) view.getBackground();
        transitionDrawable.startTransition(500);
        view.setEnabled(true);

    }
    public static void yellowToGray(View view){
        view.setBackgroundResource(R.drawable.color_yellowtogray);
        TransitionDrawable transitionDrawable = (TransitionDrawable) view.getBackground();
        transitionDrawable.startTransition(500);
        view.setEnabled(false);

    }

    public static void startTransition(View view, int duration) {
        TransitionDrawable transitionDrawable = (TransitionDrawable) view.getBackground();
        transitionDrawable.startTransition(500);

    }

    public static void reverseTransition(View view, int duration) {
        TransitionDrawable transitionDrawable = (TransitionDrawable) view.getBackground();
        transitionDrawable.reverseTransition(500);

    }


}
