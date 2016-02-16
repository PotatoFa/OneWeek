package company.twoweeks.twoweeks.Controller;


import java.util.ArrayList;

import company.twoweeks.twoweeks.CallbackObject.Img;
import company.twoweeks.twoweeks.CallbackObject.LoginStatus;
import company.twoweeks.twoweeks.CallbackObject.MapAddress;
import company.twoweeks.twoweeks.CallbackObject.Redirect;
import company.twoweeks.twoweeks.CallbackObject.RegId;
import company.twoweeks.twoweeks.CallbackObject.Regi;
import company.twoweeks.twoweeks.CallbackObject.Upload;
import company.twoweeks.twoweeks.CallbackObject.User;
import company.twoweeks.twoweeks.CallbackObject.return_FileList;
import company.twoweeks.twoweeks.CallbackObject.userList;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by hoon on 2015-10-16.
 */
public interface ServerInterface {


    @Headers("Content-Type: application/json")
    @POST("/test")
    void postTest(@Body RegId regId, Callback<LoginStatus> callback);


    @Headers("Content-Type: application/json")
    @POST("/custom/regi")
    void postRegister(@Body User user, Callback<Regi> callback);
    //callback result : regi{regi_success, regi_err_fail}
    //join user


    @Headers("Content-Type: application/json")
    @POST("/sync/redirect")
    void postRedirect(@Body Redirect redirect, Callback<userList> callback);



    @GET("/custom/infoupdate/nicksearch")
    void getNickCheck(@Query("id") String id,
                      @Query("nick") String nick,
                      Callback<LoginStatus> callback);
    //callback result : nick_search{search_unexist, search_exist, search_err_fail}
    //checked duplicate nickname


    @GET("/custom/login")
    void getIdCheck(@Query("id") String id,
                    Callback<LoginStatus> callback);
    //callback result : id_status{id_unexist, id_exist, id_err}
    //checked duplicate nickname

    @GET("/custom/mapaddress")
    void getMapAddress(@Query("latitude") String latitude,
                       @Query("longitude") String longitude,
                       Callback<MapAddress> callback);
    //callback result : map_country, map_city, map_address{'address_err_fail'} = 재요청

    @GET("/matchapply")
    void getMatchApply(@Query("id") String id,
                       @Query("latitude") String latitude,
                       @Query("longitude") String longitude,
                       Callback<LoginStatus> callback);
    //callback result : Match{'Success_regi', 'over_or_err'}

    //@Headers("Content-Type: application/json")
    @Multipart
    @POST("/file/upload")
    void postFileUpload(@Part("fromUserId") TypedString fromUserId,
                        @Part("toUserId") ArrayList<String> toUserId,
                        @Part("text") TypedString text,
                        @Part("latitude") TypedString latitude,
                        @Part("longitude") TypedString longitude,
                        @Part("photo") TypedFile photo,
                        Callback<Upload> callback);

    @GET("/file/firsttimeline")
    @Headers({"Content-Type: image/jpeg"})
    Response getTimeline(@Query("country") String country,
                         @Query("city") String city);


    @GET("/file/download")
    void getDownload(@Query("filename") String filename, Callback<Img> callback);

    @GET("/file/listdisplay")
    void getFilelist(@Query("toUserId") String toUserId,
                     @Query("fromUserId") String fromUserId,
                     @Query("utc") String utc,
                     Callback<return_FileList> callback);



}
