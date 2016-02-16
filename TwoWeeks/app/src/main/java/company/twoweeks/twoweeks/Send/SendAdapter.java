package company.twoweeks.twoweeks.Send;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import company.twoweeks.twoweeks.Database.MatchUser;
import company.twoweeks.twoweeks.R;

/**
 * Created by hoon on 2015-10-05.
 */
public class SendAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<MatchUser> datas;
    private LayoutInflater inflater;

    public SendAdapter(Context context, int layout, ArrayList<MatchUser> datas) {
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
    public MatchUser getItem(int position) {
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

        ImageView imageView = (ImageView)convertView.findViewById(R.id.senditem_imageview);
        TextView textView_country = (TextView)convertView.findViewById(R.id.senditem_country);
        TextView textView_city = (TextView)convertView.findViewById(R.id.senditem_city);

        //imageView.setImageResource(datas.get(pos).getImage());
        imageView.setBackgroundResource(R.drawable.select1);
        textView_country.setText(datas.get(pos).getCountry());
        textView_city.setText(datas.get(pos).getCity());


        return convertView;
    }

}
