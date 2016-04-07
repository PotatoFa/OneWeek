package whataday.oneweek.Controller;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;
import whataday.oneweek.NetworkObject.User;

/**
 * Created by hoon on 2016-03-07.
 */
public interface ServerInterface {


    @GET("business/signin/login")
    void getLogin(@Query("id") String id,
                  @Query("token") String token,
                  Callback<Response> callback);


    @Headers("Content-Type: application/json")
    @POST("/custom/regi")
    void postRegister(@Body User user, Callback<Response> callback);



}
