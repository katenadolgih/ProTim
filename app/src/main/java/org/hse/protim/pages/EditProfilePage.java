package org.hse.protim.pages;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.hse.protim.DTO.profile.ProfileInfoDTO;
import org.hse.protim.DTO.profile.UploadProfileDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.file.FileClient;
import org.hse.protim.clients.retrofit.profile.ProfileClient;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;


public class EditProfilePage extends BaseActivity {
    private TextInputLayout etEducation, etContacts, etAbout;
    private TextInputEditText telegramInput, socialInput, aboutInput;
    private Button btnUploadResume, btnSave;
    private ImageButton buttonBack;
    private FlexboxLayout sectionContainer;
    private FlexboxLayout softwareContainer;
    private TextView titleView;
    private ProfileClient profileClient;
    private RetrofitProvider retrofitProvider;
    private List<String> selectedSections;
    private List<String> selectedSoftware;
    private LinearLayout educationContainer;
    private LinearLayout contactContainer;
    private String[] sections = {"ГП", "АС", "АР", "ВК", "ВС", "ЭО", "ГР", "КЖ", "КМ", "КД", "НВ", "НК", "НВК", "ОВ", "ТХ", "ЭС", "ЭН", "ЭМ"};
    private String[] software = {"Allplan", "Renga", "Revit", "Компас 3D", "BIM WIZARD", "ArchiCAD", "AutoCAD", "nanoCAD", "Pilot Ice"};
    private static final int REQUEST_CODE_PICK_RESUME = 1001;
    private Uri selectedResumeUri;
    private String selectedResumeName;
    private FileClient fileClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        init();
        handle();

        titleView.setText(R.string.edit_profile_page_title);


    }

    private void init() {
        retrofitProvider = new RetrofitProvider(this);
        profileClient = new ProfileClient(retrofitProvider);
        fileClient = new FileClient(retrofitProvider);

        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);

//        etEducation = findViewById(R.id.et_education);
//        etContacts = findViewById(R.id.et_contacts);
        etAbout = findViewById(R.id.et_about);

        btnUploadResume = findViewById(R.id.btn_upload_resume);
        btnSave = findViewById(R.id.save_button);
//        btnAddEducation = findViewById(R.id.btn_add_education);
//        btnAddContact = findViewById(R.id.btn_add_contact);

        sectionContainer = findViewById(R.id.section_container);
        softwareContainer = findViewById(R.id.software_container);

        selectedSections = new ArrayList<>();
        selectedSoftware = new ArrayList<>();

        telegramInput = findViewById(R.id.telegram_input);
        socialInput = findViewById(R.id.social_input);
        aboutInput = findViewById(R.id.about_input);

        educationContainer = findViewById(R.id.education_fields_container);
        contactContainer = findViewById(R.id.contact_fields_container);

        setFields();
    }

    private void populateEducationFields(List<String> educations) {
        educationContainer.removeAllViews();

        View firstItem = LayoutInflater.from(this)
                .inflate(R.layout.item_education_first, educationContainer, false);
        TextInputEditText firstInput = firstItem.findViewById(R.id.education_input);
        ImageButton addBtn = firstItem.findViewById(R.id.btn_add_education);

        firstInput.setText(!educations.isEmpty() ? educations.get(0) : "");
        educationContainer.addView(firstItem);

        addBtn.setOnClickListener(v -> {
            educationContainer.addView(createExtraEducationField(""));
        });

        for (int i = 1; i < educations.size(); i++) {
            educationContainer.addView(createExtraEducationField(educations.get(i)));
        }
    }

    private View createExtraEducationField(String text) {
        TextInputEditText input;
        View extra = LayoutInflater.from(this)
                .inflate(R.layout.item_education_extra, educationContainer, false);
        input = extra.findViewById(R.id.education_input);
        input.setText(text);
        return extra;
    }

    private List<String> collectEducationFields() {
        List<String> educations = new ArrayList<>();

        for (int i = 0; i < educationContainer.getChildCount(); i++) {
            View child = educationContainer.getChildAt(i);

            TextInputEditText input = child.findViewById(R.id.education_input);
            if (input != null) {
                String text = input.getText().toString().trim();
                if (!text.isEmpty()) {
                    educations.add(text);
                }
            }
        }
        return educations;
    }

    private List<String> collectSocialLinks() {
        List<String> socials = new ArrayList<>();
        LinearLayout contactContainer = findViewById(R.id.contact_fields_container);

        for (int i = 0; i < contactContainer.getChildCount(); i++) {
            View child = contactContainer.getChildAt(i);
            TextInputEditText input = child.findViewById(R.id.social_input);
            if (input != null) {
                String url = input.getText().toString().trim();
                if (!url.isEmpty()) {
                    socials.add(url);
                }
            }
        }
        return socials;
    }

    private void populateContactFields(List<String> socials) {
        contactContainer.removeAllViews();

        View first = LayoutInflater.from(this)
                .inflate(R.layout.item_social_first, contactContainer, false);
        TextInputEditText firstInput = first.findViewById(R.id.social_input);
        firstInput.setText(!socials.isEmpty() ? socials.get(0) : "");

        ImageButton addContactBtn = first.findViewById(R.id.btn_add_contact);
        contactContainer.addView(first);

        addContactBtn.setOnClickListener(v ->
                contactContainer.addView(createExtraSocialField(""))
        );

        for (int i=1; i<socials.size(); i++) {
            contactContainer.addView(createExtraSocialField(socials.get(i)));
        }
    }

    private View createExtraSocialField(String url) {
        View extra = LayoutInflater.from(this)
                .inflate(R.layout.item_social_extra, contactContainer, false);
        TextInputEditText input = extra.findViewById(R.id.social_input);
        input.setText(url);
        return extra;
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
        btnUploadResume.setOnClickListener(v -> openFilePicker());
        btnSave.setOnClickListener(v -> save());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");               // или "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(
                Intent.createChooser(intent, "Выберите файл резюме"),
                REQUEST_CODE_PICK_RESUME
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_RESUME
                && resultCode == RESULT_OK
                && data != null) {

            selectedResumeUri = data.getData();
            selectedResumeName = queryName(getContentResolver(), selectedResumeUri);
            // 1) Отобразить имя пользователю:
            btnUploadResume.setText(selectedResumeName);
            // 2) Отправить имя на сервер:
//            sendNameToServer(selectedResumeName);
        }
    }

    /** Читает DisplayName из Cursor для content:// Uri */
    public static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor = resolver.query(uri, null, null, null, null);
        try {
            if (returnCursor != null && returnCursor.moveToFirst()) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                return returnCursor.getString(nameIndex);
            }
        } finally {
            if (returnCursor != null) returnCursor.close();
        }
        return null;
    }

    private void setFields() {
        profileClient.getProfileInfo(new ProfileClient.ProfileInfoCallback() {
            @Override
            public void onSuccess(ProfileInfoDTO profileInfoDTO) {
                selectedSections = profileInfoDTO.sectionAndStamps();
                selectedSoftware = profileInfoDTO.softwareSkills();
                addTags(sectionContainer, sections, selectedSections);
                addTags(softwareContainer, software, selectedSoftware);
                populateEducationFields(profileInfoDTO.education());
                telegramInput.setText(profileInfoDTO.telegram());
                populateContactFields(profileInfoDTO.socialNetworks());
//                socialInput.setText(String.join(" ", profileInfoDTO.socialNetworks()));
                aboutInput.setText(profileInfoDTO.about());
            }
            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(EditProfilePage.this, message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void addTags(FlexboxLayout container, String[] tags, List<String> selectedList) {
        for (String tag : tags) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_filter_tag, container, false);
            TextView text = view.findViewById(R.id.tag_text);
            ImageView close = view.findViewById(R.id.tag_close);

            text.setText(tag);

            boolean isSelectedView = selectedList.contains(text.getText().toString());
            view.setSelected(isSelectedView);
            text.setTextColor(ContextCompat.getColor(this, isSelectedView ? R.color.white : R.color.active_field));


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

    private void save() {
        String randomUUID = UUID.randomUUID().toString();
        List<String> education = collectEducationFields();
        List<String> socialLinks = collectSocialLinks();

        if (!btnUploadResume.getText().toString().equals("Загрузить файл-резюме")) {
            fileClient.getPutLink(selectedResumeName+randomUUID, new FileClient.PutLinkCallback() {
                @Override
                public void onSuccess(String link) {
                    uploadWithOkHttp(selectedResumeUri, link, randomUUID);
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> Toast.makeText(EditProfilePage.this, message,
                            Toast.LENGTH_LONG).show());
                }
            });
        }

        profileClient.uploadProfile(new UploadProfileDTO(selectedSections, selectedSoftware,
                education, socialLinks, aboutInput.getText().toString()), new ProfileClient.UploadProfileCallback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(EditProfilePage.this, ProfilePage.class);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(EditProfilePage.this, message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void uploadWithOkHttp(Uri fileUri, String uploadUrl, String randomUUID) {
        String mimeType = getMimeType(fileUri);
        InputStream in = null;

        try {
            in = getContentResolver().openInputStream(fileUri);
            if (in == null) throw new IOException("Cannot open URI");

            // Читаем все данные в буфер (подходит для небольших файлов)
            byte[] data = readFully(in);

            RequestBody body = RequestBody.create(data, MediaType.parse(mimeType));

            Request request = new Request.Builder()
                    .url(uploadUrl)
                    .put(body)
                    .build();

            InputStream finalIn = in;
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    showToast("Ошибка загрузки: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    if (!response.isSuccessful()) {
                        showToast(response.message());
                    } else {
                        fileClient.putLinkSave(selectedResumeName+randomUUID, "resume", new FileClient.PutLinkSaveCallback() {
                            @Override
                            public void onSuccess() {

                            }
                            @Override
                            public void onError(String message) {
                                runOnUiThread(() -> Toast.makeText(EditProfilePage.this,
                                        message,
                                        Toast.LENGTH_LONG).show());
                            }
                        });
                    }
                    closeStream(finalIn); // Закрываем поток после завершения
                }
            });

        } catch (IOException e) {
            closeStream(in);
            showToast("Не удалось прочитать файл");
        }
    }

    // Метод для чтения всего потока в байтовый массив
    private byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] tmp = new byte[8192];
        int read;
        while ((read = in.read(tmp)) != -1) {
            buffer.write(tmp, 0, read);
        }
        return buffer.toByteArray();
    }

    // Метод для безопасного закрытия потока
    private void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignored) {}
        }
    }

    // Вспомогательный метод для показа уведомлений
    private void showToast(String message) {
        runOnUiThread(() ->
                Toast.makeText(EditProfilePage.this, message, Toast.LENGTH_SHORT).show()
        );
    }

    private String getMimeType(Uri uri) {
        ContentResolver cr = getContentResolver();
        String mime = cr.getType(uri);
        // на случай, если ContentResolver не знает тип, ставим общий
        return mime != null ? mime : "application/octet-stream";
    }
}
