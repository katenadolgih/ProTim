package org.hse.protim.clients.retrofit.content;

import org.hse.protim.DTO.content.ProjectContentDTO;
import org.hse.protim.DTO.project.RetLikesDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentClient {
    private final ContentApi contentApi;
    public ContentClient(RetrofitProvider retrofitProvider) {
        this.contentApi = retrofitProvider.getAuthorizedRetrofit().create(ContentApi.class);
    }
    public void getProjectContent(Long projectId, ProjectContentCallback callback) {
        contentApi.getProjectContent(projectId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<ProjectContentDTO>> call,
                                           Response<List<ProjectContentDTO>> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<ProjectContentDTO>> call,
                                          Throwable throwable) {
                        callback.onError(throwable.getMessage());
                    }
                });
    }
    public interface ProjectContentCallback {
        void onSuccess(List<ProjectContentDTO> projectContentDTOS);
        void onError(String message);
    }
}
