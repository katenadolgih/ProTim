package org.hse.protim.pages;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.hse.protim.R;

public class EditProfilePage extends BaseActivity {

    private TextInputLayout etEducation, etContacts, etAbout;

    private Button btnUploadResume, btnSave;
    private ImageButton btnAddEducation, btnAddContact, buttonBack;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        init();
        handle();

        titleView.setText(R.string.edit_profile_page_title);

        btnUploadResume.setOnClickListener(v -> {
            // TODO: загрузка файла резюме
        });

//        btnAddEducation.setOnClickListener(v -> {
//            String education = etEducation.getText().toString();
//            if (!education.isEmpty()) {
//                // TODO: добавить в список или в базу
//            }
//        });
//
//        btnAddContact.setOnClickListener(v -> {
//            String contact = etContacts.getText().toString();
//            if (!contact.isEmpty()) {
//                // TODO: добавить контакт
//            }
//        });

        btnSave.setOnClickListener(v -> {
            // TODO: сохранение всех данных профиля
        });
    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);

        etEducation = findViewById(R.id.et_education);
        etContacts = findViewById(R.id.et_contacts);
        etAbout = findViewById(R.id.et_about);

        btnUploadResume = findViewById(R.id.btn_upload_resume);
        btnSave = findViewById(R.id.save_button);
        btnAddEducation = findViewById(R.id.btn_add_education);
        btnAddContact = findViewById(R.id.btn_add_contact);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
    }
}
