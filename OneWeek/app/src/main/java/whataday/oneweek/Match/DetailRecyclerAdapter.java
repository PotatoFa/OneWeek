package whataday.oneweek.Match;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import whataday.oneweek.CustomView.AutoResizeTextView;
import whataday.oneweek.Data.ImageData;
import whataday.oneweek.R;

/**
 * Created by hoon on 2015-10-05.
 */
public class DetailRecyclerAdapter extends RecyclerView.Adapter {

    private List<ImageData> imageDatas = new ArrayList<>();

    public Activity targetActivity;

    RelativeLayout.LayoutParams layoutParams;



    public void setSource(List<ImageData> imageDatas, Activity mActivity, int width) {

        this.targetActivity = mActivity;
        this.imageDatas.clear();
        this.imageDatas.addAll(imageDatas);
        this.layoutParams = new RelativeLayout.LayoutParams(width, width*4/3);

        this.notifyDataSetChanged();
    }

    public void update(List<ImageData> matchDataList){
        this.imageDatas.clear();
        this.imageDatas.addAll(matchDataList);
        this.notifyDataSetChanged();

    }
    public void push(ImageData imageData) {

        this.imageDatas.add(0, imageData);
        this.notifyDataSetChanged();
    }

    public void clear() {

        this.imageDatas.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup group, int type) {

        View view = View.inflate(group.getContext(), R.layout.item_detail, null);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ImageData imageData = imageDatas.get(position);

        Date now = new Date();
        SimpleDateFormat to_time = new SimpleDateFormat("aHH:mm", Locale.UK);
        SimpleDateFormat for_time = new SimpleDateFormat("aHH:mm", Locale.UK);

        String string_time_date = to_time.format(now)+" "+for_time.format(now);
        SpannableString spannableString = new SpannableString(string_time_date);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#EF4A3D")),0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE),7,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                ((ViewHolder) holder).set(imageData, layoutParams, spannableString);
    }

    @Override
    public int getItemCount() {
        return imageDatas != null ? imageDatas.size() : 0;
    }
}



class ViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    private RelativeLayout detail_textbox, detail_layout;
    private ImageView detail_background;
    private TextView detail_time;
    private AutoResizeTextView detail_text;


    private boolean visible = true;

    public ViewHolder(View view) {
        super(view);

        detail_layout = (RelativeLayout)view.findViewById(R.id.detail_layout);
        detail_textbox = (RelativeLayout)view.findViewById(R.id.detail_textbox);
        detail_background = (ImageView)view.findViewById(R.id.detail_background);
        detail_text = (AutoResizeTextView)view.findViewById(R.id.detail_text);
        detail_time = (TextView)view.findViewById(R.id.detail_time);

        this.context = view.getContext();

    }

    public void set(final ImageData imageData, RelativeLayout.LayoutParams layoutParams, SpannableString string_time_date) {

        detail_background.setBackgroundResource(R.drawable.image1);
        detail_background.setLayoutParams(layoutParams);
        detail_textbox.setLayoutParams(layoutParams);
        detail_text.setText(imageData.getText());
        detail_time.setText(string_time_date);

        detail_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible) {
                    visible = false;
                    detail_textbox.setVisibility(View.INVISIBLE);
                } else {
                    visible = true;
                    detail_textbox.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
