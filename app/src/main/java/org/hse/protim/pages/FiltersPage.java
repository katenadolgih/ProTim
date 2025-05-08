package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.flexbox.FlexboxLayout;

import org.hse.protim.R;

import java.util.ArrayList;

public class FiltersPage extends BaseActivity {

    private TextView titleView;
    private String[] sections = {"ГП", "АС", "АР", "ВК", "ВС", "ЭО", "ГР", "КЖ", "КМ", "КД", "НВ", "НК", "НВК", "ОВ", "ТХ", "ЭС", "ЭН", "ЭМ"};
    private String[] software = {"Allplan", "Autocad", "ArchiCad", "Pilot", "BIM Wizard", "NanoCad", "Pilot", "BIM Wizard", "NanoCad", "Pilot", "BIM Wizard", "NanoCad"};

    private ArrayList<String> selectedSections = new ArrayList<>();
    private ArrayList<String> selectedSoftware = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters_page);

        FlexboxLayout sectionContainer = findViewById(R.id.section_container);
        FlexboxLayout softwareContainer = findViewById(R.id.software_container);
        Button applyButton = findViewById(R.id.apply_button);
        titleView = findViewById(R.id.title_text);


        addTags(sectionContainer, sections, selectedSections);
        addTags(softwareContainer, software, selectedSoftware);

        applyButton.setOnClickListener(v -> {
            Intent intent = new Intent(FiltersPage.this, SearchPage.class);
            intent.putStringArrayListExtra("selected_sections", selectedSections);
            intent.putStringArrayListExtra("selected_software", selectedSoftware);
            startActivity(intent);
        });

        titleView.setText(R.string.filters_page_title);
        ImageView backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> onBackPressed());

    }

    private void addTags(FlexboxLayout container, String[] tags, ArrayList<String> selectedList) {
        for (String tag : tags) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_filter_tag, container, false);
            TextView text = view.findViewById(R.id.tag_text);
            ImageView close = view.findViewById(R.id.tag_close);

            text.setText(tag);
            view.setSelected(false);

            view.setOnClickListener(v -> {
                boolean isSelected = v.isSelected();
                v.setSelected(!isSelected);

                close.setVisibility(!isSelected ? View.VISIBLE : View.GONE);
                text.setTextColor(ContextCompat.getColor(this, !isSelected ? R.color.white : R.color.active_field));

                if (!isSelected) {
                    selectedList.add(tag);
                } else {
                    selectedList.remove(tag);
                }
            });

            close.setOnClickListener(v -> {
                view.setSelected(false);
                close.setVisibility(View.GONE);
                text.setTextColor(ContextCompat.getColor(this, R.color.active_field));
                selectedList.remove(tag);
            });

            container.addView(view);
        }
    }
}
