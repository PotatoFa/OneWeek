package whataday.oneweek.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import whataday.oneweek.R;


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
        RelativeLayout rootView = (RelativeLayout)inflater.inflate(R.layout.main_fragment_menu, container, false);
        this.rootView = rootView;
        return rootView;
    }
    RelativeLayout btn_menu_back;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_menu_back = (RelativeLayout)rootView.findViewById(R.id.btn_menu_back);
        btn_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPagerActivity)getActivity()).changeViewPager(1);
            }
        });



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

}
