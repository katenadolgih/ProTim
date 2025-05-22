package org.hse.protim.clients.retrofit.lesson;

import org.hse.protim.DTO.lesson.LessonDTO;
import org.hse.protim.DTO.lesson.LessonId;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LessonApi {
    @GET("lesson/{lessonId}/content")
    Call<LessonDTO> getLessonContent(@Path("lessonId") Long lessonId);

    @POST("lesson/update-last-lesson/{lessonId}")
    Call<Void> updateLastLesson(@Path("lessonId") Long lessonId);

    @GET("lesson/before-lesson/{lessonId}")
    Call<LessonId> getBeforeLesson(@Path("lessonId") Long lessonId);

    @GET("lesson/after-lesson/{lessonId}")
    Call<LessonId> getAfterLesson(@Path("lessonId") Long lessonId);
}
