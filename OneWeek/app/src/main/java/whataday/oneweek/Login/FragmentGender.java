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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
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
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    String gender;
    boolean checked_flag = false;
    int currentCheck;


    @Bind(R.id.btn_next_gender) Button btn_next_gender;
    @OnClick(R.id.btn_next_gender)
    public void next_gender(){
        Toast.makeText(getActivity(), "save your gender : "+gender, Toast.LENGTH_SHORT).show();
        ((JoinActivity) getActivity()).editor.putString("gender", gender);
        ((JoinActivity) getActivity()).editor.commit();

        ((JoinActivity) getActivity()).user.setGender(gender);

        ((JoinActivity) getActivity()).nextPage();
    }

    @Bind(R.id.radio_man) RadioButton radio_man;
    @Bind(R.id.radio_woman) RadioButton radio_woman;
    @OnClick({ R.id.radio_man, R.id.radio_woman })
    public void onRadioButtonClicked(RadioButton radioButton) {

        if(!checked_flag){
            checked_flag = true;
            ViewAnimation.grayToYellow(btn_next_gender);
            ViewAnimation.alphaOut(rootView.findViewById(R.id.background_image), 500);
            if(radioButton.getId() == R.id.radio_man){
                ViewAnimation.startTransition(radio_man, 300);
                currentCheck = R.id.radio_man;
                gender = "man";
            }else{
                ViewAnimation.startTransition(radio_woman, 300);
                currentCheck = R.id.radio_woman;
                gender = "woman";

            }
        }

        if(!(currentCheck == radioButton.getId())){
            if(radioButton.getId() == R.id.radio_man){
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

}