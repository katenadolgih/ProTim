package org.hse.protim.pages;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import org.hse.protim.R;

public class GoodPasswordChangePage extends BaseActivity {
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_password_change_page);

        init();
        handle();
    }
    private void init() {
        backButton = findViewById(R.id.button_back);
    }
    private void handle() {
        backButton.setOnClickListener(v -> onBackPressed());
    }

}