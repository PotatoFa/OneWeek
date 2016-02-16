package company.twoweeks.twoweeks.CallbackObject;

/**
 * Created by hoon on 2015-11-02.
 */
public class RegId {

    String Id, gcmid;

    public RegId(String id, String gcmid){
        this.Id = id;
        this.gcmid = gcmid;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getGcmid() {
        return gcmid;
    }

    public void setGcmid(String gcmid) {
        this.gcmid = gcmid;
    }
}
