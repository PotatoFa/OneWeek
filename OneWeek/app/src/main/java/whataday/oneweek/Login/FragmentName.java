package whataday.oneweek.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import whataday.oneweek.R;

/**
 * Created by hoon on 2016-03-06.
 */
public class FragmentName extends android.support.v4.app.Fragment {
    View rootView;

    public static FragmentName newInstance() {
        FragmentName MenuFragment = new FragmentName();
        return MenuFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout rootView = (LinearLayout)inflater.inflate(R.layout.login_fragment_name, container, false);
        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}