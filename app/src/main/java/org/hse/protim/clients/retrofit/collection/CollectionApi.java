package org.hse.protim.clients.retrofit.collection;

import org.hse.protim.DTO.collection.CollectionCreateDTO;
import org.hse.protim.DTO.collection.CollectionDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CollectionApi {
    @GET("collection/collection-preview")
    Call<CollectionDTO> getCollectionPreview();

    @GET("collection/collection-preview-all")
    Call<List<CollectionDTO>> getCollectionPreviewAll();

    @POST("collection")
    Call<Void> createCollection(@Body CollectionCreateDTO collectionCreateDTO);
}
