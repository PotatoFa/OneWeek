package whataday.oneweek.CustomCameraActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import android.widget.Toast;

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


    public static SendFragment newInstance() {
        SendFragment sendFragment = new SendFragment();

        return sendFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBitmap = ((CameraFragmentActivity) getActivity()).bitmap;
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
        for(final MatchedUser matchedUser : results){

            final RelativeLayout matched_user = (RelativeLayout) inflater.inflate(R.layout.send_matched_user, null);

            RelativeLayout.LayoutParams item_param =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
            matched_user.setLayoutParams(item_param);
            TextView user_country = (TextView) matched_user.findViewById(R.id.user_country);
            TextView user_city = (TextView) matched_user.findViewById(R.id.user_city);

            final ImageView user_image = (ImageView) matched_user.findViewById(R.id.user_image);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.test2);

            user_image.setImageBitmap(bm);

            user_country.setText(matchedUser.getCountry());
            user_city.setText(matchedUser.getCity());

            user_list.addView(matched_user);

            matched_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("CLCLCLCL", matchedUser.getCity());

                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0); //0 means grayscale
                        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
                    if(user_image.getColorFilter() == null){
                        user_image.setColorFilter(cf);
                    }else{
                        user_image.clearColorFilter();
                    }

                }
            });

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