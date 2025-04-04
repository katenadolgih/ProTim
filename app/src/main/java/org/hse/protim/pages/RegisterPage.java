package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.hse.protim.R;
import org.hse.protim.clients.RegisterClient;
import org.hse.protim.utils.ValidationUtils;

public class RegisterPage extends BaseActivity {

    private TextInputLayout textInputLayoutSurname;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutPhone;
    private TextInputLayout textInputLayoutEmail;
    private Button registerButton;
    private ImageButton backButton;
    private RegisterClient registerClient;

    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        init();
        handle();
    }

    private void init() {
        textInputLayoutSurname = findViewById(R.id.textInputLayoutSurname);
        textInputLayoutSurname.setErrorIconDrawable(null);

        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutName.setErrorIconDrawable(null);

        textInputLayoutPhone = findViewById(R.id.textInputLayoutPhone);
        textInputLayoutPhone.setErrorIconDrawable(null);

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutEmail.setErrorIconDrawable(null);

        registerButton = findViewById(R.id.registrationButton);

        backButton = findViewById(R.id.button_back);

        loginTextView = findViewById(R.id.loginTextView);

        registerClient = new RegisterClient(RegisterPage.this);
    }

    private void handle() {
        backButton.setOnClickListener(v -> onBackPressed());

        registerButton.setOnClickListener(v -> validateFields());

        loginTextView.setOnClickListener(v -> loginHandler());
    }
    private void validateFields() {
        boolean isSurnameValid = ValidationUtils.validateField(textInputLayoutSurname, "Поле не может быть пустым");
        boolean isNameValid = ValidationUtils.validateField(textInputLayoutName, "Поле не может быть пустым");
        boolean isPhoneValid = ValidationUtils.validateField(textInputLayoutPhone, "Поле не может быть пустым");
        boolean isEmailValid = ValidationUtils.validateField(textInputLayoutEmail, "Поле не может быть пустым");

        if (isSurnameValid && isNameValid && isPhoneValid && isEmailValid) {
            registerUser();
        }
    }

    private void registerUser() {
        String surname = textInputLayoutSurname.getEditText().getText().toString();
        String name = textInputLayoutName.getEditText().getText().toString();
        String phoneNumber = textInputLayoutPhone.getEditText().getText().toString();
        String email = textInputLayoutEmail.getEditText().getText().toString();

        registerClient.register(surname, name, phoneNumber, email, new RegistrationCallback() {
            @Override
            public void onSuccess(String email) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterPage.this,
                            "Пароль отправлен на " + email, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(RegisterPage.this, error, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void loginHandler() {
        Intent intent = new Intent(RegisterPage.this, LoginPage.class);
        startActivity(intent);
    }


}