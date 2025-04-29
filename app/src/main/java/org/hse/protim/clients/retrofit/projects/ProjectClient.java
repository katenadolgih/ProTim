package org.hse.protim.clients.retrofit.projects;

import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.DTO.project.RetLikesDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectClient {
    private final ProjectApi projectApi;
    private final RetrofitProvider retrofitProvider;

    public ProjectClient(RetrofitProvider retrofitProvider) {
        this.projectApi = retrofitProvider.getAuthorizedRetrofit().create(ProjectApi.class);
        this.retrofitProvider = retrofitProvider;
    }

    public void getProjects(String filter, Integer count, ProjectCallback callback) {
        projectApi.getNewProjects(filter, count).enqueue(new Callback<List<ProjectDTO>>() {
            @Override
            public void onResponse(Call<List<ProjectDTO>> call, Response<List<ProjectDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Ошибка: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += " - " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<ProjectDTO>> call, Throwable t) {
                callback.onError("Ошибка сети: " + t.getMessage());
            }
        });
    }

    public void checkLikeStatus(Long projectId, LikeCallback callback) {
        projectApi.checkLikeStatus(projectId)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            try {
                                callback.onError(response.errorBody().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable throwable) {
                        callback.onError(throwable.getMessage());
                    }
                });
    }

    public void putLike(Long projectId, PutLikeCallBack callBack) {
        projectApi.putLike(projectId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callBack.onSuccess();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                callBack.onError(throwable.getMessage());
            }
        });
    }

    public void getProjectLikeCount(Long projectId, LikeCountCallback callback) {
        projectApi.getProjectsLikeCount(projectId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body()); // Возвращаем результат
                } else {
                    callback.onError("Invalid response");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void updateFavourites(Long projectId, PutLikeCallBack callBack) {
        projectApi.updateFavourites(projectId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(); // Возвращаем результат
                } else {
                    callBack.onError("Invalid response");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                callBack.onError(throwable.getMessage());
            }
        });
    }

    public void checkFavourites(Long projectId, LikeCallback callback) {
        projectApi.checkFavourites(projectId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getLikes(Long projectId, ProjectLikesCallback callback) {
        projectApi.getLikes(projectId).enqueue(new Callback<List<RetLikesDTO>>() {
            @Override
            public void onResponse(Call<List<RetLikesDTO>> call, Response<List<RetLikesDTO>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<RetLikesDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public interface ProjectCallback {
        void onSuccess(List<ProjectDTO> projects);
        void onError(String message);
    }

    public interface LikeCallback {
        void onSuccess(Boolean isLike);
        void onError(String message);
    }

    public interface LikeCountCallback {
        void onSuccess(Integer count);
        void onError(String message);
    }

    public interface PutLikeCallBack {
        void onSuccess();
        void onError(String message);
    }

    public interface ProjectLikesCallback {
        void onSuccess(List<RetLikesDTO> projects);
        void onError(String message);
    }
}
