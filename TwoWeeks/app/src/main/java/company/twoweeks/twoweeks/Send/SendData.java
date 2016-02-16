package company.twoweeks.twoweeks.Send;

/**
 * Created by hoon on 2015-10-05.
 */
public class SendData {

    private String country;
    private String city;
    private int image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public SendData(int image, String country, String city){
        this.image = image;
        this.country = country;
        this.city = city;

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

}
