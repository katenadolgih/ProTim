package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
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

import org.hse.protim.DTO.collection.EditCollectionDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.collection.CollectionClient;

public class SelectionEditingPage extends BaseActivity {


    private ImageButton buttonBack;
    private Button saveButton;
    private TextView deleteSelection;
    private TextInputEditText nameField, descriptionField;
    private RetrofitProvider retrofitProvider;
    private CollectionClient collectionClient;
    private Long collectionId;


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

        nameField = findViewById(R.id.nameField);
        descriptionField = findViewById(R.id.descriptionField);

        retrofitProvider = new RetrofitProvider(this);
        collectionClient = new CollectionClient(retrofitProvider);

        collectionId = getIntent().getLongExtra("collectionId", -1);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
        saveButton.setOnClickListener(v -> saveButtonHandle());
        deleteSelection.setOnClickListener(v -> {
            deleteButtonHandle();
        });
    }

    private void saveButtonHandle() {
        String newCollectionName = nameField.getText().toString();

        collectionClient.updateCollection(new EditCollectionDTO(
                collectionId,
                nameField.getText().toString(),
                descriptionField.getText().toString()
        ), new CollectionClient.NoGetCollectionCallback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(SelectionEditingPage.this, SelectionDetailsPage.class);
                intent.putExtra("collectionId", collectionId);
                intent.putExtra("collectionName", newCollectionName);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SelectionEditingPage.this, message, Toast.LENGTH_LONG)
                        .show());
            }
        });
    }

    private void deleteButtonHandle() {
        collectionClient.deleteCollectionById(collectionId, new CollectionClient.NoGetCollectionCallback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(SelectionEditingPage.this, SelectionsAllPage.class);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SelectionEditingPage.this, message, Toast.LENGTH_LONG)
                        .show());
            }
        });
    }

}