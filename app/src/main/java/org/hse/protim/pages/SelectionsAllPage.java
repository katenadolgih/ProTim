package org.hse.protim.pages;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.DTO.collection.CollectionDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.collection.CollectionClient;

import java.util.ArrayList;
import java.util.List;

public class SelectionsAllPage extends BaseActivity {

    private ImageButton buttonBack;
    private TextView titleView;
    private LinearLayout selectionLayout;
    private RecyclerView selectionsRecycler;
    private RetrofitProvider retrofitProvider;
    private CollectionClient collectionClient;
    private List<CollectionDTO> selections = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selections_all_page);


        init();
        handle();
        loadSampleData();

        titleView.setText(R.string.selections_all_title);
    }


    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        selectionLayout = findViewById(R.id.selection);
        selectionsRecycler = findViewById(R.id.selectionsRecycler);
        retrofitProvider = new RetrofitProvider(this);
        collectionClient = new CollectionClient(retrofitProvider);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
    }

    private void loadSampleData() {
        collectionClient.getCollectionPreviewAll(new CollectionClient.GetCollectionPreviewAll() {
            @Override
            public void onSuccess(List<CollectionDTO> collectionDTOS) {
                selections.addAll(collectionDTOS);
                setupAdapters();
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SelectionsAllPage.this, message, Toast.LENGTH_LONG)
                        .show());
            }
        });

    }

    private void setupAdapters() {
        // Список подборок
        selectionsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        selectionsRecycler.setAdapter(new SelectionAdapter(selections, false));

    }
}