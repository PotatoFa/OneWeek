package whataday.oneweek.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import whataday.oneweek.CustomView.ViewAnimation;
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

    }

    Button btn_next;
    RadioGroup radio_group_gender;
    String gender;
    boolean checked_flag = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), gender, Toast.LENGTH_SHORT).show();
                ((JoinActivity) getActivity()).nextPage();
            }
        });

        radio_group_gender = (RadioGroup) rootView.findViewById(R.id.radio_group_gender);
        radio_group_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_man) {
                    gender = "man";
                } else if (checkedId == R.id.radio_woman) {
                    gender = "woman";
                }

                if (!checked_flag) {

                    checked_flag = true;
                    ViewAnimation.grayToYellow(btn_next);



                }
            }
        });


    }
}