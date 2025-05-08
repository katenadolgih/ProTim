package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class ProfilePage extends BaseActivity {

    private ImageView buttonInfo;
    private RecyclerView recyclerView;
    private ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        buttonInfo = findViewById(R.id.button_info);
        buttonInfo.setOnClickListener(v -> showInfoBottomSheet());

        findViewById(R.id.more_options).setOnClickListener(this::showPopupMenu);

        setupProjects();
    }

    private void setupProjects() {
        recyclerView = findViewById(R.id.recycler_projects);

        // Устанавливаем LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Инициализация адаптера и установка данных
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(R.drawable.photo_project, "Технологии информационного моделирования", "#BIM #Modeling #Revit", "Железнева Екатерина", "123"));
        projects.add(new Project(R.drawable.photo_project, "Галерейного типа", "#Architecture #Gallery", "Железнева Екатерина", "123"));

        projectAdapter = new ProjectAdapter(projects);
        recyclerView.setAdapter(projectAdapter);
    }


    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.profile_popup_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_edit) {
                startActivity(new Intent(ProfilePage.this, EditProfilePage.class));
                return true;
            } else if (id == R.id.action_settings) {
                startActivity(new Intent(ProfilePage.this, SettingsPage.class));
                return true;
            } else if (id == R.id.action_logout) {
                showLogoutDialog(); // вызов попапа
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

    private void showInfoBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_info, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Выйти из профиля?")
                .setPositiveButton("Выйти", (dialog, which) -> {
                    // Пример очистки данных и выхода
                    // SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    // prefs.edit().clear().apply();
                    finishAffinity(); // Закрыть все активити
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    // Класс для хранения данных о проекте
    public static class Project {
        private int imageRes;
        private String description;
        private String hashtags;
        private String author;
        private String likesCount;

        public Project(int imageRes, String description, String hashtags, String author, String likesCount) {
            this.imageRes = imageRes;
            this.description = description;
            this.hashtags = hashtags;
            this.author = author;
            this.likesCount = likesCount;
        }

        public int getImageRes() {
            return imageRes;
        }

        public String getDescription() {
            return description;
        }

        public String getHashtags() {
            return hashtags;
        }

        public String getAuthor() {
            return author;
        }

        public String getLikesCount() {
            return likesCount;
        }
    }

    // Адаптер для RecyclerView
    public static class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private final List<Project> projectList;

        public ProjectAdapter(List<Project> projectList) {
            this.projectList = projectList;
        }

        @NonNull
        @Override
        public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
            Project project = projectList.get(position);
            holder.projectImage.setImageResource(project.getImageRes());
            holder.projectDescription.setText(project.getDescription());
            holder.projectHashtags.setText(project.getHashtags());
            holder.projectAuthor.setText(project.getAuthor());
            holder.likesCount.setText(project.getLikesCount());
        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }

        public static class ProjectViewHolder extends RecyclerView.ViewHolder {
            ImageView projectImage;
            TextView projectDescription, projectHashtags, projectAuthor, likesCount;

            public ProjectViewHolder(View itemView) {
                super(itemView);
                projectImage = itemView.findViewById(R.id.projectImage);
                projectDescription = itemView.findViewById(R.id.projectDescription);
                projectHashtags = itemView.findViewById(R.id.projectHashtags);
                projectAuthor = itemView.findViewById(R.id.projectAuthor);
                likesCount = itemView.findViewById(R.id.likesCount);
            }
        }
    }
}
