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
        return rootView;

    }

    TextView text_country;
    Button btn_next_country;
    GPSTracker gpsTracker;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gpsTracker = ApplicationController.getGpsTracker();

        btn_next_country = (Button) rootView.findViewById(R.id.btn_next_country);
        text_country = (TextView) rootView.findViewById(R.id.text_country);

        text_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( gpsTracker.canGetLocation() ){
                    ViewAnimation.grayToYellow(btn_next_country);
                    ViewAnimation.alphaOut(rootView.findViewById(R.id.background_image));
                    //Location 정보 사용가능
                    Toast.makeText(getActivity(), gpsTracker.getLatitude()+"/"+gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
                    //TODO getLocation 유효성 검사 및 서버에 위치요청

                    text_country.setText(gpsTracker.getLatitude()+"/"+gpsTracker.getLongitude());
                }else{
                    Log.i("Fragment_Country", "can't Get Location");
                }


            }
        });


        btn_next_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((JoinActivity) getActivity()).nextPage();
            }
        });

    }

}