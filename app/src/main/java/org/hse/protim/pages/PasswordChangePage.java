package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.hse.protim.R;

public class PasswordChangePage extends BaseActivity {

    private ImageButton backButton;
    private Button submitButton, cancelChangeButton;


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
        cancelChangeButton = findViewById(R.id.cancelChangeButton);
    }

    private void handle() {
        backButton.setOnClickListener(v -> onBackPressed());
        cancelChangeButton.setOnClickListener(v -> onBackPressed());
        submitButton.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordChangePage.this, GoodPasswordChangePage.class);
            startActivity(intent);
        });
    }
}