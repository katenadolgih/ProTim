package org.hse.protim.clients.retrofit.courses;

import static org.hse.protim.clients.retrofit.RetrofitProvider.BASE_URL;

import org.hse.protim.DTO.courses.CourseDetail;
import org.hse.protim.DTO.courses.CoursePreviewDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseClient {
    private final CourseApi courseApi;

    public CourseClient(RetrofitProvider retrofitProvider) {
        this.courseApi = retrofitProvider.getAuthorizedRetrofit().create(CourseApi.class);
    }

    public void getMainCourses(CourseCallback callback) {
        courseApi.getMainCourses().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CoursePreviewDTO>> call, Response<List<CoursePreviewDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CoursePreviewDTO>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getAllCourses(CourseCallback callback) {
        courseApi.getAllCourses().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CoursePreviewDTO>> call, Response<List<CoursePreviewDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CoursePreviewDTO>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getCourseDetail(Long courseId, CourseDetailCallback callback) {
        courseApi.getCourseDetail(courseId).enqueue(new Callback<CourseDetail>() {
            @Override
            public void onResponse(Call<CourseDetail> call, Response<CourseDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CourseDetail> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    public interface CourseCallback {
        void onSuccess(List<CoursePreviewDTO> courses);
        void onError(String message);
    }

    public interface CourseDetailCallback {
        void onSuccess(CourseDetail courseDetail);
        void onError(String message);
    }
}
