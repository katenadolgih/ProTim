package org.hse.protim.clients.retrofit.content;

import org.hse.protim.DTO.content.ProjectContentDTO;
import org.hse.protim.DTO.project.ProjectDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContentApi {

    @GET("content/project-content/{projectId}")
    Call<List<ProjectContentDTO>> getProjectContent(@Path("projectId") Long projectId);
}
