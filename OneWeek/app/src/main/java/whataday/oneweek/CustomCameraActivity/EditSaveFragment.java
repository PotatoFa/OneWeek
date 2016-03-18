package whataday.oneweek.CustomCameraActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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


    public static EditSaveFragment newInstance(Bitmap bitmap, int bottom_cover, int img_width, int img_height) {
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

    RelativeLayout bottom_cover, top_cover;
    RelativeLayout.LayoutParams image_save_param, bottom_cover_param;
    ImageView image_save;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        top_cover = (RelativeLayout)rootView.findViewById(R.id.top_cover);
        bottom_cover = (RelativeLayout)rootView.findViewById(R.id.bottom_cover);
        image_save = (ImageView)rootView.findViewById(R.id.image_save);

        bottom_cover_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bottom_cover_height);
        bottom_cover_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        bottom_cover.setLayoutParams(bottom_cover_param);

        image_save_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, image_height);
        image_save_param.addRule(RelativeLayout.BELOW, R.id.top_cover);
        image_save_param.addRule(RelativeLayout.ABOVE, R.id.bottom_cover);
        image_save.setLayoutParams(image_save_param);

        image_save.setImageBitmap(getBitmap);
        Log.i("EDITSAVE : ", String.valueOf(getBitmap.getByteCount()));



    }




    @Override
    public void onResume() {
        super.onResume();
    }


    int convert_int_map(int input, int input_min, int input_max, int convert_min, int convert_max){
        return ( ((input - input_min) * (convert_max - convert_min)) / (input_max - input_min) ) + convert_min;
    }


}