package org.hse.protim.clients.retrofit.courses;

import org.hse.protim.DTO.courses.CourseDetail;
import org.hse.protim.DTO.courses.CoursePreviewDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CourseApi {
    @GET("courses/main-preview")
    Call<List<CoursePreviewDTO>> getMainCourses();

    @GET("courses/all-preview")
    Call<List<CoursePreviewDTO>> getAllCourses();

    @GET("courses/course-detail/{courseId}")
    Call<CourseDetail> getCourseDetail(@Path("courseId") Long courseId);
}
