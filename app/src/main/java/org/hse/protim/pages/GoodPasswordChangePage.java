package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.hse.protim.R;

public class GoodPasswordChangePage extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_password_change_page);

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

        // Настройка кнопки "Okay"
        Button okayButton = findViewById(R.id.okayButton);
        if (okayButton != null) {
            okayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Создаем интент для перехода на страницу activity_password_change_log_in_page
                    Intent intent = new Intent(GoodPasswordChangePage.this, PasswordChangeLogInPage.class);
                    startActivity(intent);
                }
            });
        }
    }

}