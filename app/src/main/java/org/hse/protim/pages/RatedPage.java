package org.hse.protim.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.hse.protim.DTO.project.RetLikesDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.projects.ProjectClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RatedPage extends AppCompatActivity {

    private ImageButton buttonBack;
    private TextView titleView;
    private RecyclerView ratedUsersRecycler;
    private ProjectClient projectClient; // Ваш клиент для работы с API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rated_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rated_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        projectClient = new ProjectClient(new RetrofitProvider(this));

        initViews();
        setupRecyclerView();
        loadDataFromClient();
    }

    private void initViews() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        ratedUsersRecycler = findViewById(R.id.ratedUsersRecycler);

        buttonBack.setOnClickListener(v -> finish());
        titleView.setText(R.string.rated_page_title);
    }

    private void setupRecyclerView() {
        ratedUsersRecycler.setLayoutManager(new LinearLayoutManager(this));
        ratedUsersRecycler.setAdapter(new RatedUsersAdapter());
    }

    private void loadDataFromClient() {
        Long projectId = getIntent().getLongExtra("PROJECT_ID", -1);

        projectClient.getLikes(projectId, new ProjectClient.ProjectLikesCallback() {
            @Override
            public void onSuccess(List<RetLikesDTO> users) {
                ((RatedUsersAdapter) ratedUsersRecycler.getAdapter()).setUsers(users);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(RatedPage.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Адаптер для RecyclerView
    private static class RatedUsersAdapter extends RecyclerView.Adapter<RatedUsersAdapter.ViewHolder> {
        private List<RetLikesDTO> users = new ArrayList<>();

        void setUsers(List<RetLikesDTO> users) {
            this.users = users;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rated_user, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RetLikesDTO user = users.get(position);

            holder.userName.setText(user.fullName());

            Glide.with(holder.itemView.getContext())
                    .load(user.photoPath())
                    .circleCrop()
                    .into(holder.userPhoto);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView userPhoto;
            TextView userName;

            ViewHolder(View itemView) {
                super(itemView);
                userPhoto = itemView.findViewById(R.id.userPhoto);
                userName = itemView.findViewById(R.id.userName);
            }
        }
    }

}
