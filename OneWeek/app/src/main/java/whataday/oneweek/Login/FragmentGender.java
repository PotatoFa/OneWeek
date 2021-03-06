package whataday.oneweek.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import whataday.oneweek.R;

/**
 * Created by hoon on 2016-03-06.
 */
public class FragmentGender extends android.support.v4.app.Fragment {
    View rootView;

    public static FragmentGender newInstance() {
        FragmentGender MenuFragment = new FragmentGender();
        return MenuFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout)inflater.inflate(R.layout.login_fragment_gender, container, false);
        this.rootView = rootView;
        return rootView;

        //TODO 남/여 스위치버튼
    }

    Button btn_next;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((JoinActivity) getActivity()).nextPage();
            }
        });

    }
}