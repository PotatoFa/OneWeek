package whataday.oneweek.Data;

import io.realm.RealmObject;

/**
 * Created by jaehun on 16. 4. 9..
 */
public class Picture extends RealmObject {

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    private byte[] rawData;

}
