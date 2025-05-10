package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.hse.protim.R;
import org.hse.protim.clients.retrofit.auth.RegisterClient;

public class PasswordChangePage extends BaseActivity {

    private ImageButton backButton;
    private Button submitButton;
    private Button cancelButton;
    private TextInputEditText emailEdit;
    private RegisterClient registerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change_page);

        init();
        handle();
    }

    private void init() {
        backButton = findViewById(R.id.button_back);
        submitButton = findViewById(R.id.sendButton);
        cancelButton = findViewById(R.id.cancelButton);
        registerClient = new RegisterClient(PasswordChangePage.this);
        emailEdit = findViewById(R.id.email_text);
    }

    private void handle() {
        backButton.setOnClickListener(v -> onBackPressed());
        submitButtonHandle();
        cancelButtonHandle();
    }

    private void cancelButtonHandle() {
        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordChangePage.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void submitButtonHandle() {
        submitButton.setOnClickListener(v -> {
            String email = emailEdit.getText().toString();
            registerClient.requestReset(email, new RegisterClient.RequestResetCallback() {
                @Override
                public void onSuccess(String email) {
                    Intent intent = new Intent(PasswordChangePage.this, GoodPasswordChangePage.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() ->
                            Toast.makeText(PasswordChangePage.this, message, Toast.LENGTH_LONG).show()
                    );
                }
            });

        });
    }
}