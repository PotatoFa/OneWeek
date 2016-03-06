package whataday.oneweek.Login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by hoon on 2016-03-06.
 */
public class JoinPagerAdapter extends FragmentPagerAdapter {

    int MAX_PAGE = 4;

    public JoinPagerAdapter(FragmentManager fm) {
        super(fm);;
    }

    @Override
    public Fragment getItem(int position) {
        if(position<0 || MAX_PAGE<=position)
            return null;
        switch (position){
            case 0: // Fragment # 0 - This will show FirstFragment
                return FragmentGender.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return FragmentAge.newInstance();
            case 2: // Fragment # 1 - This will show SecondFragment
                return FragmentCountry.newInstance();
            case 3: // Fragment # 1 - This will show SecondFragment
                return FragmentName.newInstance();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return MAX_PAGE;
    }
}
