package company.twoweeks.twoweeks.CallbackObject;

import java.util.ArrayList;

/**
 * Created by hoon on 2015-11-08.
 */
public class userList {

    String Redirect;
    ArrayList<MatchedUser> userList;
    String count;
    ArrayList<imgcount> imgcount;

    public String getRedirect() {
        return Redirect;
    }

    public void setRedirect(String redirect) {
        Redirect = redirect;
    }

    public ArrayList<MatchedUser> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<MatchedUser> userList) {
        this.userList = userList;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<imgcount> getImgcount() {
        return imgcount;
    }

    public void setImgcount(ArrayList<imgcount> imgcount) {
        this.imgcount = imgcount;
    }
}
