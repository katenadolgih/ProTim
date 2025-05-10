package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.hse.protim.R;

public class GoodPasswordChangePage extends BaseActivity {

    private ImageButton backButton;
    private Button okayButton;
    private TextView sendEmailMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_password_change_page);

        init();
        handle();
    }

    private void init() {
        backButton = findViewById(R.id.button_back);
        okayButton = findViewById(R.id.okayButton);
        sendEmailMessage = findViewById(R.id.send_email_message);
    }

    private void handle() {
        backButton.setOnClickListener(v -> onBackPressed());
        okayButton.setOnClickListener(v -> {
            Intent intent = new Intent(GoodPasswordChangePage.this, MainActivity.class);
            startActivity(intent);
        });
        setSendEmailMessage();
    }

    private void setSendEmailMessage() {
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        sendEmailMessage.setText("Письмо отправлено на " + email);
    }

}