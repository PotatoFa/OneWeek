package wad.google_login;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by hoon on 2016-02-16.
 */
public interface ServerInterface {


    @GET("/custom/logout")
    void get_logout(@Query("id") String id, Callback<Return> callback);

    @GET("/custom/login")
    void get_login(@Query("id") String id, @Query("token") String token, Callback<Return> callback);

}
