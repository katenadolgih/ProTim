package org.hse.protim.clients.retrofit.collection;

import org.hse.protim.DTO.collection.CollectionCountDTO;
import org.hse.protim.DTO.collection.CollectionCreateDTO;
import org.hse.protim.DTO.collection.CollectionDTO;
import org.hse.protim.DTO.collection.EditCollectionDTO;
import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionClient {
    private final CollectionApi collectionApi;

    public CollectionClient(RetrofitProvider retrofitProvider) {
        this.collectionApi = retrofitProvider.getAuthorizedRetrofit().create(CollectionApi.class);
    }

    public void getCollectionPreview(GetCollectionPreviewCallback callback) {
        collectionApi.getCollectionPreview().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CollectionDTO> call, Response<CollectionDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CollectionDTO> call, Throwable throwable) {
                if (throwable.getMessage().equals("End of input at line 1 column 1 path $")) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(throwable.getMessage());
                }
            }
        });
    }

    public void getCollectionPreviewAll(GetCollectionPreviewAll callback) {
        collectionApi.getCollectionPreviewAll().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CollectionDTO>> call, Response<List<CollectionDTO>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<CollectionDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void createCollection(CollectionCreateDTO collectionCreateDTO, NoGetCollectionCallback callback) {
        collectionApi.createCollection(collectionCreateDTO).enqueue(new Callback<>() {
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

    public void getCollectionProjects(Long collectionId, GetCollectionProjectsCallback callback) {
        collectionApi.getCollectionProjects(collectionId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProjectDTO>> call, Response<List<ProjectDTO>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<ProjectDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void deleteCollectionById(Long collectionId, NoGetCollectionCallback callback) {
        collectionApi.deleteCollectionById(collectionId).enqueue(new Callback<>() {
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

    public void getCollectionCount(GetCollectionCountCallback callback) {
        collectionApi.getCollectionCount().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CollectionCountDTO> call, Response<CollectionCountDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CollectionCountDTO> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void updateCollection(EditCollectionDTO editCollectionDTO, NoGetCollectionCallback callback) {
        collectionApi.updateCollection(editCollectionDTO).enqueue(new Callback<>() {
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

    public void getAllCollections(GetAllCollectionCallback callback) {
        collectionApi.getAllCollections().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CollectionDTO>> call, Response<List<CollectionDTO>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<CollectionDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public interface GetCollectionPreviewCallback {
        void onSuccess(CollectionDTO collectionDTO);
        void onError(String message);
    }
    public interface GetCollectionPreviewAll {
        void onSuccess(List<CollectionDTO> collectionDTOS);
        void onError(String message);
    }

    public interface NoGetCollectionCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface GetCollectionProjectsCallback {
        void onSuccess(List<ProjectDTO> collectionProjects);
        void onError(String message);
    }

    public interface GetCollectionCountCallback {
        void onSuccess(CollectionCountDTO collectionCountDTO);
        void onError(String message);
    }

    public interface GetAllCollectionCallback {
        void onSuccess(List<CollectionDTO> allCollections);
        void onError(String message);
    }

}
