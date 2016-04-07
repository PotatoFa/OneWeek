package whataday.oneweek.Login;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomView.ViewAnimation;
import whataday.oneweek.R;
import whataday.oneweek.Service.GPSTracker;

/**
 * Created by hoon on 2016-03-06.
 */
public class FragmentCountry extends android.support.v4.app.Fragment {
    View rootView;

    public static FragmentCountry newInstance() {
        FragmentCountry MenuFragment = new FragmentCountry();
        return MenuFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout)inflater.inflate(R.layout.login_fragment_country, container, false);
        this.rootView = rootView;
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    GPSTracker gpsTracker;
    boolean btn_check = false;

    @Bind(R.id.btn_next_country) Button btn_next_country;
    @OnClick(R.id.btn_next_country)
    public void clickNext(){
        ((JoinActivity) getActivity()).editor
                .putFloat("latitude", Float.parseFloat(String.valueOf(gpsTracker.getLatitude())));

        ((JoinActivity) getActivity()).editor
                .putFloat("longitude", Float.parseFloat(String.valueOf(gpsTracker.getLongitude())));

        //TODO 나라/도시 추가해야함 서버요청

        ((JoinActivity) getActivity()).user.setLatitude(Float.parseFloat(String.valueOf(gpsTracker.getLatitude())));
        ((JoinActivity) getActivity()).user.setLongitude(Float.parseFloat(String.valueOf(gpsTracker.getLongitude())));


        ((JoinActivity) getActivity()).editor.commit();
        ((JoinActivity) getActivity()).nextPage();
    }

    @OnClick(R.id.text_country)
    public void clickCountry(TextView textView){
        if(gpsTracker.canGetLocation()){
            if(!btn_check){
                ViewAnimation.grayToYellow(btn_next_country);
                ViewAnimation.alphaOut(rootView.findViewById(R.id.background_image), 500);
                btn_check = true;
            }
            //Location 정보 사용가능
            Toast.makeText(getActivity(), gpsTracker.getLatitude()+"/"+gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
            //TODO getLocation 유효성 검사 및 서버에 위치요청

            textView.setText(gpsTracker.getLatitude()+"/"+gpsTracker.getLongitude());
        }else{
            Log.i("Fragment_Country", "can't Get Location");
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ApplicationController.setGpsTracker(getActivity());
        gpsTracker = ApplicationController.getGpsTracker();


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

}