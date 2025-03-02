package org.hse.protim;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.hse.protim.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginField, passwordField;
    private Button loginButton;
    private TextView changePasswordLink, registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Инициализация элементов
        loginField = findViewById(R.id.loginField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        changePasswordLink = findViewById(R.id.changePasswordLink);
        registerLink = findViewById(R.id.registerLink);

        // Обработка клика на кнопку "Войти"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginField.getText().toString();
                String password = passwordField.getText().toString();
                // Логика авторизации
            }
        });

        // Обработка клика на "Изменить пароль"
        changePasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран изменения пароля
            }
        });

        // Обработка клика на "Зарегистрироваться"
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран регистрации
            }
        });
    }
}