package org.hse.protim.pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.hse.protim.R;
import org.hse.protim.clients.retrofit.auth.RegisterClient;
import org.hse.protim.clients.retrofit.auth.RegistrationCallback;
import org.hse.protim.utils.ValidationUtils;

public class RegisterPage extends BaseActivity {

    private TextInputLayout textInputLayoutSurname;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutPhone;
    private TextInputLayout textInputLayoutEmail;
    private Button registerButton;
    private ImageButton backButton;
    private TextView termsText;
    private TextView loginLink;
    private RegisterClient registerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Инициализация элементов
        textInputLayoutSurname = findViewById(R.id.textInputLayoutSurname);
        textInputLayoutSurname.setErrorIconDrawable(null);

        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutName.setErrorIconDrawable(null);

        textInputLayoutPhone = findViewById(R.id.textInputLayoutPhone);
        textInputLayoutPhone.setErrorIconDrawable(null);

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutEmail.setErrorIconDrawable(null);

        registerButton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.button_back);
        termsText = findViewById(R.id.termsAndConditionsText);
        loginLink = findViewById(R.id.loginTextView);

        registerClient = new RegisterClient(RegisterPage.this);
        // Make terms text clickable
        setupTermsText();

        // Setup login link
        setupLoginLink();

        // Обработка нажатия на кнопку "Назад"
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Обработка нажатия на кнопку "Зарегистрироваться"
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
    }

    private void setupTermsText() {
        String fullText = getString(R.string.terms_and_conditions);
        SpannableString spannableString = new SpannableString(fullText);

        // Тексты для кликабельных ссылок (должны точно соответствовать тексту в strings.xml)
        String publicOfferText = "Публичной офертой";
        String privacyPolicyText = "Политикой обработки персональных данных";

        // Находим позиции текста в строке
        int publicOfferStart = fullText.indexOf(publicOfferText);
        int privacyPolicyStart = fullText.indexOf(privacyPolicyText);

        // Проверяем, что текст найден (indexOf не вернул -1)
        if (publicOfferStart >= 0) {
            int publicOfferEnd = publicOfferStart + publicOfferText.length();

            ClickableSpan publicOfferSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    openPublicOffer();
                }
            };
            spannableString.setSpan(publicOfferSpan, publicOfferStart, publicOfferEnd,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (privacyPolicyStart >= 0) {
            int privacyPolicyEnd = privacyPolicyStart + privacyPolicyText.length();

            ClickableSpan privacyPolicySpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    openPrivacyPolicy();
                }
            };
            spannableString.setSpan(privacyPolicySpan, privacyPolicyStart, privacyPolicyEnd,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        termsText.setText(spannableString);
        termsText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setupLoginLink() {
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на экран входа (LoginPage)
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
                finish(); // Закрываем текущую активность, чтобы нельзя было вернуться назад
            }
        });
    }

    private void openPublicOffer() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://протим.рф/oferta.pdf"));
        startActivity(browserIntent);
    }

    private void openPrivacyPolicy() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://протим.рф/policy.pdf"));
        startActivity(browserIntent);
    }

    // Метод для проверки полей
    private void validateFields() {
        boolean isSurnameValid = ValidationUtils.validateCyrillicName(
                textInputLayoutSurname,
                "Фамилия должна состоять только из кириллицы"
        );

        boolean isNameValid = ValidationUtils.validateCyrillicName(
                textInputLayoutName,
                "Имя должно состоять только из кириллицы"
        );

        boolean isPhoneValid = ValidationUtils.validateField(
                textInputLayoutPhone,
                "Поле не может быть пустым"
        );

        // Проверка email (должен содержать символ "@")
        boolean isEmailValid = ValidationUtils.validateEmail(
                textInputLayoutEmail,
                "Email должен содержать символ @"
        );

        // Если все поля прошли валидацию, можно продолжить регистрацию
        if (isSurnameValid && isNameValid && isPhoneValid && isEmailValid) {
            registerUser();
        }
    }

    // Метод для регистрации пользователя
    private void registerUser() {
        String surname = textInputLayoutSurname.getEditText().getText().toString();
        String name = textInputLayoutName.getEditText().getText().toString();
        String phoneNumber = textInputLayoutPhone.getEditText().getText().toString();
        String email = textInputLayoutEmail.getEditText().getText().toString();


        registerClient.register(surname, name, phoneNumber, email, new RegistrationCallback() {
            @Override
            public void onSuccess(String email) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterPage.this,
                            "Пароль отправлен на " + email, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(RegisterPage.this, error, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

}