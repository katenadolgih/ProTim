package org.hse.protim.pages;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import org.hse.protim.DTO.collection.UpdateFavouritesDTO;
import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.favourites.FavouritesClient;
import org.hse.protim.clients.retrofit.projects.ProjectClient;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private final List<ProjectDTO> projects;
    private final Context context;
    private final OnItemClickListener listener;
    private ProjectClient projectClient;
    private RetrofitProvider retrofitProvider;
    private final OnFavouriteRemoveCallback callback;
    private Long collectionId;
    private FavouritesClient favouritesClient;

    public interface OnItemClickListener {
        void onItemClick(ProjectDTO project);
    }

    public ProjectAdapter(Context context,
                          List<ProjectDTO> projects,
                          OnItemClickListener listener,
                          OnFavouriteRemoveCallback callback,
                          Long collectionId
    ) {
        this.context = context;
        this.projects = projects;
        this.listener = listener;
        this.callback = callback;
        this.collectionId = collectionId;

        retrofitProvider = new RetrofitProvider(context);
        projectClient = new ProjectClient(retrofitProvider);
        favouritesClient = new FavouritesClient(retrofitProvider);
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        ProjectDTO project = projects.get(position);
        Long projectId = project.projectId();
        // Загружаем картинку
        Glide.with(context)
                .load(project.photoPath())
                .into(holder.image);

        holder.description.setText(project.name());
        holder.hashtags.setText(TextUtils.join(" ", project.tags()));
        holder.author.setText(project.fullName());
        holder.likesCount.setText(String.valueOf(project.likesCount()));

        holder.itemView.setOnClickListener(v -> projectClickHandle(project, context));

        likeButtonHandler(projectId, holder.likeButton, holder.likesCount);
        favouritesHandler(project.projectId(), holder.favouriteButton, holder.itemView);
        projectAuthorHandler(projectId, holder.author);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        final ShapeableImageView image;
        final TextView description, hashtags, author, likesCount;
        final ImageButton favouriteButton, likeButton;

        ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            favouriteButton = itemView.findViewById(R.id.favoriteButton);
            image      = itemView.findViewById(R.id.projectImage);
            description= itemView.findViewById(R.id.projectDescription);
            hashtags   = itemView.findViewById(R.id.projectHashtags);
            author     = itemView.findViewById(R.id.projectAuthor);
            likesCount = itemView.findViewById(R.id.likesCount);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }

    private void favouritesHandler(Long projectId, ImageButton favouritesButton, View view) {
        Context currContext = favouritesButton.getContext();

        projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
            @Override
            public void onSuccess(Boolean isLike) {
                favouritesButton.setSelected(isLike);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(currContext, message, Toast.LENGTH_LONG).show();
            }
        });

        favouritesButton.setOnClickListener(v -> {
            if (context instanceof SelectionDetailsPage) {
                favouritesClient.removeFavourites(new UpdateFavouritesDTO(projectId,
                        collectionId), new FavouritesClient.NoRetValueCallback() {
                    @Override
                    public void onSuccess() {
                        callback.onFavouriteRemove(projectId);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(currContext, message, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                projectClient.updateFavourites(projectId, new ProjectClient.PutLikeCallBack() {
                    @Override
                    public void onSuccess() {
                        projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
                            @Override
                            public void onSuccess(Boolean isLike) {
                                callback.onFavouriteRemove(projectId);
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(currContext, message, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(currContext, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void likeButtonHandler(Long projectId, ImageButton likeButton, TextView likesText) {
        Context currContext = likeButton.getContext();

        projectClient.checkLikeStatus(projectId, new ProjectClient.LikeCallback() {
            @Override
            public void onSuccess(Boolean isLike) {
                likeButton.setSelected(isLike);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(currContext, message, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(currContext, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(currContext, message, Toast.LENGTH_LONG).show();
                }
            });
        });

        likesText.setOnClickListener(v -> {
            Intent intent = new Intent(currContext, RatedPage.class);
            intent.putExtra("PROJECT_ID", projectId);
            v.getContext().startActivity(intent);
        });
    }

    private void projectAuthorHandler(Long projectId, TextView projectAuthor) {
        projectAuthor.setOnClickListener(v -> {
            Context currContext = v.getContext();
            Intent intent = new Intent(currContext, ProfilePage.class);
            intent.putExtra("projectId", projectId);
            intent.putExtra("fromPage", "project");
            currContext.startActivity(intent);
        });
    }

    private void projectClickHandle(ProjectDTO p, Context context) {
        Intent intent = new Intent(context, ProjectDetailsPage.class);
        intent.putExtra("project_id", p.projectId());
        intent.putExtra("project_name", p.name());
        intent.putExtra("author", p.fullName());
        context.startActivity(intent);
    }
}
