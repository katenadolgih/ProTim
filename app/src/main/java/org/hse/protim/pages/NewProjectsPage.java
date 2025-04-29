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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.projects.ProjectClient;
import org.hse.protim.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewProjectsPage extends BaseActivity {

    private ImageButton buttonBack;
    private TextView titleView;
    private ImageButton settingsButton;
    private ProjectClient projectClient;
    private RetrofitProvider retrofitProvider;
    private RecyclerView recyclerNewProjects;
    private ProjectUtils projectUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_projects_page);


        init();
        handle();

        titleView.setText(R.string.New_projects);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_projects_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerNewProjects.setLayoutManager(new LinearLayoutManager(this));

        LayoutInflater inflater = LayoutInflater.from(this);
        projectUtils.setNewProjects(inflater, NewProjectsPage.this, "new", projectClient, recyclerNewProjects);
    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        settingsButton = findViewById(R.id.settings_button);
        retrofitProvider = new RetrofitProvider(NewProjectsPage.this);
        projectClient = new ProjectClient(retrofitProvider);
        recyclerNewProjects = findViewById(R.id.newProjectsRecycler);
        projectUtils = new ProjectUtils();
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> {
                Intent intent = new Intent(NewProjectsPage.this, HomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(NewProjectsPage.this, FiltersPage.class);
            startActivity(intent);
        });
    }
}
