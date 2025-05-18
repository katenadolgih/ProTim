package org.hse.protim.pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.hse.protim.R;
import org.hse.protim.clients.retrofit.auth.RegisterClient;
import org.hse.protim.clients.retrofit.auth.RegistrationCallback;

public class PasswordChangeLogInPage extends AppCompatActivity {
    private TextInputLayout textInputLayoutPassword1, textInputLayoutPassword2;
    private Button loginButton;
    private TextView changePasswordLink, registerLink;
    private ImageButton backButton;
    private RegisterClient registerClient;

    private String token;
    private String getEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change_log_in_page);
        init();
        handle();
    }

    private void init() {
        textInputLayoutPassword1 = findViewById(R.id.textInputLayoutPassword1);
        textInputLayoutPassword1.setErrorIconDrawable(null);

        textInputLayoutPassword2 = findViewById(R.id.textInputLayoutPassword2);
        textInputLayoutPassword2.setErrorIconDrawable(null);

        loginButton = findViewById(R.id.loginButton);
        changePasswordLink = findViewById(R.id.changePasswordLink);
        registerLink = findViewById(R.id.registerLink);

        backButton = findViewById(R.id.button_back);

        Uri data = getIntent().getData();
        token = data.getQueryParameter("token");
        getEmail = data.getQueryParameter("mail");
        registerClient = new RegisterClient(PasswordChangeLogInPage.this);
    }

    private void handle() {
        passwordChangeHandle();
        registerLinkHandle();
        loginButton.setOnClickListener(v -> validateFields());
        backButton.setOnClickListener(v -> onBackPressed());

    }
    private void passwordChangeHandle() {
        changePasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordChangeLogInPage.this, PasswordChangePage.class);
            startActivity(intent);
        });
    }

    private void registerLinkHandle() {
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordChangeLogInPage.this, RegisterPage.class);
            startActivity(intent);
        });
    }

    private void validateFields() {
        boolean isValid = true;

        String password1 = textInputLayoutPassword1.getEditText().getText().toString();
        if (password1.isEmpty()) {
            textInputLayoutPassword1.setError("*Обязательно для заполнения.");
            isValid = false;
        } else if (password1.length() < 6) {
            textInputLayoutPassword1.setError("Пароль должен содержать не менее 6 символов.");
            isValid = false;
        } else {
            textInputLayoutPassword1.setError(null);
        }

        String password2 = textInputLayoutPassword2.getEditText().getText().toString();
        if (password2.isEmpty()) {
            textInputLayoutPassword2.setError("*Обязательно для заполнения.");
            isValid = false;
        } else if (!password2.equals(password1)) {
            textInputLayoutPassword2.setError("Пароли не совпадают.");
            isValid = false;
        } else {
            textInputLayoutPassword2.setError(null);
        }

        if (isValid) {
            performPasswordChange();
        }
    }

    private void performPasswordChange() {
        String password = textInputLayoutPassword1.getEditText().getText().toString();

        registerClient.resetPassword(token, password, new RegistrationCallback() {
            @Override
            public void onSuccess(String email) {
                registerClient.signIn(getEmail, password, new RegistrationCallback() {
                    @Override
                    public void onSuccess(String email) {
                        runOnUiThread(() -> startActivity(new Intent(PasswordChangeLogInPage.this, HomePage.class)));
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() ->
                                Toast.makeText(PasswordChangeLogInPage.this, error, Toast.LENGTH_LONG).show()
                        );
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(PasswordChangeLogInPage.this, error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }
}
