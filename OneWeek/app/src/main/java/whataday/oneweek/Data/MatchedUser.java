package whataday.oneweek.Data;

import io.realm.RealmObject;

/**
 * Created by jaehun on 16. 3. 13..
 */
public class MatchedUser extends RealmObject {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
