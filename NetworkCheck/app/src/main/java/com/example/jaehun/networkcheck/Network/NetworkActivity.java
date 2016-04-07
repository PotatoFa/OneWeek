package com.example.jaehun.networkcheck.Network;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jaehun.networkcheck.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        loadData();
    }

    private void loadData(){


        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(httpLoggingInterceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Authorization", "auth-token")
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);

                // Customize or return the response
                return response;
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-193-85-231.ap-northeast-1.compute.amazonaws.com:9000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        info info = new info();
        info.setId("6");
        info.setPassword("7");
        Gson gson1 = new Gson();

        String json = gson1.toJson(info);
        Log.i("json : ", json);


        Call<Status> statusCall = networkAPI.getTest(json);

        statusCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, retrofit2.Response<Status> response) {
                if(response.isSuccessful()){
                    Status status = response.body();
                    Log.i("success : ", status.getStat());

                }else{
                    ResponseBody errorBody = response.errorBody();
                    Log.i("not success : ", errorBody.toString());

                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.i("failure : ", t.toString());

            }
        });


    }

}
