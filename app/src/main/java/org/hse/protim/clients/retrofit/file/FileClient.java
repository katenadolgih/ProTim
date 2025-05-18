package org.hse.protim.clients.retrofit.file;

import org.hse.protim.DTO.PutLinkDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.profile.ProfileApi;

import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileClient {
    private final FileApi fileApi;

    public FileClient(RetrofitProvider retrofitProvider) {
        this.fileApi = retrofitProvider.getAuthorizedRetrofit().create(FileApi.class);
    }

    public void getPutLink(String filePath, PutLinkCallback putLinkCallback) {
        fileApi.getPutLink(filePath).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PutLinkDTO> call, Response<PutLinkDTO> response) {
                try {
                    putLinkCallback.onSuccess(response.body().link());
                } catch (FileNotFoundException e) {
                }
            }

            @Override
            public void onFailure(Call<PutLinkDTO> call, Throwable throwable) {
                putLinkCallback.onError(throwable.getMessage());
            }
        });
    }

    public void putLinkSave(String filePath, String type, PutLinkSaveCallback putLinkSaveCallback) {
        fileApi.putLinkSave(filePath, type).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                putLinkSaveCallback.onSuccess();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                putLinkSaveCallback.onError(throwable.getMessage());
            }
        });
    }

    public interface PutLinkCallback {
        void onSuccess(String link) throws FileNotFoundException;
        void onError(String message);
    }

    public interface PutLinkSaveCallback {
        void onSuccess();
        void onError(String message);
    }
}
