package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class SelectionDetailsPage extends BaseActivity {
    private ImageButton buttonBack;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selection_details_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.selection_details_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        handle();

        titleView.setText(R.string.selection_title);
        findViewById(R.id.more_selection).setOnClickListener(this::showPopupMenuSelection);

        RecyclerView recyclerNewProjects = findViewById(R.id.favoriteProjectsRecycler);
        recyclerNewProjects.setLayoutManager(new LinearLayoutManager(this));

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> projectViews = new ArrayList<>();

        View project1 = inflater.inflate(R.layout.item_project, recyclerNewProjects, false);
        ((ImageView) project1.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project1.findViewById(R.id.projectDescription)).setText("Популярный проект A — исследование ИИ");
        ((TextView) project1.findViewById(R.id.projectHashtags)).setText("#AI #Research #Tech");
        ((TextView) project1.findViewById(R.id.projectAuthor)).setText("Смирнов Алексей");
        ((TextView) project1.findViewById(R.id.likesCount)).setText("105");

        View project2 = inflater.inflate(R.layout.item_project, recyclerNewProjects, false);
        ((ImageView) project2.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project2.findViewById(R.id.projectDescription)).setText("Популярный проект B — мобильное приложение для экологии");
        ((TextView) project2.findViewById(R.id.projectHashtags)).setText("#Eco #Mobile #UX");
        ((TextView) project2.findViewById(R.id.projectAuthor)).setText("Кузнецова Ирина");
        ((TextView) project2.findViewById(R.id.likesCount)).setText("321");

        View project3 = inflater.inflate(R.layout.item_project, recyclerNewProjects, false);
        ((ImageView) project3.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project3.findViewById(R.id.projectDescription)).setText("Популярный проект C — цифровая платформа образования");
        ((TextView) project3.findViewById(R.id.projectHashtags)).setText("#EdTech #Platform #Java");
        ((TextView) project3.findViewById(R.id.projectAuthor)).setText("Иванова Мария");
        ((TextView) project3.findViewById(R.id.likesCount)).setText("678");

        projectViews.add(project1);
        projectViews.add(project2);
        projectViews.add(project3);

        recyclerNewProjects.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(projectViews.get(viewType)) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

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

    public void showPopupMenuSelection(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.selection_popup_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_edit) {
                startActivity(new Intent(SelectionDetailsPage.this, SelectionEditingPage.class));
                return true;
            } else if (id == R.id.action_logout) {
                showSettingsDialog(); // вызов попапа
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
    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Удалить подборку?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    // Заглушка удаления
                    Intent intent = new Intent(SelectionDetailsPage.this, SelectionPage.class);
                    startActivity(intent);
                    finish(); // Закрываем текущую страницу
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
//        settingsButton = findViewById(R.id.settings_button);

    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
//        settingsButton.setOnClickListener(v -> {
//            Intent intent = new Intent(SelectionDetailsPage.this, FiltersPage.class);
//            startActivity(intent);
//        });
    }
}