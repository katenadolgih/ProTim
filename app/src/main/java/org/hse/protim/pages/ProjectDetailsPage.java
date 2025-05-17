package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.hse.protim.R;

public class ProjectDetailsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_page);
        TextView author = findViewById(R.id.project_author);
        author.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfilePage.class);
            intent.putExtra("authorId", "some_id");
            startActivity(intent);
        });

        ImageButton share = findViewById(R.id.shareButton);
        share.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Смотри проект в приложении Протим!");
            startActivity(Intent.createChooser(intent, "Поделиться проектом"));
        });
    }
}