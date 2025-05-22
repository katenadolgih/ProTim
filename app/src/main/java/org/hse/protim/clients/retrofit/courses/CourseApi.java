package org.hse.protim.clients.retrofit.courses;

import org.hse.protim.DTO.courses.CourseDetailDTO;
import org.hse.protim.DTO.courses.CoursePreviewDTO;
import org.hse.protim.DTO.courses.CourseProgramDTO;
import org.hse.protim.DTO.courses.OwnedCourseDTO;

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
    Call<CourseDetailDTO> getCourseDetail(@Path("courseId") Long courseId);

    @GET("courses/owned")
    Call<List<OwnedCourseDTO>> getOwnedCourse();

    @GET("courses/course-program/{courseId}")
    Call<List<CourseProgramDTO>> getCourseProgram(@Path("courseId") Long courseId);

    @GET("courses/last-seen/{courseId}")
    Call<CourseProgramDTO> getLastSeenProgram(@Path("courseId") Long courseId);
}
