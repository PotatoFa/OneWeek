package whataday.oneweek.CustomCameraActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import whataday.oneweek.Controller.ApplicationController;
import whataday.oneweek.CustomView.AutoResizeEditText;
import whataday.oneweek.Data.MatchedUser;
import whataday.oneweek.R;

/**
 * Created by jaehun on 16. 3. 18..
 */
public class SendFragment extends android.support.v4.app.Fragment {

    View rootView;
    Realm realm;

    static Bitmap getBitmap;


    public static SendFragment newInstance(Bitmap bitmap) {
        SendFragment sendFragment = new SendFragment();
        getBitmap = bitmap;

        return sendFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout)inflater.inflate(R.layout.camera_fragment_send, container, false);
        this.rootView = rootView;
        return rootView;

    }

    ImageButton btn_send_cancel, btn_send;
    LayoutInflater inflater;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = ApplicationController.getRealm();
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //initView > setEvent
        initView();


    }

    private void initView(){
        btn_send_cancel = (ImageButton)rootView.findViewById(R.id.btn_send_cancel);
        btn_send = (ImageButton)rootView.findViewById(R.id.btn_send);

        setEvent();
        setUserList();

    }

    private void setUserList(){
        final LinearLayout user_list = (LinearLayout)rootView.findViewById(R.id.user_list);

        RealmResults<MatchedUser> results =
                realm.where(MatchedUser.class).notEqualTo("id", "empty").findAll();
        for(MatchedUser matchedUser : results){

            RelativeLayout matched_user = (RelativeLayout) inflater.inflate(R.layout.send_matched_user, null);

            matched_user.setBackgroundResource(R.drawable.test4);

            RelativeLayout.LayoutParams item_param =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
            matched_user.setLayoutParams(item_param);
            TextView user_country = (TextView) matched_user.findViewById(R.id.user_country);
            TextView user_city = (TextView) matched_user.findViewById(R.id.user_city);

            user_country.setText(matchedUser.getCountry());
            user_city.setText(matchedUser.getCity());

            user_list.addView(matched_user);

        }
    }


    private void setEvent(){
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 서버전송 & DB저장
                getActivity().finish();

            }
        });
        btn_send_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CameraFragmentActivity)getActivity()).onCancel(v);
            }
        });

    }




    @Override
    public void onResume() {
        super.onResume();
    }



}