package whataday.oneweek.Login;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
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

    Button btn_next_gender;
    RadioGroup radio_group_gender;
    RadioButton radio_man, radio_woman;

    String gender;
    boolean checked_flag = false;
    int currentCheck;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        btn_next_gender = (Button) rootView.findViewById(R.id.btn_next_gender);
        radio_man = (RadioButton) rootView.findViewById(R.id.radio_man);
        radio_woman = (RadioButton) rootView.findViewById(R.id.radio_woman);

        btn_next_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "save your gender : "+gender, Toast.LENGTH_SHORT).show();
                ((JoinActivity) getActivity()).editor.putString("gender", gender);
                ((JoinActivity) getActivity()).editor.commit();

                ((JoinActivity) getActivity()).nextPage();
            }
        });

        radio_group_gender = (RadioGroup) rootView.findViewById(R.id.radio_group_gender);



        radio_group_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if(!checked_flag){
                    checked_flag = true;
                    ViewAnimation.grayToYellow(btn_next_gender);
                    ViewAnimation.alphaOut(rootView.findViewById(R.id.background_image), 500);
                    if(checkedId == R.id.radio_man){
                        ViewAnimation.startTransition(radio_man, 300);
                        currentCheck = R.id.radio_man;
                        gender = "man";
                    }else{
                        ViewAnimation.startTransition(radio_woman, 300);
                        currentCheck = R.id.radio_woman;
                        gender = "woman";

                    }
                }

                if(!(currentCheck == checkedId)){
                    if(checkedId == R.id.radio_man){
                        currentCheck = R.id.radio_man;
                        gender = "man";
                        ViewAnimation.startTransition(radio_man, 300);
                        ViewAnimation.reverseTransition(radio_woman, 300);
                    }else{
                        currentCheck = R.id.radio_woman;
                        gender = "woman";
                        ViewAnimation.startTransition(radio_woman, 300);
                        ViewAnimation.reverseTransition(radio_man, 300);
                    }

                }
            }
        });


    }

}