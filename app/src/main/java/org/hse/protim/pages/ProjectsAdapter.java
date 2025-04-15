//package org.hse.protim.pages;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.hse.protim.R;
//
//public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {
//
//
//    @NonNull
//    @Override
//    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_project, parent, false);
//        return new ProjectViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
//        Project project = projects.get(position);
//
//        // Установка данных
//        holder.projectDescription.setText(project.getDescription());
//        holder.projectHashtags.setText(project.getHashtags());
//        holder.projectAuthor.setText(project.getAuthor());
//        holder.likesCount.setText(String.valueOf(project.getLikesCount()));
//
//        // Обработчики кликов
//        setupClickListeners(holder, project);
//    }
//
//    private void setupClickListeners(ProjectViewHolder holder, Project project) {
//        // Кнопка избранного
//        holder.favoriteButton.setChecked(project.isFavorite());
//        holder.favoriteButton.setOnClickListener(v -> {
//            boolean newState = !project.isFavorite();
//            project.setFavorite(newState);
//            holder.favoriteButton.setChecked(newState);
//
//            // Анимация
//            animateButton(holder.favoriteButton);
//        });
//
//        // Кнопка лайка
//        holder.likeButton.setChecked(project.isLiked());
//        holder.likeButton.setOnClickListener(v -> {
//            boolean newState = !project.isLiked();
//            project.setLiked(newState);
//            holder.likeButton.setChecked(newState);
//
//            // Обновление счетчика
//            int likes = project.getLikesCount() + (newState ? 1 : -1);
//            project.setLikesCount(likes);
//            holder.likesCount.setText(String.valueOf(likes));
//
//            // Анимация
//            animateButton(holder.likeButton);
//        });
//    }
//
//    private void animateButton(ImageButton button) {
//        AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(
//                context, R.animator.button_scale);
//        animator.setTarget(button);
//        animator.start();
//    }
//
//    @Override
//    public int getItemCount() {
//        return projects.size();
//    }
//
//    // ViewHolder класс
//    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
//        ImageView projectImage;
//        ImageButton favoriteButton;
//        TextView projectDescription;
//        TextView projectHashtags;
//        TextView projectAuthor;
//        ImageButton likeButton;
//        TextView likesCount;
//
//        public ProjectViewHolder(@NonNull View itemView) {
//            super(itemView);
//            projectImage = itemView.findViewById(R.id.projectImage);
//            favoriteButton = itemView.findViewById(R.id.favoriteButton);
//            projectDescription = itemView.findViewById(R.id.projectDescription);
//            projectHashtags = itemView.findViewById(R.id.projectHashtags);
//            projectAuthor = itemView.findViewById(R.id.projectAuthor);
//            likeButton = itemView.findViewById(R.id.likeButton);
//            likesCount = itemView.findViewById(R.id.likesCount);
//        }
//    }
//}