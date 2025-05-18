package org.hse.protim.clients.retrofit.profile;

import org.hse.protim.DTO.profile.ProfileInfoDTO;
import org.hse.protim.DTO.profile.ProfilePreviewDTO;
import org.hse.protim.DTO.profile.ProfileSettingInfoDTO;
import org.hse.protim.DTO.profile.UploadProfileDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProfileApi {
    @GET("profile/profile-preview")
    Call<ProfilePreviewDTO> getProfilePreview();

    @GET("profile/profile-info")
    Call<ProfileInfoDTO> getProfileInfo();

    @POST("profile/upload-profile")
    Call<Void> uploadProfile(@Body UploadProfileDTO uploadProfileDTO);

    @GET("profile/profile-setting-info")
    Call<ProfileSettingInfoDTO> getProfileSettingInfo();

    @POST("profile/upload-profile-setting")
    Call<Void> uploadProfileSetting(@Body ProfileSettingInfoDTO profileSettingInfoDTO);
}
