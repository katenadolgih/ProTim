package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.hse.protim.R;

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

        // Настройка кнопки "Отмена" (переход на главный экран)
        Button cancelButton = findViewById(R.id.cancelButton);
        if (cancelButton != null) {
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Переход на главный экран (MainActivity)
                    Intent intent = new Intent(PasswordChangePage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Очистка стека активностей
                    startActivity(intent);
                    finish(); // Закрываем текущую активность
                }
            });
        }
    }
}