package org.hse.protim.clients.retrofit.profile;

import org.hse.protim.DTO.profile.ProfileCheckDTO;
import org.hse.protim.DTO.profile.ProfileInfoDTO;
import org.hse.protim.DTO.profile.ProfilePreviewDTO;
import org.hse.protim.DTO.profile.ProfileSettingInfoDTO;
import org.hse.protim.DTO.profile.UploadProfileDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProfileApi {
    @GET("profile/profile-preview")
    Call<ProfilePreviewDTO> getProfilePreview();

    @GET("profile/profile-preview/project/{projectId}")
    Call<ProfilePreviewDTO> getProfilePreviewFromProject(@Path("projectId") Long projectId);

    @GET("profile/profile-preview/user/{userId}")
    Call<ProfilePreviewDTO> getProfilePreviewFromSpecialist(@Path("userId") Long userId);

    @GET("profile/profile-preview/lesson/{lessonId}")
    Call<ProfilePreviewDTO> getProfilePreviewFromLesson(@Path("lessonId") Long lessonId);

    @GET("profile/profile-info")
    Call<ProfileInfoDTO> getProfileInfo();

    @GET("profile/profile-info/project/{projectId}")
    Call<ProfileInfoDTO> getProfileInfoFromProject(@Path("projectId") Long projectId);

    @GET("profile/profile-info/user/{userId}")
    Call<ProfileInfoDTO> getProfileInfoFromSpecialist(@Path("userId") Long userId);

    @GET("profile/profile-info/lesson/{lessonId}")
    Call<ProfileInfoDTO> getProfileInfoFromLesson(@Path("lessonId") Long lessonId);

    @POST("profile/upload-profile")
    Call<Void> uploadProfile(@Body UploadProfileDTO uploadProfileDTO);

    @GET("profile/profile-setting-info")
    Call<ProfileSettingInfoDTO> getProfileSettingInfo();

    @POST("profile/upload-profile-setting")
    Call<Void> uploadProfileSetting(@Body ProfileSettingInfoDTO profileSettingInfoDTO);

    @GET("profile/profile-check/project/{projectId}")
    Call<ProfileCheckDTO> getProfileCheckFromProject(@Path("projectId") Long projectId);

    @GET("profile/profile-check/user/{userId}")
    Call<ProfileCheckDTO> getProfileCheckFromSpecialist(@Path("userId") Long userId);

    @GET("profile/profile-check/lesson/{lessonId}")
    Call<ProfileCheckDTO> getProfileCheckFromLesson(@Path("lessonId") Long lessonId);
}
