package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.hse.protim.R;
import org.hse.protim.TokenStorage;
import org.hse.protim.clients.RegisterClient;

public class LoginPage extends BaseActivity {

    private TextInputLayout textInputLayoutLogin, textInputLayoutPassword;
    private Button loginButton;
    private TextView changePasswordLink, registerLink;
    private ImageButton backButton;
    private RegisterClient registerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        init();
        handle();
    }

    private void init() {
        textInputLayoutLogin = findViewById(R.id.textInputLayoutLogin);
        textInputLayoutLogin.setErrorIconDrawable(null);

        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutPassword.setErrorIconDrawable(null);

        loginButton = findViewById(R.id.registrationButton);
        changePasswordLink = findViewById(R.id.changePasswordLink);
        registerLink = findViewById(R.id.registerLink);

        backButton = findViewById(R.id.button_back);

        registerClient = new RegisterClient(LoginPage.this);
    }

    private void handle() {
        backButton.setOnClickListener(v -> onBackPressed());

        loginButton.setOnClickListener(v -> validateFields());

        changePasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, PasswordChangePage.class);
            startActivity(intent);
        });

        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(intent);
        });
    }

    private void validateFields() {
        boolean isValid = true;

        if (textInputLayoutLogin.getEditText().getText().toString().isEmpty()) {
            textInputLayoutLogin.setError("*Обязательно для заполнения.");
            isValid = false;
        } else {
            textInputLayoutLogin.setError(null);
        }

        if (textInputLayoutPassword.getEditText().getText().toString().isEmpty()) {
            textInputLayoutPassword.setError("*Обязательно для заполнения.");
            isValid = false;
        } else {
            textInputLayoutPassword.setError(null);
        }

        if (isValid) {
            performLogin();
        }
    }

    private void performLogin() {
        String login = textInputLayoutLogin.getEditText().getText().toString();
        String password = textInputLayoutPassword.getEditText().getText().toString();

        registerClient.signIn(login, password, new RegistrationCallback() {
            @Override
            public void onSuccess(String email) {
                registerClient.test(new RegistrationCallback() {
                    @Override
                    public void onSuccess(String testResponse) {
                        runOnUiThread(() -> {
                            Toast.makeText(LoginPage.this,
                                    "Login and test successful! Response: " + testResponse,
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginPage.this, MainActivity.class));
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() ->
                                Toast.makeText(LoginPage.this,
                                        "Login OK but test failed: " + error,
                                        Toast.LENGTH_LONG).show()
                        );
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(LoginPage.this, error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }
}