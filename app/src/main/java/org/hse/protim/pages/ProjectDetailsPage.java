package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import org.hse.protim.DTO.content.ProjectContentDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.content.ContentClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.getstream.photoview.PhotoView;

public class ProjectDetailsPage extends AppCompatActivity {
    private TextView author;
    private ImageButton share;
    private String fullName;
    private Long projectId;
    private String projectName;
    private LinearLayout contentContainer;  // ← наш новый контейнер
    private ContentClient contentClient;
    private RetrofitProvider retrofitProvider;
    private List<ProjectContentDTO> projectContentDTOS;
    private TextView titleText;
    private ImageButton buttonBack;
//    private TextView projectNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_page);

        init();
        authorHandle();
        shareHandle();
        buttonBackHandle();

        loadAndDisplayProjectContents();
    }

    private void init() {
        Intent intent = getIntent();
        retrofitProvider = new RetrofitProvider(ProjectDetailsPage.this);
        contentClient = new ContentClient(retrofitProvider);
        projectContentDTOS = new ArrayList<>();

        fullName = intent.getStringExtra("author");
        projectName = intent.getStringExtra("project_name");
        projectId = intent.getLongExtra("project_id", 0);

        author = findViewById(R.id.project_author);
        author.setText("Автор: " + fullName);

        share = findViewById(R.id.shareButton);
        contentContainer = findViewById(R.id.content_container);
        titleText = findViewById(R.id.title_text);
        titleText.setText("Проект");

        buttonBack = findViewById(R.id.button_back);

//        projectNameText = findViewById(R.id.project_name_text);
//        projectNameText.setText(projectName);
    }

    private void authorHandle() {
        author.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfilePage.class);
            intent.putExtra("authorId", "some_id");
            startActivity(intent);
        });
    }

    private void shareHandle() {
        share.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Смотри проект в приложении Протим!");
            startActivity(Intent.createChooser(intent, "Поделиться проектом"));
        });
    }

    private void buttonBackHandle() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void displayContent() {
        for (ProjectContentDTO dto : projectContentDTOS) {
            if ("text".equalsIgnoreCase(dto.contentType()) && dto.content() != null) {
                TextView name = new TextView(this);
                name.setText(projectName);
                name.setTextSize(16);
                name.setPadding(0, 12, 0, 12);
                contentContainer.addView(name);

                TextView tv = new TextView(this);
                tv.setText(dto.content());
                tv.setTextSize(16);
                tv.setPadding(0, 12, 0, 12);
                contentContainer.addView(tv);
            } else if ("photo".equalsIgnoreCase(dto.contentType()) && dto.contentUrl() != null) {
                PhotoView photoView = new PhotoView(this);
                photoView.setAdjustViewBounds(true);
                photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(this)
                        .load(dto.contentUrl())
                        .placeholder(R.drawable.ic_photo)
                        .error(R.drawable.ic_photo)
                        .into(photoView);

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                params.topMargin = 12;

                contentContainer.addView(photoView, params);

                photoView.setOnClickListener(v -> showPhotoZoomDialog(dto.contentUrl()));
            }
        }
    }

    private void loadAndDisplayProjectContents() {
        contentClient.getProjectContent(projectId, new ContentClient.ProjectContentCallback() {
            @Override
            public void onSuccess(List<ProjectContentDTO> retProjectContent) {
                if (retProjectContent != null) {
                    projectContentDTOS.addAll(retProjectContent);
                    displayContent();
                }
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(ProjectDetailsPage.this, message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void showPhotoZoomDialog(String imageUrl) {
        View view = getLayoutInflater().inflate(R.layout.dialog_photo_zoom, null);
        PhotoView pv = view.findViewById(R.id.photoView);

        Glide.with(this)
                .load(imageUrl)
                .into(pv);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        pv.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialog.show();
    }
}