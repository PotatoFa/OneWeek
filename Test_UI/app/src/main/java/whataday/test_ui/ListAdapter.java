package whataday.test_ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;


import java.util.ArrayList;

/**
 * Created by hoon on 2016-02-19.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<ListData> datas;
    private LayoutInflater inflater;

    public ListAdapter(Context context, int layout, ArrayList<ListData> datas) {
        this.context = context;
        this.layout = layout;
        this.datas = datas;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ListData getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;


        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        RelativeLayout back_layout = (RelativeLayout)convertView.findViewById(R.id.back_layout);
        back_layout.setBackgroundResource(R.drawable.test);

        return convertView;
    }

}
