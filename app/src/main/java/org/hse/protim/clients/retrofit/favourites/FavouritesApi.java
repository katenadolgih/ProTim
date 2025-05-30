package org.hse.protim.clients.retrofit.favourites;

import org.hse.protim.DTO.project.ProjectDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FavouritesApi {
    @GET("favourites")
    Call<List<ProjectDTO>> getFavourites();
}
