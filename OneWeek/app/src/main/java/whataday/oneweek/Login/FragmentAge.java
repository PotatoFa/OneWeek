package whataday.oneweek.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import whataday.oneweek.CustomView.NumberPicker;
import whataday.oneweek.R;

/**
 * Created by hoon on 2016-03-06.
 */
public class FragmentAge extends android.support.v4.app.Fragment {
    View rootView;

    public static FragmentAge newInstance() {
        FragmentAge MenuFragment = new FragmentAge();
        return MenuFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout)inflater.inflate(R.layout.login_fragment_age, container, false);
        this.rootView = rootView;
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Bind(R.id.year_picker) NumberPicker year_picker;
    @Bind(R.id.month_picker) NumberPicker month_picker;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initPicker();


    }

    private void initPicker(){
        year_picker.setMinValue(1916);
        year_picker.setMaxValue(2016);
        year_picker.setValue(1992);
        year_picker.setWrapSelectorWheel(false);
        month_picker.setMinValue(1);
        month_picker.setMaxValue(12);
        month_picker.setValue(6);
        month_picker.setWrapSelectorWheel(false);
    }


    @OnClick(R.id.btn_next_age)
    public void clickNext(){

        Toast.makeText(getActivity(), year_picker.getValue() + "/" + month_picker.getValue(), Toast.LENGTH_SHORT).show();

        ((JoinActivity) getActivity()).editor.putInt("age", year_picker.getValue());
        ((JoinActivity) getActivity()).editor.commit();

        ((JoinActivity) getActivity()).user.setAge(String.valueOf(year_picker.getValue()));

        ((JoinActivity) getActivity()).nextPage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}