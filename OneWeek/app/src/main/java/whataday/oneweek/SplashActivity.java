package whataday.oneweek;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import whataday.oneweek.Login.AccountActivity;

public class SplashActivity extends AppCompatActivity {

    Handler h = new Handler(Looper.getMainLooper());
    ImageView image_splash;
    int count = 0;
    int image_resource[] = { R.drawable.splash1, R.drawable.splash2, R.drawable.splash3, R.drawable.splash4,
            R.drawable.splash5, R.drawable.splash6, R.drawable.splash7, R.drawable.splash8, R.drawable.splash9 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        image_splash = (ImageView)findViewById(R.id.image_splash);

        h.postDelayed(changeImage, 30);

    }


    Runnable changeImage = new Runnable() {
        @Override
        public void run() {
            if( count > image_resource.length-1 ){
                h.removeCallbacks(changeImage);
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                finish();
            }else{
                image_splash.setImageResource(image_resource[count++]);
                h.postDelayed(changeImage, 30);
            }
        }
    };
}
