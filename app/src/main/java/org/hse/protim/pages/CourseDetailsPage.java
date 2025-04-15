package org.hse.protim.pages;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.hse.protim.R;

public class CourseDetailsPage extends AppCompatActivity {

    // Объявляем переменные
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details_page);

        // Инициализация переменных
        init();

        // Установка обработчиков
        handle();

        // Установка отступов для системы
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.course_details_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Метод для инициализации переменных
    private void init() {
        buttonBack = findViewById(R.id.button_back); // Находим кнопку "Назад"
    }

    // Метод для обработки нажатий
    private void handle() {
        // Обработка клика по кнопке "Назад"
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed()); // Закрытие текущей активности
        }
    }
}
