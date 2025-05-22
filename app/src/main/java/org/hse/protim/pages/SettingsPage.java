package org.hse.protim.pages;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.hse.protim.DTO.profile.ProfileSettingInfoDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.file.FileClient;
import org.hse.protim.clients.retrofit.profile.ProfileClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingsPage extends BaseActivity {
    private CheckBox cbJobSearch, cbInternshipSearch, cbHideBirthDate, cbPublicProfile;
    private Button applyButton;
    private ImageButton buttonBack;
    private TextView titleView;
    private TextInputEditText surname, name, city, birthday, email;
    private Spinner gender;
    private RetrofitProvider retrofitProvider;
    private ProfileClient profileClient;
    private ImageView photoIconMain;
    private ActivityResultLauncher<Intent> chooserLauncher;
    private Uri tempCameraUri;
    private Uri selectedPhotoUri;
    private FileClient fileClient;
    private boolean photoSent = false;
    private boolean textFieldSent = false;
    private volatile boolean wasIntent = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        init();
        setupChooserLauncher();
        handle();
        loadData();

        titleView.setText(R.string.settings_page_title);
    }
    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        applyButton = findViewById(R.id.apply_button);
        cbJobSearch = findViewById(R.id.checkbox_job_search);
        cbInternshipSearch = findViewById(R.id.checkbox_internship);
        cbHideBirthDate = findViewById(R.id.checkbox_hide_birthdate);
        cbPublicProfile = findViewById(R.id.checkbox_public_profile);
        surname = findViewById(R.id.surname_setting);
        name = findViewById(R.id.name_setting);
        city = findViewById(R.id.city_setting);
        birthday = findViewById(R.id.birthday_date_setting);
        email = findViewById(R.id.email_setting);
        gender = findViewById(R.id.spinner_gender);
        retrofitProvider = new RetrofitProvider(SettingsPage.this);
        profileClient = new ProfileClient(retrofitProvider);
        photoIconMain = findViewById(R.id.avatarImage);
        fileClient = new FileClient(retrofitProvider);
    }

    private void setupChooserLauncher() {
        chooserLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedPhotoUri = data.getData();
                        } else if (tempCameraUri != null) {
                            selectedPhotoUri = tempCameraUri;
                        }
                        if (selectedPhotoUri != null) {
                            photoIconMain.setImageURI(selectedPhotoUri);
                        }
                    }
                }
        );
    }

    private void photoIconMainHandle() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createTempImageFile();
        tempCameraUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider",
                photoFile
        );
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempCameraUri);

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        Intent chooser = Intent.createChooser(cameraIntent, "Выберите фото");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{ galleryIntent });

        chooserLauncher.launch(chooser);
    }

    private File createTempImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(
                    "JPEG_" + timeStamp + "_",
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            throw new RuntimeException("Не создался файл для камеры", e);
        }
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }

        applyButton.setOnClickListener(v -> applyButtonHandle());

        photoIconMain.setOnClickListener(v -> photoIconMainHandle());
        checkElementsHandle();
    }

    private void checkElementsHandle() {
        cbJobSearch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbInternshipSearch.setChecked(false);
            }
        });

        cbInternshipSearch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbJobSearch.setChecked(false);
            }
        });
    }
    private void loadData() {
        profileClient.getProfileSettingInfo(new ProfileClient.ProfileSettingInfoCallback() {
            @Override
            public void onSuccess(ProfileSettingInfoDTO profileSettingInfoDTO) {
                String surnameGet = profileSettingInfoDTO != null ? profileSettingInfoDTO.surname() : null;
                String nameGet = profileSettingInfoDTO != null ? profileSettingInfoDTO.name() : null;
                String cityGet = profileSettingInfoDTO != null ? profileSettingInfoDTO.city() : null;
                String birthDayGet = profileSettingInfoDTO != null ? profileSettingInfoDTO.birthday() : null;
                String emailGet = profileSettingInfoDTO != null ? profileSettingInfoDTO.email() : null;
                Boolean cbHideBirthDateGet = profileSettingInfoDTO != null ? profileSettingInfoDTO.isHideBirthday() : null;
                Boolean cbPublicProfileGet = profileSettingInfoDTO != null ? profileSettingInfoDTO.isPublic() : null;
                String statusGet = profileSettingInfoDTO != null ? profileSettingInfoDTO.status() : null;
                String photoPath = profileSettingInfoDTO != null ? profileSettingInfoDTO.photoPath() : null;
                String genderGet = profileSettingInfoDTO != null ? profileSettingInfoDTO.gender() : null;


                surname.setText(surnameGet == null ? "" : surnameGet);
                name.setText(nameGet == null ? "" : nameGet);
                city.setText(cityGet == null ? "" : cityGet);
                birthday.setText(birthDayGet == null ? "" : birthDayGet);
                email.setText(emailGet == null ? "" : emailGet);
                cbHideBirthDate.setChecked(cbHideBirthDateGet != null && cbHideBirthDateGet);
                cbPublicProfile.setChecked(cbPublicProfileGet != null && cbPublicProfileGet);
                if (statusGet != null) {
                    cbJobSearch.setChecked(Objects.equals(profileSettingInfoDTO.status(), "Ищу работу"));
                    cbInternshipSearch.setChecked(Objects.equals(profileSettingInfoDTO.status(), "Ищу стажировку"));
                }
                if (genderGet != null) {
                    gender.setSelection(genderGet.equals("Мужской") ? 0 : 1);
                }
                if (photoIconMain != null) {
                    Glide.with(photoIconMain.getContext()).clear(photoIconMain);
                    Glide.with(photoIconMain.getContext())
                            .load(photoPath)
                            .into(photoIconMain);
                }
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SettingsPage.this, message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void applyButtonHandle() {
        photoSent = false;
        textFieldSent = false;

        sendPhoto();
        sendTextFields();
    }

    private void sendTextFields() {
        String genderString = gender.getSelectedItemPosition() == 0 ? "Мужской" : "Женский";
        String status = null;
        if (cbJobSearch.isChecked()) {
            status = "Ищу работу";
        }
        if (cbInternshipSearch.isChecked()) {
            status = "Ищу стажировку";
        }
        Boolean birthDateHide = cbHideBirthDate.isChecked();
        Boolean isPublicProfile = cbPublicProfile.isChecked();

        ProfileSettingInfoDTO profileSettingInfoDTO = new ProfileSettingInfoDTO(
                null, surname.getText().toString(), name.getText().toString(),
                genderString, city.getText().toString(), birthday.getText().toString(),
                email.getText().toString(), status, birthDateHide, isPublicProfile
        );

        profileClient.uploadProfileSetting(profileSettingInfoDTO, new ProfileClient.UploadProfileCallback() {
            @Override
            public void onSuccess() {
                textFieldSent = true;
                checkAndProceed();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(SettingsPage.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void sendPhoto() {
        if (selectedPhotoUri != null && !selectedPhotoUri.toString().isEmpty()) {
            String randomUUID = UUID.randomUUID().toString();
            String fileName = selectedPhotoUri.toString();
            String toSend = fileName + randomUUID;
            fileClient.getPutLink(toSend, new FileClient.PutLinkCallback() {
                @Override
                public void onSuccess(String link) {
                    try (
                            InputStream inputStream = getContentResolver().openInputStream(selectedPhotoUri);
                            OutputStream out = new FileOutputStream(
                                    new File(getCacheDir(), "upload_image.jpg"))
                    ) {
                        if (inputStream == null) {
                            throw new IOException("Не удалось открыть InputStream для URI");
                        }
                        byte[] buf = new byte[8192];
                        int len;
                        while ((len = inputStream.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        RequestBody body = RequestBody.create(
                                MediaType.parse("image/jpeg"),
                                new File(getCacheDir(), "upload_image.jpg")
                        );

                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(link)
                                .put(body)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                fileClient.putLinkSave(toSend, "photo", new FileClient.PutLinkSaveCallback() {
                                    @Override
                                    public void onSuccess() {
                                        photoSent = true;
                                        checkAndProceed();
                                    }
                                    @Override
                                    public void onError(String message) {
                                        runOnUiThread(() ->
                                                Toast.makeText(SettingsPage.this, message, Toast.LENGTH_LONG).show()
                                        );
                                    }
                                });
                            }
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(() ->
                                        Toast.makeText(SettingsPage.this, e.getMessage(), Toast.LENGTH_LONG).show()
                                );
                            }
                        });

                    } catch (IOException e) {
                        runOnUiThread(() ->
                                Toast.makeText(SettingsPage.this,
                                        "Ошибка при чтении или записи файла: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show()
                        );
                    }
                }
                @Override
                public void onError(String message) {
                    runOnUiThread(() ->
                            Toast.makeText(SettingsPage.this, message, Toast.LENGTH_LONG).show()
                    );
                }
            });
        }
    }
    private synchronized void checkAndProceed() {
        if (photoSent && textFieldSent && !wasIntent) {
            wasIntent = true;
            Intent intent = new Intent(SettingsPage.this, ProfilePage.class);
            startActivity(intent);
        }
    }

}
