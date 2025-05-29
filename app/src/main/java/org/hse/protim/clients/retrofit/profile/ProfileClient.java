package org.hse.protim.clients.retrofit.profile;

import org.hse.protim.DTO.profile.ProfileCheckDTO;
import org.hse.protim.DTO.profile.ProfileInfoDTO;
import org.hse.protim.DTO.profile.ProfilePreviewDTO;
import org.hse.protim.DTO.profile.ProfileSettingInfoDTO;
import org.hse.protim.DTO.profile.UploadProfileDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileClient {
    private final ProfileApi profileApi;

    public ProfileClient(RetrofitProvider retrofitProvider) {
        this.profileApi = retrofitProvider.getAuthorizedRetrofit().create(ProfileApi.class);
    }

    public void getProfilePreview(ProfilePreviewCallback callback) {
        profileApi.getProfilePreview().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfilePreviewDTO> call, Response<ProfilePreviewDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfilePreviewDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfilePreviewFromProject(Long projectId, ProfilePreviewCallback callback) {
        profileApi.getProfilePreviewFromProject(projectId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfilePreviewDTO> call, Response<ProfilePreviewDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfilePreviewDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfilePreviewFromSpecialist(Long userId, ProfilePreviewCallback callback) {
        profileApi.getProfilePreviewFromSpecialist(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfilePreviewDTO> call, Response<ProfilePreviewDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfilePreviewDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfilePreviewFromLesson(Long lessonId, ProfilePreviewCallback callback) {
        profileApi.getProfilePreviewFromLesson(lessonId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfilePreviewDTO> call, Response<ProfilePreviewDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfilePreviewDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfileInfo(ProfileInfoCallback callback) {
        profileApi.getProfileInfo().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfileInfoDTO> call, Response<ProfileInfoDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfileInfoDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfileInfoFromProject(Long projectId, ProfileInfoCallback callback) {
        profileApi.getProfileInfoFromProject(projectId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfileInfoDTO> call, Response<ProfileInfoDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfileInfoDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfileInfoFromSpecialist(Long userId, ProfileInfoCallback callback) {
        profileApi.getProfileInfoFromSpecialist(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfileInfoDTO> call, Response<ProfileInfoDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfileInfoDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfileInfoFromLesson(Long lessonId, ProfileInfoCallback callback) {
        profileApi.getProfileInfoFromLesson(lessonId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfileInfoDTO> call, Response<ProfileInfoDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfileInfoDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void uploadProfile(UploadProfileDTO uploadProfileDTO, UploadProfileCallback callback) {
        profileApi.uploadProfile(uploadProfileDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfileSettingInfo(ProfileSettingInfoCallback profileSettingInfoCallback) {
        profileApi.getProfileSettingInfo().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfileSettingInfoDTO> call, Response<ProfileSettingInfoDTO> response) {
                profileSettingInfoCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfileSettingInfoDTO> call, Throwable throwable) {
                profileSettingInfoCallback.onError(throwable.getMessage());
            }
        });
    }

    public void uploadProfileSetting(ProfileSettingInfoDTO profileSettingInfoDTO, UploadProfileCallback callback) {
        profileApi.uploadProfileSetting(profileSettingInfoDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfileCheckFromProject(Long projectId, ProfileCheckCallback callback) {
        profileApi.getProfileCheckFromProject(projectId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfileCheckDTO> call, Response<ProfileCheckDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfileCheckDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfileCheckFromSpecialist(Long userId, ProfileCheckCallback callback) {
        profileApi.getProfileCheckFromSpecialist(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfileCheckDTO> call, Response<ProfileCheckDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfileCheckDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getProfileCheckFromLesson(Long lessonId, ProfileCheckCallback callback) {
        profileApi.getProfileCheckFromLesson(lessonId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProfileCheckDTO> call, Response<ProfileCheckDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProfileCheckDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public interface ProfilePreviewCallback {
        void onSuccess(ProfilePreviewDTO previewDTO);
        void onError(String message);
    }

    public interface ProfileInfoCallback {
        void onSuccess(ProfileInfoDTO profileInfoDTO);
        void onError(String message);
    }

    public interface UploadProfileCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface ProfileSettingInfoCallback {
        void onSuccess(ProfileSettingInfoDTO profileSettingInfoDTO);
        void onError(String message);
    }

    public interface ProfileCheckCallback {
        void onSuccess(ProfileCheckDTO profileCheckDTO);
        void onError(String message);
    }

}
