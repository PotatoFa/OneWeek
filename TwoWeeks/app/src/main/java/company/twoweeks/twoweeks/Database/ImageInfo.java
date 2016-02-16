package company.twoweeks.twoweeks.Database;

import io.realm.RealmObject;

/**
 * Created by hoon on 2015-11-28.
 */
public class ImageInfo extends RealmObject {

    private String fromUserId;
    private String toUserId;
    private String toLTC;
    private String fromLTC;
    private String UTC;
    private String latitude;
    private String longitude;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToLTC() {
        return toLTC;
    }

    public void setToLTC(String toLTC) {
        this.toLTC = toLTC;
    }

    public String getFromLTC() {
        return fromLTC;
    }

    public void setFromLTC(String fromLTC) {
        this.fromLTC = fromLTC;
    }

    public String getUTC() {
        return UTC;
    }

    public void setUTC(String UTC) {
        this.UTC = UTC;
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
