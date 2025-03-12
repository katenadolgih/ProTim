package org.hse.protim.utils;

import com.google.android.material.textfield.TextInputLayout;

public class ValidationUtils {

    // Метод для проверки поля на пустоту
    public static boolean validateField(TextInputLayout textInputLayout, String errorMessage) {
        if (textInputLayout.getEditText().getText().toString().isEmpty()) {
            textInputLayout.setError(errorMessage); // Установить ошибку
            return false; // Поле не прошло валидацию
        } else {
            textInputLayout.setError(null); // Очистить ошибку
            return true; // Поле прошло валидацию
        }
    }
}