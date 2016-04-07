package whataday.oneweek.CustomView;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.ButterKnife;
import whataday.oneweek.Controller.ApplicationController;

/**
 * Created by hoon on 2016-03-08.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
        //커스텀font 설정
    }

    @Override
    protected void onPause() {
        super.onPause();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ButterKnife.bind(this);
    }
}
