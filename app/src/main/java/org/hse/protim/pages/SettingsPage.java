package org.hse.protim.pages;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.hse.protim.R;

public class SettingsPage extends BaseActivity {

    private CheckBox cbJobSearch, cbInternshipSearch, cbHideBirthDate, cbPublicProfile;
    private Button applyButton;
    private ImageButton buttonBack;
    private TextView titleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        init();
        handle();
        loadPreferences();

        titleView.setText(R.string.settings_page_title);

    }

    private void savePreferences() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("job_search", cbJobSearch.isChecked());
        editor.putBoolean("internship_search", cbInternshipSearch.isChecked());
        editor.putBoolean("hide_birthdate", cbHideBirthDate.isChecked());
        editor.putBoolean("public_profile", cbPublicProfile.isChecked());
        editor.apply();
    }

    private void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        cbJobSearch.setChecked(prefs.getBoolean("job_search", false));
        cbInternshipSearch.setChecked(prefs.getBoolean("internship_search", false));
        cbHideBirthDate.setChecked(prefs.getBoolean("hide_birthdate", false));
        cbPublicProfile.setChecked(prefs.getBoolean("public_profile", false));
    }
    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        applyButton = findViewById(R.id.apply_button);
        cbJobSearch = findViewById(R.id.checkbox_job_search);
        cbInternshipSearch = findViewById(R.id.checkbox_internship);
        cbHideBirthDate = findViewById(R.id.checkbox_hide_birthdate);
        cbPublicProfile = findViewById(R.id.checkbox_public_profile);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }

        applyButton.setOnClickListener(v -> {
            savePreferences();
            Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
            onBackPressed();
        });
    }
}
