package whataday.test_ui.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import whataday.test_ui.R;

/**
 * Created by hoon on 2016-02-23.
 */
public class MenuFragment extends android.support.v4.app.Fragment {
    View rootView;

    public static MenuFragment newInstance() {
        MenuFragment MenuFragment = new MenuFragment();
        return MenuFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout rootView = (LinearLayout)inflater.inflate(R.layout.fragment_menu, container, false);
        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
