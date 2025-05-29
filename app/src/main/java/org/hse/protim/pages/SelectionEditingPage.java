package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.hse.protim.R;

public class SelectionEditingPage extends BaseActivity {


    private ImageButton buttonBack;
    private Button saveButton;
    private TextView deleteSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_editing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        handle();
    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        saveButton = findViewById(R.id.button_save);
        deleteSelection = findViewById(R.id.deleteSelection);

    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
        saveButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionEditingPage.this, FavoritesPage.class);
            startActivity(intent);
        });
        deleteSelection.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionEditingPage.this, SelectionsAllPage.class);
            startActivity(intent);
        });
    }

}