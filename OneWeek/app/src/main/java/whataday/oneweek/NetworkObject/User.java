package whataday.oneweek.NetworkObject;

/**
 * Created by jaehun on 16. 3. 25..
 */
public class User {

    private String id;
    private String nick;
    private String gender;
    private String token;
    private String age;
    private float latitude, longitude;
/*
    public User(String id, String nick, String gender, String age, String token,
    float latitude, float longitude) {
        this.id = id;
        this.nick = nick;
        this.age = age;
        this.gender = gender;
        this.token = token;
        this.latitude = latitude;
        this.longitude = longitude;
    }
*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }


}
