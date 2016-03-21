package whataday.oneweek.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import whataday.oneweek.CustomView.SetFontActivity;
import whataday.oneweek.R;

public class TermsActivity extends SetFontActivity {

    RelativeLayout btn_account_done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        btn_account_done = (RelativeLayout)findViewById(R.id.btn_account_done);
        btn_account_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
