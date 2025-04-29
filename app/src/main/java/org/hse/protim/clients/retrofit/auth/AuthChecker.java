package org.hse.protim.clients.retrofit.auth;

import android.content.Context;

import org.hse.protim.DTO.auth.RefreshRequest;
import org.hse.protim.DTO.auth.TokensDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.utils.auth.TokenStorage;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthChecker {
    private final AuthApi authApi;
    private final TokenStorage tokenStorage;
    private final RetrofitProvider retrofitProvider;

    public AuthChecker(RetrofitProvider retrofitProvider) {
        this.retrofitProvider = retrofitProvider;
        this.tokenStorage = retrofitProvider.getTokenStorage();
        this.authApi = retrofitProvider.getAuthorizedRetrofit().create(AuthApi.class);
    }

    public void checkAuth(AuthCallback callback) {
        if (tokenStorage.getAccessToken() == null) {
            callback.onUnauthorized();
            return;
        }

        authApi.checkAuth().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onAuthorized();
                } else if (response.code() == 401) {
                    refreshToken(callback);
                } else {
                    callback.onError("Check failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private void refreshToken(AuthCallback callback) {
        String refreshToken = tokenStorage.getRefreshToken();
        if (refreshToken == null) {
            callback.onUnauthorized();
            return;
        }

        authApi.refreshTokens(new RefreshRequest(refreshToken))
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<TokensDTO> call, Response<TokensDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            TokensDTO tokens = response.body();
                            tokenStorage.saveTokens(tokens.accessToken(), tokens.refreshToken());
                            checkAuth(callback); // Повторная проверка с новым токеном
                        } else {
                            callback.onUnauthorized();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokensDTO> call, Throwable t) {
                        callback.onError("Refresh failed: " + t.getMessage());
                    }
                });
    }

    public interface AuthCallback {
        void onAuthorized();
        void onUnauthorized();
        void onError(String message);
    }
}
