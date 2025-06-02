package org.hse.protim.clients.retrofit.collection;

import org.hse.protim.DTO.collection.CollectionCountDTO;
import org.hse.protim.DTO.collection.CollectionCreateDTO;
import org.hse.protim.DTO.collection.CollectionDTO;
import org.hse.protim.DTO.collection.EditCollectionDTO;
import org.hse.protim.DTO.project.ProjectDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CollectionApi {
    @GET("collection/collection-preview")
    Call<CollectionDTO> getCollectionPreview();

    @GET("collection/collection-preview-all")
    Call<List<CollectionDTO>> getCollectionPreviewAll();

    @GET("collection/collection-projects/{collectionId}")
    Call<List<ProjectDTO>> getCollectionProjects(@Path("collectionId") Long collectionId);

    @DELETE("collection/{collectionId}")
    Call<Void> deleteCollectionById(@Path("collectionId") Long collectionId);

    @GET("collection/count")
    Call<CollectionCountDTO> getCollectionCount();

    @POST("collection")
    Call<Void> createCollection(@Body CollectionCreateDTO collectionCreateDTO);

    @PUT("collection")
    Call<Void> updateCollection(@Body EditCollectionDTO editCollectionDTO);

    @GET("collection")
    Call<List<CollectionDTO>> getAllCollections();
}
