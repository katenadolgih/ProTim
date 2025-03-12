package org.hse.protim;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.hse.protim.utils.ValidationUtils;

public class RegisterPage extends BaseActivity {

    private TextInputLayout textInputLayoutSurname;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutPhone;
    private TextInputLayout textInputLayoutEmail;
    private Button registerButton;
    private ImageButton backButton; // Добавляем переменную для кнопки "Назад"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Инициализация элементов
        textInputLayoutSurname = findViewById(R.id.textInputLayoutSurname);
        textInputLayoutSurname.setErrorIconDrawable(null);

        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutName.setErrorIconDrawable(null);

        textInputLayoutPhone = findViewById(R.id.textInputLayoutPhone);
        textInputLayoutPhone.setErrorIconDrawable(null);

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutEmail.setErrorIconDrawable(null);

        registerButton = findViewById(R.id.loginButton);

        // Инициализация кнопки "Назад"
        backButton = findViewById(R.id.button_back);

        // Обработка нажатия на кнопку "Назад"
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Закрываем текущую активность
            }
        });

        // Обработка нажатия на кнопку "Зарегистрироваться"
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
    }

    // Метод для проверки полей
    private void validateFields() {
        boolean isSurnameValid = ValidationUtils.validateField(textInputLayoutSurname, "Поле не может быть пустым");
        boolean isNameValid = ValidationUtils.validateField(textInputLayoutName, "Поле не может быть пустым");
        boolean isPhoneValid = ValidationUtils.validateField(textInputLayoutPhone, "Поле не может быть пустым");
        boolean isEmailValid = ValidationUtils.validateField(textInputLayoutEmail, "Поле не может быть пустым");

        // Если все поля прошли валидацию, можно продолжить регистрацию
        if (isSurnameValid && isNameValid && isPhoneValid && isEmailValid) {
            registerUser();
        }
    }

    // Метод для регистрации пользователя
    private void registerUser() {
        // Логика регистрации
        // Например, отправка данных на сервер
    }
}