package org.hse.protim.clients.retrofit.favourites;

import org.hse.protim.DTO.collection.UpdateFavouritesDTO;
import org.hse.protim.DTO.project.ProjectDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface FavouritesApi {
    @GET("favourites")
    Call<List<ProjectDTO>> getFavourites();

    @PUT("favourites/update")
    Call<Void> updateFavouritesAddColl(@Body UpdateFavouritesDTO updateFavouritesDTO);

    @PUT("favourites/remove-coll")
    Call<Void> removeFavourites(@Body UpdateFavouritesDTO updateFavouritesDTO);
}
