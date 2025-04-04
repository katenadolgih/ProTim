package org.hse.protim.utils;

import com.google.android.material.textfield.TextInputLayout;

public class ValidationUtils {

    // Проверка, что поле не пустое
    public static boolean validateField(TextInputLayout inputLayout, String errorMessage) {
        String text = inputLayout.getEditText().getText().toString().trim();
        if (text.isEmpty()) {
            inputLayout.setError(errorMessage);
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }

    // Проверка, что имя/фамилия состоит только из кириллицы
    public static boolean validateCyrillicName(TextInputLayout inputLayout, String errorMessage) {
        String text = inputLayout.getEditText().getText().toString().trim();
        if (!text.matches("[а-яА-ЯёЁ]+")) { // Регулярное выражение для кириллицы
            inputLayout.setError(errorMessage);
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }

    // Проверка, что email содержит символ "@"
    public static boolean validateEmail(TextInputLayout inputLayout, String errorMessage) {
        String text = inputLayout.getEditText().getText().toString().trim();
        if (!text.contains("@")) {
            inputLayout.setError(errorMessage);
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }
}