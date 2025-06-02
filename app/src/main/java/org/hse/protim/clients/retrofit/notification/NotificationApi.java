package org.hse.protim.clients.retrofit.notification;

import org.hse.protim.DTO.notification.LastNotificationDTO;
import org.hse.protim.DTO.notification.NotificationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;

public interface NotificationApi {
    @GET("notification/last")
    Call<LastNotificationDTO> getLastNotification();

    @GET("notification")
    Call<List<NotificationDTO>> getNotifications();

    @DELETE("notification/watch")
    Call<Void> watchNotification();
}
