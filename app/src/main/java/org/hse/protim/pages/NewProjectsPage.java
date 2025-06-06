package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class NewProjectsPage extends BaseActivity {

    private ImageButton buttonBack;
    private TextView titleView;
    private ImageButton settingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_projects_page);

        init();
        handle();

        titleView.setText(R.string.New_projects);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_projects_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        RecyclerView recyclerNewProjects = findViewById(R.id.newProjectsRecycler);
        recyclerNewProjects.setLayoutManager(new LinearLayoutManager(this));

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> projectViews = new ArrayList<>();

        View project1 = inflater.inflate(R.layout.item_project, recyclerNewProjects, false);
        ((ImageView) project1.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project1.findViewById(R.id.projectDescription)).setText("Популярный проект A — исследование ИИ");
        ((TextView) project1.findViewById(R.id.projectHashtags)).setText("#AI #Research #Tech");
        ((TextView) project1.findViewById(R.id.projectAuthor)).setText("Смирнов Алексей");
        ((TextView) project1.findViewById(R.id.likesCount)).setText("105");

        View project2 = inflater.inflate(R.layout.item_project, recyclerNewProjects, false);
        ((ImageView) project2.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project2.findViewById(R.id.projectDescription)).setText("Популярный проект B — мобильное приложение для экологии");
        ((TextView) project2.findViewById(R.id.projectHashtags)).setText("#Eco #Mobile #UX");
        ((TextView) project2.findViewById(R.id.projectAuthor)).setText("Кузнецова Ирина");
        ((TextView) project2.findViewById(R.id.likesCount)).setText("321");

        View project3 = inflater.inflate(R.layout.item_project, recyclerNewProjects, false);
        ((ImageView) project3.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project3.findViewById(R.id.projectDescription)).setText("Популярный проект C — цифровая платформа образования");
        ((TextView) project3.findViewById(R.id.projectHashtags)).setText("#EdTech #Platform #Java");
        ((TextView) project3.findViewById(R.id.projectAuthor)).setText("Иванова Мария");
        ((TextView) project3.findViewById(R.id.likesCount)).setText("678");

        projectViews.add(project1);
        projectViews.add(project2);
        projectViews.add(project3);

        recyclerNewProjects.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        settingsButton = findViewById(R.id.settings_button);

    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(NewProjectsPage.this, FiltersPage.class);
            startActivity(intent);
        });
    }
}
