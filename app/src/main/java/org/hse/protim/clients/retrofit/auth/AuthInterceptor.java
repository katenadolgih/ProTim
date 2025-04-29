package org.hse.protim.clients.retrofit.auth;

import androidx.annotation.NonNull;

import org.hse.protim.DTO.auth.RefreshRequest;
import org.hse.protim.DTO.auth.TokensDTO;
import org.hse.protim.utils.auth.TokenStorage;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final TokenStorage tokenStorage;
    private final AuthApi authApi;

    public AuthInterceptor(TokenStorage tokenStorage, AuthApi authApi) {
        this.tokenStorage = tokenStorage;
        this.authApi = authApi;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + tokenStorage.getAccessToken())
                .build();

        Response response = chain.proceed(newRequest);

        if (response.code() == 401) {
            synchronized (this) {
                String refreshToken = tokenStorage.getRefreshToken();
                if (refreshToken != null) {
                    try {
                        retrofit2.Response<TokensDTO> refreshResponse = authApi.refreshTokens(
                                new RefreshRequest(refreshToken)).execute();

                        if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                            TokensDTO tokens = refreshResponse.body();
                            tokenStorage.saveTokens(tokens.accessToken(), tokens.refreshToken());

                            newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + tokens.accessToken())
                                    .build();
                            return chain.proceed(newRequest);
                        } else {
                            tokenStorage.clearTokens();
                        }
                    } catch (Exception e) {
                        tokenStorage.clearTokens();
                    }
                }
            }
        }

        return response;
    }
}
