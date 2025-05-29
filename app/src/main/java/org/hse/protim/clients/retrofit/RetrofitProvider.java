package org.hse.protim.clients.retrofit;

import android.content.Context;

import org.hse.protim.clients.retrofit.auth.AuthApi;
import org.hse.protim.clients.retrofit.auth.AuthInterceptor;
import org.hse.protim.utils.auth.TokenStorage;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
    //10.0.2.2
//    public static final String BASE_URL = "http://45.11.27.174:8081/api/";
    public static final String BASE_URL = "http://10.0.2.2:8081/api/";
    private final Retrofit retrofit;
    private final Retrofit authorizedRetrofit;
    private final TokenStorage tokenStorage;

    public RetrofitProvider(Context context) {
        tokenStorage = new TokenStorage(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TokenStorage tokenStorage = new TokenStorage(context);
        AuthApi authApi = retrofit.create(AuthApi.class);

        OkHttpClient authorizedClient = client.newBuilder()
                .addInterceptor(new AuthInterceptor(tokenStorage, authApi))
                .build();

        authorizedRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(authorizedClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Retrofit getAuthorizedRetrofit() {
        return authorizedRetrofit;
    }

    public TokenStorage getTokenStorage() {
        return tokenStorage;
    }
}
