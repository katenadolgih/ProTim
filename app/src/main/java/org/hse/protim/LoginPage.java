package org.hse.protim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class LoginPage extends BaseActivity {

    private TextInputLayout textInputLayoutLogin, textInputLayoutPassword;
    private Button loginButton;
    private TextView changePasswordLink, registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Инициализация элементов
        textInputLayoutLogin = findViewById(R.id.textInputLayoutLogin);
        textInputLayoutLogin.setErrorIconDrawable(null);

        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutPassword.setErrorIconDrawable(null);

        loginButton = findViewById(R.id.loginButton);
        changePasswordLink = findViewById(R.id.changePasswordLink);
        registerLink = findViewById(R.id.registerLink);

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

        // Обработка клика на кнопку "Войти"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

        // Обработка клика на "Изменить пароль"
        changePasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, PasswordChangePage.class);
                startActivity(intent);
            }
        });

        // Обработка клика на "Зарегистрироваться"
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    // Метод для валидации полей
    private void validateFields() {
        boolean isValid = true;

        // Проверка поля "Логин"
        if (textInputLayoutLogin.getEditText().getText().toString().isEmpty()) {
            textInputLayoutLogin.setError("*Обязательно для заполнения.");
            isValid = false;
        } else {
            textInputLayoutLogin.setError(null); // Очистить ошибку
        }

        // Проверка поля "Пароль"
        if (textInputLayoutPassword.getEditText().getText().toString().isEmpty()) {
            textInputLayoutPassword.setError("*Обязательно для заполнения.");
            isValid = false;
        } else {
            textInputLayoutPassword.setError(null); // Очистить ошибку
        }

        // Если все поля прошли валидацию, выполнить вход
        if (isValid) {
            performLogin();
        }
    }

    // Метод для выполнения входа
    private void performLogin() {
        String login = textInputLayoutLogin.getEditText().getText().toString();
        String password = textInputLayoutPassword.getEditText().getText().toString();
        // Логика авторизации
    }
}