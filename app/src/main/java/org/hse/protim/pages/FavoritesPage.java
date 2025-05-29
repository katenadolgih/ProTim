package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPage extends BaseActivity {

    private ImageButton addSelection;
    private TextView seeAllSelections;
    private LinearLayout selectionLayout;
    private RecyclerView selectionsRecycler;

    private List<Selection> selections = new ArrayList<>();
    private TextView titleView;
    private ImageButton buttonBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_page);

        init();
        handle();
        loadSampleData();
        setupAdapters();

        titleView.setText(R.string.favorites);


        RecyclerView recyclerPopularProjects = findViewById(R.id.popularProjectsRecycler);
        recyclerPopularProjects.setLayoutManager(new LinearLayoutManager(this));

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> projectViews = new ArrayList<>();

        View project1 = inflater.inflate(R.layout.item_project, recyclerPopularProjects, false);
        ((ImageView) project1.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project1.findViewById(R.id.projectDescription)).setText("Популярный проект A — исследование ИИ");
        ((TextView) project1.findViewById(R.id.projectHashtags)).setText("#AI #Research #Tech");
        ((TextView) project1.findViewById(R.id.projectAuthor)).setText("Смирнов Алексей");
        ((TextView) project1.findViewById(R.id.likesCount)).setText("105");

        View project2 = inflater.inflate(R.layout.item_project, recyclerPopularProjects, false);
        ((ImageView) project2.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project2.findViewById(R.id.projectDescription)).setText("Популярный проект B — мобильное приложение для экологии");
        ((TextView) project2.findViewById(R.id.projectHashtags)).setText("#Eco #Mobile #UX");
        ((TextView) project2.findViewById(R.id.projectAuthor)).setText("Кузнецова Ирина");
        ((TextView) project2.findViewById(R.id.likesCount)).setText("321");

        View project3 = inflater.inflate(R.layout.item_project, recyclerPopularProjects, false);
        ((ImageView) project3.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project3.findViewById(R.id.projectDescription)).setText("Популярный проект C — цифровая платформа образования");
        ((TextView) project3.findViewById(R.id.projectHashtags)).setText("#EdTech #Platform #Java");
        ((TextView) project3.findViewById(R.id.projectAuthor)).setText("Иванова Мария");
        ((TextView) project3.findViewById(R.id.likesCount)).setText("678");

        projectViews.add(project1);
        projectViews.add(project2);
        projectViews.add(project3);

        recyclerPopularProjects.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(projectViews.get(viewType)) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

            @Override
            public int getItemCount() {
                return projectViews.size();
            }

            @Override
            public int getItemViewType(int position) {
                return position;
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

    }

    private void handle() {
//        findViewById(R.id.toolbar).findViewById(R.id.button_back).setOnClickListener(v -> onBackPressed());
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
        selections.add(new Selection("Моя подборка 1", "Описание подборки"));
        selections.add(new Selection("Подборка для вдохновения", "Описание подборки"));

        if (selections.isEmpty()) {
            selectionLayout.setVisibility(View.GONE);
        }
    }

    private void setupAdapters() {
        selectionsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectionsRecycler.setAdapter(new SelectionAdapter(selections, false));

    }
}
