package company.twoweeks.twoweeks.Database;

import io.realm.RealmObject;

/**
 * Created by hoon on 2015-11-09.
 */
public class MatchUser extends RealmObject{


    private String id, nick, gender, age, country, city, matchedUTC, deletedUTC, cityImage;
    private int imgCount;


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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMatchedUTC() {
        return matchedUTC;
    }

    public void setMatchedUTC(String matchedUTC) {
        this.matchedUTC = matchedUTC;
    }

    public String getDeletedUTC() {
        return deletedUTC;
    }

    public void setDeletedUTC(String deletedUTC) {
        this.deletedUTC = deletedUTC;
    }

    public int getImgCount() {
        return imgCount;
    }

    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }

    public String getCityImage() {
        return cityImage;
    }

    public void setCityImage(String cityImage) {
        this.cityImage = cityImage;
    }
}
