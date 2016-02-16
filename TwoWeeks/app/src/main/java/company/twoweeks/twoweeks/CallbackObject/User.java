package company.twoweeks.twoweeks.CallbackObject;

/**
 * Created by hoon on 2015-10-31.
 */
public class User {

    private String id, gender, age, nick, latitude, longitude, token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(String id, String gender, String age, String nick, String latitude, String longitude, String token){
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.nick = nick;
        this.latitude = latitude;
        this.longitude = longitude;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
