package org.hse.protim.pages;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class SelectionsAllPage extends BaseActivity {

    private ImageButton buttonBack;
    private TextView titleView;
    private LinearLayout selectionLayout;
    private RecyclerView selectionsRecycler;
    private List<Selection> selections = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selections_all_page);


        init();
        handle();
        loadSampleData();
        setupAdapters();

        titleView.setText(R.string.selections_all_title);
    }


    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        selectionLayout = findViewById(R.id.selection);
        selectionsRecycler = findViewById(R.id.selectionsRecycler);

    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
    }

    private void loadSampleData() {
        selections.add(new Selection("Моя подборка 1", "Описание подборки"));
        selections.add(new Selection("Подборка для вдохновения", "Описание подборки"));
        selections.add(new Selection("Моя подборка 1", "Описание подборки"));
        selections.add(new Selection("Подборка для вдохновения", "Описание подборки"));
        selections.add(new Selection("Моя подборка 1", "Описание подборки"));
        selections.add(new Selection("Подборка для вдохновения", "Описание подборки"));

    }

    private void setupAdapters() {
        // Список подборок
        selectionsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        selectionsRecycler.setAdapter(new SelectionAdapter(selections, false));

    }
}