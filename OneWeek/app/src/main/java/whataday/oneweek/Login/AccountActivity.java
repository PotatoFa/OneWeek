package whataday.oneweek.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import whataday.oneweek.R;

public class AccountActivity extends AppCompatActivity {

    Button btn_login_facebook, btn_login_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initView();

    }

    private void initView(){
        btn_login_facebook = (Button) findViewById(R.id.btn_login_facebook);
        btn_login_google = (Button) findViewById(R.id.btn_login_google);
    }

}
