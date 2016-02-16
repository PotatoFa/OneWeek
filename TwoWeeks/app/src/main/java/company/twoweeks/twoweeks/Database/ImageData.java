package company.twoweeks.twoweeks.Database;

import java.io.File;

import io.realm.RealmObject;

/**
 * Created by hoon on 2015-11-28.
 */
public class ImageData extends RealmObject {
    private String fileName, text;
    private byte[] data;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
