package org.hse.protim.clients.retrofit.notification;

import org.hse.protim.DTO.notification.LastNotificationDTO;
import org.hse.protim.DTO.notification.NotificationDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationClient {
    private final NotificationApi notificationApi;

    public NotificationClient(RetrofitProvider retrofitProvider) {
        notificationApi = retrofitProvider.getAuthorizedRetrofit().create(NotificationApi.class);
    }

    public void getLastNotification(GetLastNotificationCallback callback) {
        notificationApi.getLastNotification().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LastNotificationDTO> call, Response<LastNotificationDTO> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<LastNotificationDTO> call, Throwable throwable) {
                if (throwable.getMessage().equals("End of input at line 1 column 1 path $")) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(throwable.getMessage());
                }
            }
        });
    }

    public void getAllNotification(GetAllNotificationsCallback callback) {
        notificationApi.getNotifications().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<NotificationDTO>> call, Response<List<NotificationDTO>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<NotificationDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void watchNotification(WatchNotificationCallback callback) {
        notificationApi.watchNotification().enqueue(new Callback<>() {
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

    public interface GetLastNotificationCallback {
        void onSuccess(LastNotificationDTO lastNotificationDTO);
        void onError(String message);
    }

    public interface GetAllNotificationsCallback {
        void onSuccess(List<NotificationDTO> notificationDTOS);
        void onError(String message);
    }

    public interface WatchNotificationCallback {
        void onSuccess();
        void onError(String message);
    }
}
