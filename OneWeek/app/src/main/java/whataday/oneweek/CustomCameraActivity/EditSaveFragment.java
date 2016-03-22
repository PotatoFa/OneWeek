package whataday.oneweek.CustomCameraActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import whataday.oneweek.CustomView.AutoResizeEditText;
import whataday.oneweek.R;

/**
 * Created by jaehun on 16. 3. 18..
 */
public class EditSaveFragment extends android.support.v4.app.Fragment {

    View rootView;

    static Bitmap getBitmap;
    static int bottom_cover_height;
    static int image_width;
    static int image_height;


    public static EditSaveFragment newInstance(Bitmap bitmap, int bottom_cover, int img_width, int img_height,
                                               int view_width) {
        EditSaveFragment editSaveFragment = new EditSaveFragment();
        getBitmap = bitmap;
        bottom_cover_height = bottom_cover;
        image_width = img_width;
        image_height = img_height;

        return editSaveFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout)inflater.inflate(R.layout.camera_fragment_editsave, container, false);
        this.rootView = rootView;
        return rootView;

    }

    RelativeLayout bottom_cover, top_cover, image_box;
    RelativeLayout.LayoutParams bottom_cover_param, image_box_param;
    ImageView image_save;
    AutoResizeEditText edit_save_text;
    ImageButton btn_save_send, btn_save_text, btn_save_cancel;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initView > setViewSize > setEvent
        initView();

    }

    private void initView(){

        top_cover = (RelativeLayout)rootView.findViewById(R.id.top_cover);
        bottom_cover = (RelativeLayout)rootView.findViewById(R.id.bottom_cover);

        image_box = (RelativeLayout)rootView.findViewById(R.id.image_box);
        image_save = (ImageView)rootView.findViewById(R.id.image_save);
        edit_save_text = (AutoResizeEditText)rootView.findViewById(R.id.edit_save_text);

        btn_save_send = (ImageButton)rootView.findViewById(R.id.btn_save_send);
        btn_save_text = (ImageButton)rootView.findViewById(R.id.btn_save_text);
        btn_save_cancel = (ImageButton)rootView.findViewById(R.id.btn_save_cancel);

        setViewSize();


    }

    private void setViewSize(){

        if(getBitmap.getHeight() > getBitmap.getWidth()){
            //세로 이미지
            image_box_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getBitmap.getHeight());
            image_box_param.addRule(RelativeLayout.BELOW, top_cover.getId());
            image_box.setLayoutParams(image_box_param);
            image_save.setImageBitmap(getBitmap);


            /*
            bottom_cover_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            bottom_cover_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            bottom_cover_param.addRule(RelativeLayout.BELOW, image_save.getId());
            bottom_cover.setLayoutParams(bottom_cover_param);
            */
        }else{
            //가로 이미지


            float scale = (float) getBitmap.getHeight() / getBitmap.getWidth();

            Log.i("Change scale", String.valueOf(scale));

            int change_height = (int) (getBitmap.getHeight() * scale);

            Log.i("Change HEIGHT", String.valueOf(change_height));



            image_box_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, change_height);
            image_box_param.addRule(RelativeLayout.BELOW, top_cover.getId());
            image_box.setLayoutParams(image_box_param);
            image_save.setImageBitmap(getBitmap);

        }


        Log.i("BITMAP width: ", String.valueOf(getBitmap.getWidth()));
        Log.i("BITMAP height: ", String.valueOf(getBitmap.getHeight()));
        Log.i("EDITSAVE : ", String.valueOf(getBitmap.getByteCount()));

        setEvent();
    }

    private void setEvent(){
        btn_save_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //에디트텍스트 포커싱 / 키보드
            }
        });
        btn_save_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((CameraFragmentActivity)getActivity()).onCancel(v);
                //캔슬버튼 백스택프래그먼트

            }
        });
        btn_save_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bitmap객체 전달해서 센드 프래그먼트로 이동
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                        .replace(
                                R.id.custom_fragment_container,
                                SendFragment.newInstance(getBitmap))
                        .addToBackStack(null)
                        .commit();
            }
        });

    }




    @Override
    public void onResume() {
        super.onResume();
    }



}