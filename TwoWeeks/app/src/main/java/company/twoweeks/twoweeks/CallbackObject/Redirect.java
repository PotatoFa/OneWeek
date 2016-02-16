package company.twoweeks.twoweeks.CallbackObject;

import java.util.ArrayList;

/**
 * Created by hoon on 2015-11-06.
 */
public class Redirect {

    String id ;
    ArrayList<String> matUser;

    public Redirect(String id, ArrayList<String> matUser){
        this.id = id;
        this.matUser = matUser;
    }

    public ArrayList<String> getMatUser() {
        return matUser;
    }

    public void setMatUser(ArrayList<String> matUser) {
        this.matUser = matUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
