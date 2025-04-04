package org.hse.protim.utils;

import com.google.android.material.textfield.TextInputLayout;

public class ValidationUtils {

    public static boolean validateField(TextInputLayout textInputLayout, String errorMessage) {
        if (textInputLayout.getEditText().getText().toString().isEmpty()) {
            textInputLayout.setError(errorMessage);
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }
}