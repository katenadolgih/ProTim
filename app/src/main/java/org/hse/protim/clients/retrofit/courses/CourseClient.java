package org.hse.protim.clients.retrofit.courses;

import org.hse.protim.DTO.courses.CourseDetailDTO;
import org.hse.protim.DTO.courses.CoursePreviewDTO;
import org.hse.protim.DTO.courses.CourseProgramDTO;
import org.hse.protim.DTO.courses.OwnedCourseDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        courseApi.getCourseDetail(courseId).enqueue(new Callback<CourseDetailDTO>() {
            @Override
            public void onResponse(Call<CourseDetailDTO> call, Response<CourseDetailDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CourseDetailDTO> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getOwnedCourse(OwnedCourseCallback callback) {
        courseApi.getOwnedCourse().enqueue(new Callback<List<OwnedCourseDTO>>() {
            @Override
            public void onResponse(Call<List<OwnedCourseDTO>> call, Response<List<OwnedCourseDTO>> response) {
                callback.onSuccess(response.body());

            }

            @Override
            public void onFailure(Call<List<OwnedCourseDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());

            }
        });
    }

    public void getCourseProgram(Long courseId, CourseProgramCallback callback) {
        courseApi.getCourseProgram(courseId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CourseProgramDTO>> call, Response<List<CourseProgramDTO>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<CourseProgramDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getLastSeenProgram(Long courseId, LastSeenProgramCallback callback) {
        courseApi.getLastSeenProgram(courseId).enqueue(new Callback<CourseProgramDTO>() {
            @Override
            public void onResponse(Call<CourseProgramDTO> call, Response<CourseProgramDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CourseProgramDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public interface CourseCallback {
        void onSuccess(List<CoursePreviewDTO> courses);
        void onError(String message);
    }

    public interface CourseDetailCallback {
        void onSuccess(CourseDetailDTO courseDetailDTO);
        void onError(String message);
    }

    public interface OwnedCourseCallback {
        void onSuccess(List<OwnedCourseDTO> ownedCourseDTO);
        void onError(String message);
    }

    public interface CourseProgramCallback {
        void onSuccess(List<CourseProgramDTO> courseProgramDTOS);
        void onError(String message);
    }

    public interface LastSeenProgramCallback {
        void onSuccess(CourseProgramDTO courseProgramDTO);
        void onError(String message);
    }
}
