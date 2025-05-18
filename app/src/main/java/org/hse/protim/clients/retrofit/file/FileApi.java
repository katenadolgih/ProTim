package org.hse.protim.clients.retrofit.file;

import org.hse.protim.DTO.PutLinkDTO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FileApi {
    @GET("put-link")
    Call<PutLinkDTO> getPutLink(
            @Query("filePath") String filePath);

    @FormUrlEncoded
    @POST("put-link")
    Call<Void> putLinkSave(
            @Field("filePath") String filePath,
            @Field("type") String type);
}
