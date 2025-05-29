package org.hse.protim.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.projects.ProjectClient;
import org.hse.protim.pages.HomePage;
import org.hse.protim.pages.NewProjectsPage;
import org.hse.protim.pages.PopularProjectsPage;
import org.hse.protim.pages.ProfilePage;
import org.hse.protim.pages.ProjectDetailsPage;
import org.hse.protim.pages.RatedPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectUtils {
    public void setNewProjects(LayoutInflater inflater, Context context, String filter,
                               String name,
                               List<String> sectionAndStamp,
                               List<String> softwareSkill,
                               ProjectClient projectClient,
                                RecyclerView recyclerPopularProjects) {
        projectClient.getProjects(filter, null, name, sectionAndStamp, softwareSkill,
                new ProjectClient.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projects) {
                recyclerPopularProjects.setLayoutManager(new LinearLayoutManager(context));
                List<View> projectViews = new ArrayList<>();

                for (ProjectDTO project : projects) {
                    View projectView = inflater.inflate(R.layout.item_project, recyclerPopularProjects, false);

                    ((TextView) projectView.findViewById(R.id.projectDescription)).setText(project.name());
                    ((TextView) projectView.findViewById(R.id.projectHashtags)).setText(project.tags().stream().map(tag -> "#" + tag)
                            .collect(Collectors.joining("  ")));
                    ((TextView) projectView.findViewById(R.id.projectAuthor)).setText(project.fullName());
                    ((TextView) projectView.findViewById(R.id.likesCount)).setText(project.likesCount().toString());
                    TextView projectAuthor = projectView.findViewById(R.id.projectAuthor);

                    ImageView imageView = projectView.findViewById(R.id.projectImage);
                    Glide.with(context)
                            .load(project.photoPath())
                            .into(imageView);

                    ImageButton likeButton = projectView.findViewById(R.id.likeButton);
                    ImageButton favoriteButton = projectView.findViewById(R.id.favoriteButton);
                    TextView likesText = projectView.findViewById(R.id.likesCount);

                    Long projectId = project.projectId();

                    likeButtonHandler(projectId, likeButton, likesText, projectClient, context);
                    favouritesHandler(projectId, favoriteButton, projectClient, context);
                    projectAuthorHandler(projectId, projectAuthor);

                    projectViews.add(projectView);
                }

                recyclerPopularProjects.setAdapter(new RecyclerView.Adapter<>() {
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return new RecyclerView.ViewHolder(projectViews.get(viewType)) {
                        };
                    }

                    @Override
                    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                        ProjectDTO p = projects.get(position);
                        holder.itemView.setOnClickListener(v -> {
                            Context currContext = holder.itemView.getContext();
                            Intent intent = new Intent(currContext, ProjectDetailsPage.class);
                            intent.putExtra("project_id", p.projectId());
                            intent.putExtra("project_name", p.name());
                            intent.putExtra("author", p.fullName());
                            currContext.startActivity(intent);
                        });
                    }

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

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void projectAuthorHandler(Long projectId, TextView projectAuthor) {
        projectAuthor.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ProfilePage.class);
            intent.putExtra("projectId", projectId);
            intent.putExtra("fromPage", "project");
            context.startActivity(intent);
        });
    }

    private void likeButtonHandler(Long projectId, ImageButton likeButton, TextView likesText, ProjectClient projectClient, Context context) {
        projectClient.checkLikeStatus(projectId, new ProjectClient.LikeCallback() {
            @Override
            public void onSuccess(Boolean isLike) {
                likeButton.setSelected(isLike);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });

        likeButton.setOnClickListener(v -> {
            projectClient.putLike(projectId, new ProjectClient.PutLikeCallBack() {
                @Override
                public void onSuccess() {
                    boolean newLikeState = !likeButton.isSelected();
                    likeButton.setSelected(newLikeState);

                    projectClient.getProjectLikeCount(projectId, new ProjectClient.LikeCountCallback() {
                        @Override
                        public void onSuccess(Integer count) {
                            likesText.setText(count.toString());
                        }
                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            });
        });

        likesText.setOnClickListener(v -> {
            Intent intent = new Intent(context, RatedPage.class);
            intent.putExtra("PROJECT_ID", projectId);
            context.startActivity(intent);
        });
    }

    private void favouritesHandler(Long projectId, ImageButton favouritesButton, ProjectClient projectClient, Context context) {
        projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
            @Override
            public void onSuccess(Boolean isLike) {
                favouritesButton.setSelected(isLike);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });

        favouritesButton.setOnClickListener(v -> {
            projectClient.updateFavourites(projectId, new ProjectClient.PutLikeCallBack() {
                @Override
                public void onSuccess() {
                    projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
                        @Override
                        public void onSuccess(Boolean isLike) {
                            favouritesButton.setSelected(isLike);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
