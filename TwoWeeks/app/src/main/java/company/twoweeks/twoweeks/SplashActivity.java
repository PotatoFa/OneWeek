package company.twoweeks.twoweeks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.Controller.ServerInterface;
import company.twoweeks.twoweeks.Login.LoginActivity;


public class SplashActivity extends AppCompatActivity {

    ApplicationController applicationController;
    ServerInterface api;
    Handler h = new Handler(Looper.getMainLooper());
    ImageView image_splash;
    Button btn_send;
    int count=0;
    int image_resource[] = { R.drawable.splash1, R.drawable.splash2, R.drawable.splash3, R.drawable.splash4,
            R.drawable.splash5, R.drawable.splash6, R.drawable.splash7, R.drawable.splash8, R.drawable.splash9 };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        applicationController = ApplicationController.getInstance();
        applicationController.buildServerInterface("ec2-54-92-43-111.ap-northeast-1.compute.amazonaws.com");

        api = applicationController.getServerInterface();

        image_splash = (ImageView)findViewById(R.id.image_splash);
        btn_send = (Button)findViewById(R.id.btn_sendtest);

        h.postDelayed(changeImage, 30);
    }

    Runnable changeImage = new Runnable() {
        @Override
        public void run() {
            if( count > image_resource.length-1 ){
                Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_SHORT).show();
                h.removeCallbacks(changeImage);
                //startActivity(new Intent(getApplicationContext(), JoinActivity.class));
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                //startActivity(new Intent(getApplicationContext(), ImageActivity.class));
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }else{
                image_splash.setImageResource(image_resource[count++]);
                h.postDelayed(changeImage, 30);
            }
        }
    };

}
