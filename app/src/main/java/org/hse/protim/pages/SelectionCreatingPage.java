package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import org.hse.protim.DTO.collection.CollectionCreateDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.collection.CollectionClient;

public class SelectionCreatingPage extends BaseActivity {

    private ImageButton buttonBack;
    private Button createButton;
    private TextInputEditText nameField, descriptionField;
    private RetrofitProvider retrofitProvider;
    private CollectionClient collectionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_creating_page);
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
        createButton = findViewById(R.id.button_create);
        nameField = findViewById(R.id.nameField);
        descriptionField = findViewById(R.id.descriptionField);

        retrofitProvider = new RetrofitProvider(this);
        collectionClient = new CollectionClient(retrofitProvider);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
        createButton.setOnClickListener(v -> createButtonHandle());
    }

    private void createButtonHandle() {

        CollectionCreateDTO collectionCreateDTO = new CollectionCreateDTO(
                nameField.getText().toString(),
                descriptionField.getText().toString()
        );

        collectionClient.createCollection(collectionCreateDTO, new CollectionClient.NoGetCollectionCallback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(SelectionCreatingPage.this, FavoritesPage.class);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SelectionCreatingPage.this, message, Toast.LENGTH_LONG)
                        .show());
            }
        });
    }
}