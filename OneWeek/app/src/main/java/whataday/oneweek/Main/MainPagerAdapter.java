package whataday.oneweek.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by hoon on 2016-03-07.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    int MAX_PAGE = 2;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);;
    }
    @Override
    public Fragment getItem(int position) {
        if(position<0 || MAX_PAGE<=position)
            return null;
        switch (position){
            case 0: // Fragment # 0 - This will show FirstFragment
                return MenuFragment.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return MatchFragment.newInstance();
            /*
            case 2: // Fragment # 1 - This will show SecondFragment
                return MatchFragment.newInstance();
            */
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return MAX_PAGE;
    }
}
