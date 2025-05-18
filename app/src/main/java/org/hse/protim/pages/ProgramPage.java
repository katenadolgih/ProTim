package org.hse.protim.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class ProgramPage extends BaseActivity {

    private ImageButton buttonBack;
    private TextView titleView;
    private RecyclerView recyclerModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_page);

        init();
        handle();

        titleView.setText(R.string.programm_page_title);

        // Пример данных
        List<Module> modules = new ArrayList<>();

        List<Lesson> lessons1 = new ArrayList<>();
        lessons1.add(new Lesson("Урок 1.", "Введение в проект", "Анна Петрова"));
        lessons1.add(new Lesson("Урок 2.", "Показ лучших проектов", "Леонид Шелковников"));

        List<Lesson> lessons2 = new ArrayList<>();
        lessons2.add(new Lesson("Урок 1.", "Создание эскиза", "Мария Иванова"));
        lessons2.add(new Lesson("Урок 2.", "Обсуждение идей", "Алексей Смирнов"));

        modules.add(new Module("Модуль 1: Знакомство", lessons1));
        modules.add(new Module("Модуль 2: Практика", lessons2));

        recyclerModules.setLayoutManager(new LinearLayoutManager(this));
        recyclerModules.setAdapter(new ModuleAdapter(modules));
    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        recyclerModules = findViewById(R.id.recyclerModules);
    }

    private void handle() {
        buttonBack.setOnClickListener(v -> onBackPressed());
    }

    // ===== МОДЕЛИ =====
    static class Lesson {
        String number;
        String title;
        String teacher;

        Lesson(String number, String title, String teacher) {
            this.number = number;
            this.title = title;
            this.teacher = teacher;
        }
    }

    static class Module {
        String title;
        List<Lesson> lessons;

        Module(String title, List<Lesson> lessons) {
            this.title = title;
            this.lessons = lessons;
        }
    }

    // ===== АДАПТЕРЫ =====
    static class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {
        private final List<Module> modules;

        ModuleAdapter(List<Module> modules) {
            this.modules = modules;
        }

        static class ModuleViewHolder extends RecyclerView.ViewHolder {
            TextView moduleTitle;
            RecyclerView lessonRecycler;

            ModuleViewHolder(View itemView) {
                super(itemView);
                moduleTitle = itemView.findViewById(R.id.moduleTitle);
                lessonRecycler = itemView.findViewById(R.id.lessonsRecycler);
            }
        }

        @NonNull
        @Override
        public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
            return new ModuleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
            Module module = modules.get(position);
            holder.moduleTitle.setText(module.title);
            holder.lessonRecycler.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.lessonRecycler.setAdapter(new LessonAdapter(module.lessons));
        }

        @Override
        public int getItemCount() {
            return modules.size();
        }
    }

    static class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {
        private final List<Lesson> lessons;

        LessonAdapter(List<Lesson> lessons) {
            this.lessons = lessons;
        }

        static class LessonViewHolder extends RecyclerView.ViewHolder {
            TextView titleAndNumber, teacher;

            LessonViewHolder(View itemView) {
                super(itemView);
                titleAndNumber = itemView.findViewById(R.id.lessonNumberAndTitle);
                teacher = itemView.findViewById(R.id.lessonTeacher);
            }
        }

        @NonNull
        @Override
        public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
            return new LessonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
            Lesson lesson = lessons.get(position);
            String fullText = lesson.number + " " + lesson.title;
            holder.titleAndNumber.setText(fullText);
            holder.teacher.setText(lesson.teacher);

            holder.itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, LessonPage.class);
                intent.putExtra("title", fullText);
                intent.putExtra("teacher", lesson.teacher);
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return lessons.size();
        }

    }
}
