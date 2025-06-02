package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.DTO.collection.CollectionCountDTO;
import org.hse.protim.DTO.collection.CollectionDTO;
import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.collection.CollectionClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SelectionDetailsPage extends BaseActivity {
    private ImageButton buttonBack;
    private TextView titleView;
    private RecyclerView recyclerNewProjects;
    private RetrofitProvider retrofitProvider;
    private CollectionClient collectionClient;
    private Long collectionId;
    private String collectionName;
    private String fromPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selection_details_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.selection_details_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        handle();
        initViews();

        findViewById(R.id.more_selection).setOnClickListener(this::showPopupMenuSelection);
    }

    @Override
    public void onBackPressed() {
        collectionClient.getCollectionPreviewAll(new CollectionClient.GetCollectionPreviewAll() {
            @Override
            public void onSuccess(List<CollectionDTO> collectionDTOS) {
                Intent intent;
                if (collectionDTOS == null || collectionDTOS.isEmpty()) {
                    intent = new Intent(SelectionDetailsPage.this, FavoritesPage.class);
                    startActivity(intent);
                } else {
                    if (!Objects.equals(fromPage, "favourites")) {
                        intent = new Intent(SelectionDetailsPage.this, SelectionsAllPage.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(SelectionDetailsPage.this, FavoritesPage.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SelectionDetailsPage.this,
                        message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
//        settingsButton.setOnClickListener(v -> {
//            Intent intent = new Intent(SelectionDetailsPage.this, FiltersPage.class);
//            startActivity(intent);
//        });
    }
    private void initViews() {
        buttonBack = findViewById(R.id.button_back);
        titleView  = findViewById(R.id.title_text);
        titleView.setText(collectionName);


        recyclerNewProjects = findViewById(R.id.favoriteProjectsRecycler);
        recyclerNewProjects.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        );

        collectionClient.getCollectionProjects(collectionId, new CollectionClient.GetCollectionProjectsCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> collectionProjects) {
                SelectionDetailsPage context = SelectionDetailsPage.this;
                recyclerNewProjects.setAdapter(new ProjectAdapter(context,
                        collectionProjects,
                        project -> {
                            // клик по элементу — открываем детали
                            Intent intent = new Intent(context, ProjectDetailsPage.class);
                            intent.putExtra("project_id", project.projectId());
                            intent.putExtra("project_name", project.name());
                            startActivity(intent);
                        },
                        projectId -> recreate(),
                        collectionId
                ));
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SelectionDetailsPage.this, message, Toast.LENGTH_LONG)
                        .show());
            }
        });
    }
    public void showPopupMenuSelection(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.selection_popup_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_edit) {
                Intent intent = new Intent(this, SelectionEditingPage.class);
                intent.putExtra("collectionId", collectionId);
                startActivity(intent);
                return true;
            } else if (id == R.id.action_logout) {
                showSettingsDialog(); // вызов попапа
                return true;
            }
            return false;
        });

        popupMenu.show();
        MenuItem logoutItem = popupMenu.getMenu().findItem(R.id.action_logout);
        SpannableString s = new SpannableString(logoutItem.getTitle());
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)), 0, s.length(), 0);
        logoutItem.setTitle(s);
    }
    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Удалить подборку?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    collectionClient.deleteCollectionById(collectionId, new CollectionClient.NoGetCollectionCallback() {
                        @Override
                        public void onSuccess() {
                            collectionClient.getCollectionCount(new CollectionClient.GetCollectionCountCallback() {
                                @Override
                                public void onSuccess(CollectionCountDTO collectionCountDTO) {
                                    Intent intent;// Закрываем текущую страницу
                                    if (collectionCountDTO.count() > 0) {
                                        intent = new Intent(SelectionDetailsPage.this, SelectionsAllPage.class);
                                    } else {
                                        intent = new Intent(SelectionDetailsPage.this, FavoritesPage.class);
                                    }
                                    startActivity(intent);
                                    finish(); // Закрываем текущую страницу
                                }

                                @Override
                                public void onError(String message) {
                                    runOnUiThread(() -> Toast.makeText(SelectionDetailsPage.this, message, Toast.LENGTH_LONG)
                                            .show());
                                }
                            });
                        }

                        @Override
                        public void onError(String message) {
                            runOnUiThread(() -> Toast.makeText(SelectionDetailsPage.this,
                                    message, Toast.LENGTH_LONG).show());
                        }
                    });
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        retrofitProvider = new RetrofitProvider(this);
        collectionClient = new CollectionClient(retrofitProvider);

        Intent intent = getIntent();
        collectionId = intent.getLongExtra("collectionId", -1);
        collectionName = intent.getStringExtra("collectionName");
        fromPage = Optional.ofNullable(intent.getStringExtra("fromPage")).orElse("");
//        settingsButton = findViewById(R.id.settings_button);

    }


}