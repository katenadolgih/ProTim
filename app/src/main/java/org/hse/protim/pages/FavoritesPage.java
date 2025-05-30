package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.DTO.collection.CollectionDTO;
import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.collection.CollectionClient;
import org.hse.protim.clients.retrofit.favourites.FavouritesClient;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPage extends BaseActivity {

    private ImageButton addSelection;
    private TextView seeAllSelections;
    private LinearLayout selectionLayout;
    private RecyclerView selectionsRecycler;
    private CollectionClient collectionClient;
    private FavouritesClient favouritesClient;
    private RetrofitProvider retrofitProvider;

    private List<CollectionDTO> collectionDTOS = new ArrayList<>();
    private TextView titleView;
    private ImageButton buttonBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_page);

        init();
        handle();
        loadSampleData();

        titleView.setText(R.string.favorites);

        RecyclerView recyclerPopularProjects = findViewById(R.id.popularProjectsRecycler);
        recyclerPopularProjects.setLayoutManager(new LinearLayoutManager(this));

        setProjects(recyclerPopularProjects);
    }

    private void setProjects(RecyclerView recyclerPopularProjects) {
        favouritesClient.getFavourites(new FavouritesClient.GetFavouritesCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projectDTOS) {
                ProjectAdapter adapter = new ProjectAdapter(
                        FavoritesPage.this,
                        projectDTOS,
                        project -> {
                            Intent intent = new Intent(FavoritesPage.this, ProjectDetailsPage.class);
                            intent.putExtra("PROJECT_ID", project.projectId());
                            startActivity(intent);
                        }
                );
                recyclerPopularProjects.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(FavoritesPage.this, message, Toast.LENGTH_LONG));
            }
        });
    }

    private void init() {
        addSelection = findViewById(R.id.add_selection);
        seeAllSelections = findViewById(R.id.seeAllSelections);
        selectionLayout = findViewById(R.id.selection);
        selectionsRecycler = findViewById(R.id.selectionsRecycler);
        titleView = findViewById(R.id.title_text);
        buttonBack = findViewById(R.id.button_back);

        retrofitProvider = new RetrofitProvider(this);
        collectionClient = new CollectionClient(retrofitProvider);
        favouritesClient = new FavouritesClient(retrofitProvider);
    }

    private void handle() {
        findViewById(R.id.toolbar).findViewById(R.id.button_back).setOnClickListener(v -> onBackPressed());
        buttonBack.setVisibility(View.GONE);
        addSelection.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesPage.this, SelectionCreatingPage.class);
            startActivity(intent);
        });

        seeAllSelections.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesPage.this, SelectionsAllPage.class);
            startActivity(intent);
        });
    }

    private void loadSampleData() {
        collectionClient.getCollectionPreview(new CollectionClient.GetCollectionPreviewCallback() {
            @Override
            public void onSuccess(CollectionDTO collectionDTO) {
                if (collectionDTO != null) {
                    collectionDTOS.add(collectionDTO);
                } else {
                    selectionLayout.setVisibility(View.GONE);
                }
                setupAdapters();
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(FavoritesPage.this
                        , message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupAdapters() {
        selectionsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectionsRecycler.setAdapter(new SelectionAdapter(collectionDTOS, false));
    }
}
