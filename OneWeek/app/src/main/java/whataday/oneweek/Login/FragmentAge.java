package whataday.oneweek.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import whataday.oneweek.CustomView.ViewAnimation;
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
        return rootView;

    }

    Button btn_next_age;
    whataday.oneweek.CustomView.NumberPicker year_picker, month_picker;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        btn_next_age = (Button) rootView.findViewById(R.id.btn_next_age);
        year_picker = (whataday.oneweek.CustomView.NumberPicker) rootView.findViewById(R.id.year_picker);
        month_picker = (whataday.oneweek.CustomView.NumberPicker) rootView.findViewById(R.id.month_picker);

        initPicker();

        btn_next_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), year_picker.getValue() + "/" + month_picker.getValue(), Toast.LENGTH_SHORT).show();

                ((JoinActivity) getActivity()).editor.putInt("age", year_picker.getValue());
                ((JoinActivity) getActivity()).editor.commit();
                ((JoinActivity) getActivity()).nextPage();
            }
        });


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

}