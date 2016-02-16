package wad.google_login;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by hoon on 2015-07-10.
 */
public class ApplicationController extends Application {
    private static  ApplicationController instance;
    public static ApplicationController getInstance() {return instance; }

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationController.instance = this;
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
