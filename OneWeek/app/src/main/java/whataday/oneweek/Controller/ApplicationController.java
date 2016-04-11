package whataday.oneweek.Controller;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

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
import io.realm.RealmSchema;
import io.realm.exceptions.RealmMigrationNeededException;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import whataday.oneweek.Data.RealmModule;
import whataday.oneweek.MainActivity;
import whataday.oneweek.R;
import whataday.oneweek.Service.GPSTracker;

/**
 * Created by hoon on 2016-03-07.
 */
public class ApplicationController extends Application {

    private static  ApplicationController instance;
    public static ApplicationController getInstance() {return instance;}
    private static GPSTracker gpsTracker;
    public static void setGpsTracker(Context context) {ApplicationController.gpsTracker = new GPSTracker(context);}
    public static GPSTracker getGpsTracker() {return gpsTracker;}
    private static Realm realm;
    public static Realm getRealm() {return realm;}
    private static ServerInterface api;
    public static ServerInterface getServerInterface() {return api;}

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
        buildServerInterface(getResources().getString(R.string.server_path));

        setDefaultRealm("");

        Typekit.getInstance()
                .addCustom1(Typeface.createFromAsset(getAssets(), "Radnika-Light.otf"))
                .addCustom2(Typeface.createFromAsset(getAssets(), "Radnika-Regular.otf"))
                .addCustom3(Typeface.createFromAsset(getAssets(), "Radnika-Medium.otf"))
                .addCustom4(Typeface.createFromAsset(getAssets(), "Radnika-Bold.otf"))
                .addCustom5(Typeface.createFromAsset(getAssets(), "Radnika-Light.otf"))
                .addCustom6(Typeface.createFromAsset(getAssets(), "Radnika-SemiBold.otf"))
                .addCustom7(Typeface.createFromAsset(getAssets(), "digital.ttf"))
                .addCustom8(Typeface.createFromAsset(getAssets(), "digital_mono.ttf"));

    }

    private void createRealm(){

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("test.realm")
                .schemaVersion(4)
                .deleteRealmIfMigrationNeeded()
                .build();

        ApplicationController.realm = Realm.getInstance(realmConfiguration);

    }

    public static void setDefaultRealm(String file_name){

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getInstance())
                .name("jaehun.realm")
                .schemaVersion(0)
                .setModules(new RealmModule())
                .migration(migration)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        Log.i("Realm setDefault", "success");


    }


    public void buildServerInterface(String ip) {
        Log.i("Build IP :", ip);
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

    public static RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            RealmSchema schema = realm.getSchema();

        }
    };
}
