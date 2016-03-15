package whataday.oneweek.Data;

import io.realm.RealmObject;

/**
 * Created by jaehun on 16. 3. 13..
 */
public class MatchedUser extends RealmObject {
    private String id;
    private String city;
    private String country;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    private String test;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
