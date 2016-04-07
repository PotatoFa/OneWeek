package com.example.jaehun.networkcheck.Network;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkAPI {

    @GET("users/{username}/repos")
    Call<User> getUser(@Path("username") String username);


    @Headers("Content-Type: application/json")
    @GET("business/signin/login")
    Call<Status> getTest(@Query("info") String json);
}
