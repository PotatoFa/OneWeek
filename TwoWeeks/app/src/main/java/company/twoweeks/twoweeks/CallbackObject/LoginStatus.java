package company.twoweeks.twoweeks.CallbackObject;

/**
 * Created by hoon on 2015-10-18.
 */
public class LoginStatus {
    public String getId_status() {
        return id_status;
    }

    public void setId_status(String id_status) {
        this.id_status = id_status;
    }

    public String getMatch() {
        return Match;
    }

    public void setMatch(String match) {
        Match = match;
    }

    String id_status, nick_search, Match, test;

    public String getNick_search() {
        return nick_search;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public void setNick_search(String nick_search) {
        this.nick_search = nick_search;
    }
}
