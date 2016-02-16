package company.twoweeks.twoweeks.TimeLine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import company.twoweeks.twoweeks.AutoSizeText.AutoResizeTextView;
import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.Database.ImageData;
import company.twoweeks.twoweeks.Database.ImageInfo;
import company.twoweeks.twoweeks.R;
import io.realm.Realm;

/**
 * Created by hoon on 2015-10-11.
 */
public class TimelineAdapter extends RecyclerView.Adapter {

    private List<ImageInfo> timelineDataList = new ArrayList<>();

    public void setSource(List<ImageInfo> timelineDataList) {

        this.timelineDataList.clear();
        this.timelineDataList.addAll(timelineDataList);

        this.notifyDataSetChanged();
    }

    public void push(ImageInfo timelineData) {

        this.timelineDataList.add(0, timelineData);
        this.notifyDataSetChanged();
    }

    public void clear() {

        this.timelineDataList.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup group, int type) {

        View view = View.inflate(group.getContext(), R.layout.item_timeline, null);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ImageInfo timelineData = timelineDataList.get(position);

        ((ViewHolder) holder).set(timelineData);
    }

    @Override
    public int getItemCount() {
        return timelineDataList != null ? timelineDataList.size() : 0;
    }
}



class ViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    private boolean visible = true;

    private AutoResizeTextView timelineitem_text ;

    private TextView timelineitem_toltc, timelineitem_fromltc;

    private ImageView timelineitem_image;

    private RelativeLayout timelineitem_view;

    private Realm realm;

    public ViewHolder(View view) {
        super(view);

        realm = ApplicationController.getInstance().getRealm();

        timelineitem_view = (RelativeLayout)view.findViewById(R.id.timelineitem_view);
        timelineitem_image = (ImageView)view.findViewById(R.id.timelineitem_image);
        timelineitem_text = (AutoResizeTextView)view.findViewById(R.id.timelineitem_text);
        timelineitem_toltc = (TextView)view.findViewById(R.id.timelineitem_toltc);
        timelineitem_fromltc = (TextView)view.findViewById(R.id.timelineitem_fromltc);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visible) {
                    timelineitem_view.setVisibility(View.INVISIBLE);
                    visible = false;
                }else{
                    timelineitem_view.setVisibility(View.VISIBLE);
                    visible = true;
                }
            }
        });


        this.context = view.getContext();



    }

    public void set(final ImageInfo timelineData) {

        ImageData imageData = realm.where(ImageData.class).equalTo("fileName", timelineData.getFileName()).findFirst();

        //timelineitem_image.
        timelineitem_text.setText(imageData.getText());
        timelineitem_toltc.setText(timelineData.getToLTC());
        timelineitem_fromltc.setText(timelineData.getFromLTC());

    }
}
