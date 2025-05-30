package org.hse.protim.clients.retrofit.favourites;

import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesClient {

    private final FavouritesApi favouritesApi;

    public FavouritesClient(RetrofitProvider retrofitProvider) {
        this.favouritesApi = retrofitProvider.getAuthorizedRetrofit().create(FavouritesApi.class);
    }

    public void getFavourites(GetFavouritesCallback callback) {
        favouritesApi.getFavourites().enqueue(new Callback<>() {
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

    public interface GetFavouritesCallback {
        void onSuccess(List<ProjectDTO> projectDTOS);
        void onError(String message);
    }
}
