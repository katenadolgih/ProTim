package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.DTO.notification.NotificationDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.notification.NotificationClient;

import java.util.ArrayList;
import java.util.List;

public class NotificationPage extends BaseActivity {

    private ImageButton buttonBack;
    private TextView titleView;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private RetrofitProvider retrofitProvider;
    private NotificationClient notificationClient;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotificationPage.this, HomePage.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.notification_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        handle();
        setupNotifications();

        titleView.setText(R.string.notification_page_title);

    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        recyclerView = findViewById(R.id.notification_recycler);
        retrofitProvider = new RetrofitProvider(this);
        notificationClient = new NotificationClient(retrofitProvider);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
    }

    private void setupNotifications() {
        notificationClient.getAllNotification(new NotificationClient.GetAllNotificationsCallback() {
            @Override
            public void onSuccess(List<NotificationDTO> notificationDTOS) {
                adapter = new NotificationAdapter(notificationDTOS);
                recyclerView.setLayoutManager(new LinearLayoutManager(NotificationPage.this));
                recyclerView.setAdapter(adapter);

                notificationClient.watchNotification(new NotificationClient.WatchNotificationCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> Toast.makeText(NotificationPage.this, message,
                                Toast.LENGTH_LONG).show());
                    }
                });
            }

            @Override
            public void onError(String message) {
                Toast.makeText(NotificationPage.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
