package org.hse.protim.clients.retrofit.lesson;

import org.hse.protim.DTO.lesson.LessonDTO;
import org.hse.protim.DTO.lesson.LessonId;
import org.hse.protim.clients.retrofit.RetrofitProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonClient {
    private final LessonApi lessonApi;

    public LessonClient(RetrofitProvider retrofitProvider) {
        this.lessonApi = retrofitProvider.getAuthorizedRetrofit().create(LessonApi.class);
    }

    public void getLessonContent(Long lessonId, GetLessonContentCallback callback) {
        lessonApi.getLessonContent(lessonId).enqueue(new Callback<LessonDTO>() {
            @Override
            public void onResponse(Call<LessonDTO> call, Response<LessonDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<LessonDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void updateLastLesson(Long lessonId, UpdateLastLessonCallback callback) {
        lessonApi.updateLastLesson(lessonId).enqueue(new Callback<Void>() {
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

    public void getBeforeLesson(Long lessonId, GetBeforeAfterLessonCallback callback) {
        lessonApi.getBeforeLesson(lessonId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LessonId> call, Response<LessonId> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<LessonId> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getAfterLesson(Long lessonId, GetBeforeAfterLessonCallback callback) {
        lessonApi.getAfterLesson(lessonId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LessonId> call, Response<LessonId> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<LessonId> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public interface GetLessonContentCallback {
        void onSuccess(LessonDTO lessonDTO);
        void onError(String message);
    }

    public interface UpdateLastLessonCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface GetBeforeAfterLessonCallback {
        void onSuccess(LessonId lessonId);
        void onError(String message);
    }
}
