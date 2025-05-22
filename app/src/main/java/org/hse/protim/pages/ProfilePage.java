package org.hse.protim.pages;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;

import org.hse.protim.DTO.profile.ProfileInfoDTO;
import org.hse.protim.DTO.profile.ProfilePreviewDTO;
import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.profile.ProfileClient;
import org.hse.protim.clients.retrofit.projects.ProjectClient;

import java.util.List;

public class ProfilePage extends BaseActivity {
    private ImageView buttonInfo;
    private RecyclerView recyclerView;
    private ProjectAdapter projectAdapter;
    private ProjectClient projectClient;
    private ProfileClient profileClient;
    private RetrofitProvider retrofitProvider;
    private ShapeableImageView specialistPhoto;
    private TextView specialistName;
    private TextView specialistAge;
    private TextView specialistCity;
    private TextView specialistStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        specialistPhoto = findViewById(R.id.specialist_photo);
        specialistName = findViewById(R.id.specialist_name);
        specialistAge = findViewById(R.id.specialist_age);
        specialistCity = findViewById(R.id.specialist_city);
        specialistStatus = findViewById(R.id.specialist_status);

        buttonInfo = findViewById(R.id.button_info);
        buttonInfo.setOnClickListener(v -> showInfoBottomSheet());

        findViewById(R.id.more_options).setOnClickListener(this::showPopupMenu);

        retrofitProvider = new RetrofitProvider(ProfilePage.this);
        projectClient = new ProjectClient(retrofitProvider);
        profileClient = new ProfileClient(retrofitProvider);

        setupProjects();
        setupProfile();
    }
    private void setupProjects() {
        recyclerView = findViewById(R.id.recycler_projects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        projectClient.getAuthorProjects(new ProjectClient.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projects) {
                projectAdapter = new ProjectAdapter(projects);
                recyclerView.setAdapter(projectAdapter);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ProfilePage.this,
                        message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupProfile() {
        profileClient.getProfilePreview(new ProfileClient.ProfilePreviewCallback() {
            @Override
            public void onSuccess(ProfilePreviewDTO previewDTO) {
                if (previewDTO.age() != null) {
                    specialistAge.setText(previewDTO.age().toString());
                }
                specialistName.setText(previewDTO.fullName());
                specialistCity.setText(previewDTO.city());
                specialistStatus.setText(previewDTO.status());

                Glide.with(specialistPhoto.getContext()).clear(specialistPhoto);
                Glide.with(specialistPhoto.getContext())
                        .load(previewDTO.photoPath())
                        .into(specialistPhoto);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ProfilePage.this, message, Toast.LENGTH_LONG).show());
            }
        });
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
                showLogoutDialog();
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
        profileClient.getProfileInfo(new ProfileClient.ProfileInfoCallback() {
            @Override
            public void onSuccess(ProfileInfoDTO profileInfoDTO) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProfilePage.this);
                View view = getLayoutInflater().inflate(R.layout.bottom_sheet_info, null);

                FlexboxLayout sectionContainer = view.findViewById(R.id.section_container);
                FlexboxLayout softwareContainer = view.findViewById(R.id.software_container_filer);

                addTags(sectionContainer, profileInfoDTO.sectionAndStamps().toArray(new String[0]));
                addTags(softwareContainer, profileInfoDTO.softwareSkills().toArray(new String[0]));

                String resumePath = profileInfoDTO.resumePath();
                String resumeName = profileInfoDTO.resumeName();
                TextView resumeLink = view.findViewById(R.id.resume_link);
                resumeLink.setOnClickListener(v -> downloadFile(resumePath, resumeName,
                        "Скачивание резюме.."));

                TextView education = view.findViewById(R.id.education);

                TextView diplomaLink = view.findViewById(R.id.diploma_link);
                String diplomaPath = profileInfoDTO.diplomaPath();
                String diplomaName = profileInfoDTO.diplomaName();
                diplomaLink.setOnClickListener(v -> downloadFile(diplomaPath, diplomaName,
                        "Скачивание диплома"));

                TextView socialNetworkLink = view.findViewById(R.id.vk_link);
                TextView telegramLink = view.findViewById(R.id.telegram_link);
                TextView aboutMe = view.findViewById(R.id.about_me);

                resumeLink.setText(resumeName);
                education.setText(String.join("\n", profileInfoDTO.education()));
                diplomaLink.setText(diplomaName);
                socialNetworkLink.setText(String.join("\n", profileInfoDTO.socialNetworks()));
                telegramLink.setText(profileInfoDTO.telegram());
                aboutMe.setText(profileInfoDTO.about());

                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ProfilePage.this, message, Toast.LENGTH_LONG).show());
            }
        });


    }

    private void downloadFile(String fileUrl, String name, String description) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setTitle(name);
        request.setDescription(description);
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        );

        request.setDestinationInExternalFilesDir(
                this,
                Environment.DIRECTORY_DOWNLOADS,
                name
        );

        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(request);
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
    public static class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private final List<ProjectDTO> projectList;

        public ProjectAdapter(List<ProjectDTO> projectList) {
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
            ProjectDTO project = projectList.get(position);
            Glide.with(holder.itemView.getContext()).clear(holder.projectImage);
            Glide.with(holder.itemView.getContext())
                    .load(project.photoPath())
                    .into(holder.projectImage);

            holder.projectDescription.setText(project.name());
            holder.projectHashtags.setText(String.join(" ", project.tags()));
            holder.projectAuthor.setText(project.fullName());
            holder.likesCount.setText(String.valueOf(project.likesCount()));
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
    private void addTags(FlexboxLayout container, String[] tags) {
        for (String tag : tags) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_filter_tag, container, false);
            TextView text = view.findViewById(R.id.tag_text);

            text.setText(tag);

            container.addView(view);
        }
    }
}
