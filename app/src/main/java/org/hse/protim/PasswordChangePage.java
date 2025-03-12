package org.hse.protim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PasswordChangePage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change_page);

        // Настройка кнопки "Назад"
        ImageButton backButton = findViewById(R.id.button_back);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed(); // Закрываем текущую активность
                }
            });
        }

        // Настройка кнопки "Отправить"
        Button submitButton = findViewById(R.id.sendButton);
        if (submitButton != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Переход на страницу GoodPasswordChangePage
                    Intent intent = new Intent(PasswordChangePage.this, GoodPasswordChangePage.class);
                    startActivity(intent);
                }
            });
        }
    }
}