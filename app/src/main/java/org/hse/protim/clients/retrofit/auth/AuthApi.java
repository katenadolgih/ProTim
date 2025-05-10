package org.hse.protim.clients.retrofit.auth;

import org.hse.protim.DTO.auth.MailDTO;
import org.hse.protim.DTO.auth.RefreshRequest;
import org.hse.protim.DTO.auth.ResetPasswordDTO;
import org.hse.protim.DTO.auth.TokensDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("refresh")
    Call<TokensDTO> refreshTokens(@Body RefreshRequest refreshRequest);

    @GET("check")
    Call<Void> checkAuth();

    @POST("request-reset")
    Call<Void> requestReset(@Body MailDTO mailDTO);

    @POST("reset-password")
    Call<Void> resetPassword(@Body ResetPasswordDTO resetPasswordDTO);
}
