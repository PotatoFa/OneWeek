package whataday.oneweek.Controller;

import android.app.Application;
import android.graphics.Typeface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.tsengvn.typekit.Typekit;

import java.net.CookieManager;
import java.net.CookiePolicy;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.exceptions.RealmMigrationNeededException;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import whataday.oneweek.MainActivity;
import whataday.oneweek.Service.GPSTracker;

/**
 * Created by hoon on 2016-03-07.
 */
public class ApplicationController extends Application {

    private static  ApplicationController instance;
    public static ApplicationController getInstance() {return instance;}
    private static GPSTracker gpsTracker;
    public static GPSTracker getGpsTracker() {return gpsTracker;}
    private static Realm realm;
    public static Realm getRealm() {return realm;}

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
        ApplicationController.gpsTracker = new GPSTracker(getApplicationContext());
        createRealm();

        Typekit.getInstance()
                .addCustom1(Typeface.createFromAsset(getAssets(), "Radnika-Light.otf"))
                .addCustom2(Typeface.createFromAsset(getAssets(), "Radnika-SemiBold.otf"))
                .addCustom3(Typeface.createFromAsset(getAssets(), "Radnika-Bold.otf"))
                .addCustom4(Typeface.createFromAsset(getAssets(), "digital.ttf"));

    }

    private void createRealm(){

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("oneweek.realm")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();

        ApplicationController.realm = Realm.getInstance(realmConfiguration);

    }

    private ServerInterface api;

    public ServerInterface getServerInterface() {return api;}

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

}
