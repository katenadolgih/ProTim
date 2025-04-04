package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.hse.protim.R;

public class PasswordChangeLogInPage extends BaseActivity {
    private TextInputLayout textInputLayoutPassword1, textInputLayoutPassword2;
    private Button loginButton;
    private TextView changePasswordLink, registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change_log_in_page);

        // Инициализация элементов
        textInputLayoutPassword1 = findViewById(R.id.textInputLayoutPassword1);
        textInputLayoutPassword1.setErrorIconDrawable(null);

        textInputLayoutPassword2 = findViewById(R.id.textInputLayoutPassword2);
        textInputLayoutPassword2.setErrorIconDrawable(null);

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
                Intent intent = new Intent(PasswordChangeLogInPage.this, PasswordChangePage.class);
                startActivity(intent);
            }
        });

        // Обработка клика на "Зарегистрироваться"
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordChangeLogInPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    // Метод для валидации полей
    private void validateFields() {
        boolean isValid = true;

        // Проверка поля "Пароль"
        String password1 = textInputLayoutPassword1.getEditText().getText().toString();
        if (password1.isEmpty()) {
            textInputLayoutPassword1.setError("*Обязательно для заполнения.");
            isValid = false;
        } else if (password1.length() < 6) { // Проверка на минимальную длину пароля
            textInputLayoutPassword1.setError("Пароль должен содержать не менее 6 символов.");
            isValid = false;
        } else {
            textInputLayoutPassword1.setError(null); // Очистить ошибку
        }

        // Проверка поля "Повторите пароль"
        String password2 = textInputLayoutPassword2.getEditText().getText().toString();
        if (password2.isEmpty()) {
            textInputLayoutPassword2.setError("*Обязательно для заполнения.");
            isValid = false;
        } else if (!password2.equals(password1)) {
            textInputLayoutPassword2.setError("Пароли не совпадают.");
            isValid = false;
        } else {
            textInputLayoutPassword2.setError(null); // Очистить ошибку
        }

        // Если все поля прошли валидацию, выполнить изменение пароля
        if (isValid) {
            performPasswordChange();
        }
    }

    // Метод для выполнения изменения пароля
    private void performPasswordChange() {
        String password1 = textInputLayoutPassword1.getEditText().getText().toString();
        String password2 = textInputLayoutPassword2.getEditText().getText().toString();
        // Логика изменения пароля
    }
}
