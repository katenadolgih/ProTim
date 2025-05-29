package org.hse.protim.pages;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationPage extends BaseActivity {

    private ImageButton buttonBack;
    private TextView titleView;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

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

        titleView.setText(R.string.notification_page_title);

    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        recyclerView = findViewById(R.id.notification_recycler);

        List<NotificationItem> notifications = new ArrayList<>();
        notifications.add(new NotificationItem(R.drawable.ic_user, "Вардиева Наталья", "Дом мечты"));
        notifications.add(new NotificationItem(R.drawable.ic_user, "Иванов Сергей", "Город солнца"));
        notifications.add(new NotificationItem(R.drawable.ic_user, "Вардиева Наталья", "Дом мечты"));
        notifications.add(new NotificationItem(R.drawable.ic_user, "Иванов Сергей", "Город солнца"));
        notifications.add(new NotificationItem(R.drawable.ic_user, "Иванов Сергей", "Город солнца"));
        notifications.add(new NotificationItem(R.drawable.ic_user, "Вардиева Наталья", "Дом мечты"));
        notifications.add(new NotificationItem(R.drawable.ic_user, "Иванов Сергей", "Город солнца"));

        adapter = new NotificationAdapter(notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
}
