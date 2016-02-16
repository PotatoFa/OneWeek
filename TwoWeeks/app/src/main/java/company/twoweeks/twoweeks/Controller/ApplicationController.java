package company.twoweeks.twoweeks.Controller;

import android.app.Application;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by hoon on 2015-10-16.
 */
public class ApplicationController extends Application {

    private static Realm realm;
    public static Realm getRealm(){ return realm; }

    private static GPSTracker gpsTracker;
    public GPSTracker getGpsTracker(){return gpsTracker;}
    //gpsTracker

    private LoginManager loginManager;
    public void setLoginManager(LoginManager loginManager){
        this.loginManager = loginManager;
    }
    public LoginManager getLoginManager(){return loginManager;}

    private ServerInterface api;
    public ServerInterface getServerInterface() {return api;}



    private static  ApplicationController instance;
    public static ApplicationController getInstance() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
        ApplicationController.gpsTracker = new GPSTracker(getInstance());
        ApplicationController.realm = realm.getInstance(this);

        /*
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
*/

    }


    public void buildServerInterface(String ip) {
        if (api != null)
            return;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient client = new OkHttpClient();
        client.setCookieHandler(cookieManager);

        RestAdapter.Builder builder = new RestAdapter.Builder();

        builder.setConverter(new GsonConverter(gson));
        builder.setEndpoint(getEndpoint(ip));
        builder.setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS);
        builder.setClient(new OkClient(client));

        RestAdapter adapter = builder.build();

        api = adapter.create(ServerInterface.class);

    }

    public String getEndpoint(String ip) {

        return String.format("http://%s", ip);
    }

    /*
    public String getEndpoint2(String ip, int port) {

        return String.format("http://%s:%d", ip, port);
    }

    public void buildServerInterface(String ip, int port) {
        if (api != null)
            return;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient client = new OkHttpClient();
        client.setCookieHandler(cookieManager);

        RestAdapter.Builder builder = new RestAdapter.Builder();

        builder.setConverter(new GsonConverter(gson));
        builder.setEndpoint(getEndpoint2(ip, port));
        builder.setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS);
        builder.setClient(new OkClient(client));

        RestAdapter adapter = builder.build();

        api = adapter.create(ServerInterface.class);

    }
*/
}
