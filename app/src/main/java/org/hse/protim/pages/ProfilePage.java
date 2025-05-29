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
import android.widget.ImageButton;
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

import org.hse.protim.DTO.profile.ProfileCheckDTO;
import org.hse.protim.DTO.profile.ProfileInfoDTO;
import org.hse.protim.DTO.profile.ProfilePreviewDTO;
import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.profile.ProfileClient;
import org.hse.protim.clients.retrofit.projects.ProjectClient;
import org.hse.protim.utils.auth.TokenStorage;

import java.util.List;
import java.util.Objects;

public class ProfilePage extends BaseActivity {
    private ImageView buttonInfo, moreOptions;
    private RecyclerView recyclerView;
    private ProjectsAdapter projectAdapter;
    private static ProjectClient projectClient;
    private ProfileClient profileClient;
    private RetrofitProvider retrofitProvider;
    private ShapeableImageView specialistPhoto;
    private TextView specialistName;
    private TextView specialistAge;
    private TextView specialistAgeText;
    private TextView specialistCity;
    private TextView specialistStatus;
    private TokenStorage tokenStorage;
    private Long projectId, userId, lessonId;
    private String fromPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        specialistPhoto = findViewById(R.id.specialist_photo);
        specialistName = findViewById(R.id.specialist_name);
        specialistAge = findViewById(R.id.specialist_age);
        specialistAgeText = findViewById(R.id.specialist_age_text);
        specialistCity = findViewById(R.id.specialist_city);
        specialistStatus = findViewById(R.id.specialist_status);
        tokenStorage = new TokenStorage(ProfilePage.this);
        recyclerView = findViewById(R.id.recycler_projects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonInfo = findViewById(R.id.button_info);

        Intent intent = getIntent();
        projectId = intent.getLongExtra("projectId", -1);
        userId = intent.getLongExtra("userId", -1);
        lessonId = intent.getLongExtra("lessonId", -1);
        fromPage = intent.getStringExtra("fromPage");

        moreOptions = findViewById(R.id.more_options);
        moreOptions.setOnClickListener(this::showPopupMenu);

        retrofitProvider = new RetrofitProvider(ProfilePage.this);
        projectClient = new ProjectClient(retrofitProvider);
        profileClient = new ProfileClient(retrofitProvider);

        if (fromPage != null && Objects.equals(fromPage, "project")) {
            profileClient.getProfileCheckFromProject(projectId, new ProfileClient.ProfileCheckCallback() {
                @Override
                public void onSuccess(ProfileCheckDTO profileCheckDTO) {
                    if (profileCheckDTO.isOwn()) {
                        setupProjectsOwn();
                        setupProfileOwn();
                        buttonInfo.setOnClickListener(v -> showInfoBottomSheetOwn());
                    } else {
                        moreOptions.setVisibility(View.GONE);
                        setupProjectsFromProject();
                        setupProfileFromProject();
                        buttonInfo.setOnClickListener(v -> showInfoBottomSheetProfileProject());
                    }
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> Toast.makeText(ProfilePage.this, message, Toast.LENGTH_LONG)
                            .show());
                }
            });
        } else if (fromPage != null && Objects.equals(fromPage, "specialist")) {
            profileClient.getProfileCheckFromSpecialist(userId, new ProfileClient.ProfileCheckCallback() {
                @Override
                public void onSuccess(ProfileCheckDTO profileCheckDTO) {
                    if (profileCheckDTO.isOwn()) {
                        setupProjectsOwn();
                        setupProfileOwn();
                        buttonInfo.setOnClickListener(v -> showInfoBottomSheetOwn());
                    } else {
                        moreOptions.setVisibility(View.GONE);
                        setupProjectsFromSpecialist();
                        setupProfileFromSpecialist();
                        buttonInfo.setOnClickListener(v -> showInfoBottomSheetProfileUser());
                    }
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> Toast.makeText(ProfilePage.this, message, Toast.LENGTH_LONG)
                            .show());
                }
            });
        } else if (fromPage != null && Objects.equals(fromPage, "lesson")) {
            profileClient.getProfileCheckFromLesson(lessonId, new ProfileClient.ProfileCheckCallback() {
                @Override
                public void onSuccess(ProfileCheckDTO profileCheckDTO) {
                    if (profileCheckDTO.isOwn()) {
                        setupProjectsOwn();
                        setupProfileOwn();
                        buttonInfo.setOnClickListener(v -> showInfoBottomSheetOwn());
                    } else {
                        moreOptions.setVisibility(View.GONE);
                        setupProjectsFromLesson();
                        setupProfileFromLesson();
                        buttonInfo.setOnClickListener(v -> showInfoBottomSheetProfileLesson());
                    }
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> Toast.makeText(ProfilePage.this, message, Toast.LENGTH_LONG)
                            .show());
                }
            });
        } else {
            setupProjectsOwn();
            setupProfileOwn();
            buttonInfo.setOnClickListener(v -> showInfoBottomSheetOwn());
        }
    }

    private void setupProjectsOwn() {
        projectClient.getAuthorProjects(new ProjectClient.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projects) {
                if (projects != null) {
                    projectAdapter = new ProjectsAdapter(projects);
                    recyclerView.setAdapter(projectAdapter);
                }
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ProfilePage.this,
                        message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupProjectsFromProject() {
        projectClient.getAuthorProjectsByProject(projectId, new ProjectClient.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projects) {
                if (projects != null) {
                    projectAdapter = new ProjectsAdapter(projects);
                    recyclerView.setAdapter(projectAdapter);
                }
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ProfilePage.this,
                        message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupProjectsFromSpecialist() {
        projectClient.getAuthorProjectsByUser(userId, new ProjectClient.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projects) {
                if (projects != null) {
                    projectAdapter = new ProjectsAdapter(projects);
                    recyclerView.setAdapter(projectAdapter);
                }
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ProfilePage.this,
                        message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupProjectsFromLesson() {
        projectClient.getAuthorProjectsByLesson(lessonId, new ProjectClient.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projects) {
                if (projects != null) {
                    projectAdapter = new ProjectsAdapter(projects);
                    recyclerView.setAdapter(projectAdapter);
                }
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ProfilePage.this,
                        message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupProfileOwn() {
        profileClient.getProfilePreview(new ProfileClient.ProfilePreviewCallback() {
            @Override
            public void onSuccess(ProfilePreviewDTO previewDTO) {
                Integer age = previewDTO.age();
                if (age != null) {
                    specialistAge.setText(String.valueOf(age));
                    switch (age % 10) {
                        case 0, 5, 6, 7, 8, 9 -> specialistAgeText.setText("лет");
                        case 1 -> specialistAgeText.setText("год");
                        case 2, 3, 4 -> specialistAgeText.setText("года");
                    }
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

    private void setupProfileFromProject() {
        profileClient.getProfilePreviewFromProject(projectId, new ProfileClient.ProfilePreviewCallback() {
            @Override
            public void onSuccess(ProfilePreviewDTO previewDTO) {
                Integer age = previewDTO.age();
                if (age != null) {
                    specialistAge.setText(String.valueOf(age));
                    switch (age % 10) {
                        case 0, 5, 6, 7, 8, 9 -> specialistAgeText.setText("лет");
                        case 1 -> specialistAgeText.setText("год");
                        case 2, 3, 4 -> specialistAgeText.setText("года");
                    }
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

    private void setupProfileFromSpecialist() {
        profileClient.getProfilePreviewFromSpecialist(userId, new ProfileClient.ProfilePreviewCallback() {
            @Override
            public void onSuccess(ProfilePreviewDTO previewDTO) {
                Integer age = previewDTO.age();
                if (age != null) {
                    specialistAge.setText(String.valueOf(age));
                    switch (age % 10) {
                        case 0, 5, 6, 7, 8, 9 -> specialistAgeText.setText("лет");
                        case 1 -> specialistAgeText.setText("год");
                        case 2, 3, 4 -> specialistAgeText.setText("года");
                    }
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

    private void setupProfileFromLesson() {
        profileClient.getProfilePreviewFromLesson(lessonId, new ProfileClient.ProfilePreviewCallback() {
            @Override
            public void onSuccess(ProfilePreviewDTO previewDTO) {
                Integer age = previewDTO.age();
                if (age != null) {
                    specialistAge.setText(String.valueOf(age));
                    switch (age % 10) {
                        case 0, 5, 6, 7, 8, 9 -> specialistAgeText.setText("лет");
                        case 1 -> specialistAgeText.setText("год");
                        case 2, 3, 4 -> specialistAgeText.setText("года");
                    }
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

    private void showInfoBottomSheetOwn() {
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

    private void showInfoBottomSheetProfileProject() {
        profileClient.getProfileInfoFromProject(projectId, new ProfileClient.ProfileInfoCallback() {
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

    private void showInfoBottomSheetProfileUser() {
        profileClient.getProfileInfoFromSpecialist(userId, new ProfileClient.ProfileInfoCallback() {
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

    private void showInfoBottomSheetProfileLesson() {
        profileClient.getProfileInfoFromLesson(lessonId, new ProfileClient.ProfileInfoCallback() {
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
                    tokenStorage.clearTokens();
                    Intent intent = new Intent(ProfilePage.this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
    public static class ProjectsAdapter extends RecyclerView.Adapter<SearchPage.ProjectsAdapter.ViewHolder> {

        private final List<ProjectDTO> projects;

        public ProjectsAdapter(List<ProjectDTO> projects) {
            this.projects = projects;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ShapeableImageView projectImage;
            ImageButton favoriteButton, likeButton;
            TextView projectDescription, projectHashtags, projectAuthor, likesCount;


            public ViewHolder(View itemView) {
                super(itemView);
                projectImage = itemView.findViewById(R.id.projectImage);
                favoriteButton = itemView.findViewById(R.id.favoriteButton);
                likeButton = itemView.findViewById(R.id.likeButton);
                projectDescription = itemView.findViewById(R.id.projectDescription);
                projectHashtags = itemView.findViewById(R.id.projectHashtags);
                projectAuthor = itemView.findViewById(R.id.projectAuthor);
                likesCount = itemView.findViewById(R.id.likesCount);
            }
        }

        @NonNull
        @Override
        public SearchPage.ProjectsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_project, parent, false);
            return new SearchPage.ProjectsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchPage.ProjectsAdapter.ViewHolder holder, int position) {
            ProjectDTO p = projects.get(position);
            Long projectId = p.projectId();

            Glide.with(holder.itemView.getContext()).clear(holder.projectImage);
            Glide.with(holder.itemView.getContext())
                    .load(p.photoPath())
                    .into(holder.projectImage);

            holder.projectDescription.setText(p.name());
            holder.projectHashtags.setText(String.join(" ", p.tags()));
            holder.projectAuthor.setText(p.fullName());
            holder.likesCount.setText(String.valueOf(p.likesCount()));

            projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
                @Override
                public void onSuccess(Boolean isLike) {
                    holder.favoriteButton.setSelected(isLike);
                }

                @Override
                public void onError(String message) {
                }
            });
            projectClient.checkLikeStatus(p.projectId(), new ProjectClient.LikeCallback() {
                @Override
                public void onSuccess(Boolean isLike) {
                    holder.likeButton.setSelected(isLike);
                }

                @Override
                public void onError(String message) {
                }
            });

            holder.favoriteButton.setOnClickListener(v -> {
                projectClient.updateFavourites(projectId, new ProjectClient.PutLikeCallBack() {
                    @Override
                    public void onSuccess() {
                        projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
                            @Override
                            public void onSuccess(Boolean isLike) {
                                holder.favoriteButton.setSelected(isLike);
                            }

                            @Override
                            public void onError(String message) {
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                    }
                });
            });

            holder.likeButton.setOnClickListener(v -> {
                projectClient.putLike(projectId, new ProjectClient.PutLikeCallBack() {
                    @Override
                    public void onSuccess() {
                        projectClient.getProjectLikeCount(projectId, new ProjectClient.LikeCountCallback() {
                            @Override
                            public void onSuccess(Integer count) {
                                holder.likesCount.setText(String.valueOf(count));
                                holder.likeButton.setSelected(!holder.likeButton.isSelected());
                            }

                            @Override
                            public void onError(String message) {
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            });

            holder.likesCount.setOnClickListener(v -> {
                Context currContext = v.getContext();
                Intent intent = new Intent(currContext, RatedPage.class);
                intent.putExtra("PROJECT_ID", projectId);
                currContext.startActivity(intent);
            });

//            holder.projectAuthor.setOnClickListener(v -> {
//                Toast.makeText(v.getContext(), "Автор: " + p.author, Toast.LENGTH_SHORT).show();
//            });
//
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ProjectDetailsPage.class);
                intent.putExtra("project_id", p.projectId());
                intent.putExtra("project_name", p.name());
                intent.putExtra("author", p.fullName());
                v.getContext().startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return projects.size();
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
