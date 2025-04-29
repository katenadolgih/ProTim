package org.hse.protim.clients.retrofit.projects;

import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.DTO.project.RetLikesDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProjectApi {
    @GET("projects")
    Call<List<ProjectDTO>> getNewProjects(
            @Query("filter") String filter,
            @Query("count") Integer count
    );

    @GET("projects/is-like/{projectId}")
    Call<Boolean> checkLikeStatus(
            @Path("projectId") Long projectId
    );

    @POST("projects/like/{projectId}")
    Call<Void> putLike(
            @Path("projectId") Long projectId
    );

    @GET("projects/like-count/{projectId}")
    Call<Integer> getProjectsLikeCount(
            @Path("projectId") Long projectId
    );

    @POST("favourites/{projectId}")
    Call<Void> updateFavourites(
            @Path("projectId") Long projectId
    );

    @GET("favourites/check/{projectId}")
    Call<Boolean> checkFavourites(
            @Path("projectId") Long projectId
    );

    @GET("projects/like/{projectId}")
    Call<List<RetLikesDTO>> getLikes(
            @Path("projectId") Long projectId
    );
}
