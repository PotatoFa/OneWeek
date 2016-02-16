package company.twoweeks.twoweeks.Match;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import company.twoweeks.twoweeks.AutoSizeText.AutoResizeTextView;
import company.twoweeks.twoweeks.Controller.ApplicationController;
import company.twoweeks.twoweeks.Controller.ServerInterface;
import company.twoweeks.twoweeks.CallbackObject.LoginStatus;
import company.twoweeks.twoweeks.Database.MatchUser;
import company.twoweeks.twoweeks.R;
import company.twoweeks.twoweeks.TimeLine.TimelineActivity;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hoon on 2015-10-05.
 */
public class MatchAdapter extends RecyclerView.Adapter {

    private List<MatchUser> matchDataList = new ArrayList<>();

    public Activity targetActivity;



    public void setSource(List<MatchUser> matchDataList, Activity mActivity) {

        this.targetActivity = mActivity;
        this.matchDataList.clear();
        this.matchDataList.addAll(matchDataList);

        this.notifyDataSetChanged();
    }

    public void update(List<MatchUser> matchDataList){
        this.matchDataList.clear();
        this.matchDataList.addAll(matchDataList);
        this.notifyDataSetChanged();

    }
    public void push(MatchUser matchData) {

        this.matchDataList.add(0, matchData);
        this.notifyDataSetChanged();
    }

    public void clear() {

        this.matchDataList.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup group, int type) {

        View view = View.inflate(group.getContext(), R.layout.item_match, null);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MatchUser matchData = matchDataList.get(position);

        ((ViewHolder) holder).set(matchData, targetActivity);
    }

    @Override
    public int getItemCount() {
        return matchDataList != null ? matchDataList.size() : 0;
    }
}



class ViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    private AutoResizeTextView textView_country, textView_city, matchitem_notuser_message;
    private ImageView imageView, icon_match;
    private RelativeLayout matchItem, matchitem_matchuser, matchitem_notuser;
    private ServerInterface api;
    private SharedPreferences pref;

    public ViewHolder(View view) {
        super(view);

        api = ApplicationController.getInstance().getServerInterface();

        matchItem = (RelativeLayout)view.findViewById(R.id.matchitem);

        matchitem_notuser = (RelativeLayout)view.findViewById(R.id.matchitem_notuser);
        matchitem_matchuser = (RelativeLayout)view.findViewById(R.id.matchitem_matchuser);


        imageView = (ImageView)view.findViewById(R.id.matchitem_imageview);
        icon_match = (ImageView)view.findViewById(R.id.icon_match);

        textView_country = (AutoResizeTextView)view.findViewById(R.id.matchitem_country);
        textView_city = (AutoResizeTextView)view.findViewById(R.id.matchitem_city);
        matchitem_notuser_message = (AutoResizeTextView)view.findViewById(R.id.matchitem_notuser_message);

        this.context = view.getContext();

        pref = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);

    }

    Intent intent;
    public void set(final MatchUser matchData, final Activity mainActivity) {

        if (matchData.getId().equals("empty")){
            imageView.setBackgroundResource(R.drawable.match_run);
            matchitem_matchuser.setVisibility(View.INVISIBLE);
            matchitem_notuser.setVisibility(View.VISIBLE);
            matchItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    matchApply();
                    imageView.setBackgroundResource(R.drawable.match_searching);
                    matchitem_notuser_message.setText(R.string.message_waitingmatch);
                }
            });
        }
        else if(matchData.getId().equals("searching")){
            imageView.setBackgroundResource(R.drawable.match_searching);
            matchitem_matchuser.setVisibility(View.INVISIBLE);
            matchitem_notuser.setVisibility(View.VISIBLE);
            matchitem_notuser_message.setText(R.string.message_waitingmatch);

        }
        else{
            matchitem_matchuser.setVisibility(View.VISIBLE);
            matchitem_notuser.setVisibility(View.INVISIBLE);
            matchItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(mainActivity, TimelineActivity.class);
                    intent.putExtra("Country", matchData.getCountry());
                    intent.putExtra("City", matchData.getCity());
                    intent.putExtra("match_id", matchData.getId());
                    mainActivity.startActivity(intent);
                }
            });
            imageView.setImageBitmap(BitmapFactory.decodeFile(
                    matchData.getCityImage()));
            textView_country.setText(matchData.getCountry());
            textView_city.setText(matchData.getCity());
        }
    }

    private void matchApply(){
        api.getMatchApply(pref.getString("id", null),
                String.valueOf(ApplicationController.getInstance().getGpsTracker().getLatitude()),
                String.valueOf(ApplicationController.getInstance().getGpsTracker().getLongitude()),
                new Callback<LoginStatus>() {
                    @Override
                    public void success(LoginStatus loginStatus, Response response) {
                        Log.i("Match_Apply", loginStatus.getMatch());
                        if(loginStatus.getMatch().equals("Success_regi")){
                            Log.i("Match_Apply", loginStatus.getMatch());
                            updateRealm();
                        }else if(loginStatus.getMatch().equals("over_or_err")){
                            Log.i("Match_Apply", loginStatus.getMatch());
                            Toast.makeText(context, "matchApply Over or Err",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("Match_err", error.getMessage());
                    }
                });
    }

    private void updateRealm(){
        Realm realm = ApplicationController.getRealm();
        realm.beginTransaction();
        MatchUser matchUser = realm.where(MatchUser.class).equalTo("id", "empty").findFirst();
        Log.i("Update RealmDB", "search_empty matUser");
        matchUser.setId("searching");
        Log.i("Update RealmDB", "change empty db to searching db");
        realm.commitTransaction();
        Log.i("Update RealmDB", "complete");

        imageView.setImageResource(R.drawable.match_searching);
        textView_city.setText(R.string.message_waitingmatch);
        Log.i("Change view data", "set Text, ImageView ");
    }

}
