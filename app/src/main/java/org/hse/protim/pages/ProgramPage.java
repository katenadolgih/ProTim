package org.hse.protim.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.hse.protim.DTO.courses.CourseProgramDTO;
import org.hse.protim.DTO.lesson.LessonPreviewDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.courses.CourseClient;
import org.hse.protim.clients.retrofit.lesson.LessonClient;

import java.util.Collections;
import java.util.List;

public class ProgramPage extends BaseActivity {

    private ImageButton buttonBack;
    private TextView titleView;
    private RecyclerView recyclerModules;
    private RecyclerView recyclerRecentlyModules;
    private Long courseId;
    private RetrofitProvider retrofitProvider;
    private CourseClient courseClient;
    private static LessonClient lessonClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_page);

        init();
        handle();

        titleView.setText(R.string.programm_page_title);

        setRecyclerModules();
        setRecyclerRecentlyModules();
    }

    private void setRecyclerRecentlyModules() {
        courseClient.getLastSeenProgram(courseId, new CourseClient.LastSeenProgramCallback() {
            @Override
            public void onSuccess(CourseProgramDTO courseProgramDTO) {
                recyclerRecentlyModules.setLayoutManager(new LinearLayoutManager(ProgramPage.this));
                recyclerRecentlyModules.setAdapter(new ModuleAdapter(Collections.singletonList(courseProgramDTO)));
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ProgramPage.this, message, Toast.LENGTH_LONG)
                        .show());
            }
        });
    }

    private void setRecyclerModules() {
        courseClient.getCourseProgram(courseId, new CourseClient.CourseProgramCallback() {
            @Override
            public void onSuccess(List<CourseProgramDTO> courseProgramDTOS) {
                recyclerModules.setLayoutManager(new LinearLayoutManager(ProgramPage.this));
                recyclerModules.setAdapter(new ModuleAdapter(courseProgramDTOS));
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ProgramPage.this, message, Toast.LENGTH_LONG)
                        .show());
            }
        });
    }
    private void init() {
        buttonBack = findViewById(R.id.button_back);
        titleView = findViewById(R.id.title_text);
        recyclerModules = findViewById(R.id.recyclerModules);
        recyclerRecentlyModules = findViewById(R.id.recyclerRecentModule);
        courseId = getIntent().getLongExtra("courseId", 0);
        retrofitProvider = new RetrofitProvider(ProgramPage.this);
        courseClient = new CourseClient(retrofitProvider);
        lessonClient = new LessonClient(retrofitProvider);
    }

    private void handle() {
        buttonBack.setOnClickListener(v -> onBackPressed());
    }

    static class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {
        private final List<CourseProgramDTO> modules;

        ModuleAdapter(List<CourseProgramDTO> modules) {
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
            CourseProgramDTO module = modules.get(position);
            holder.moduleTitle.setText(module.moduleName());
            holder.lessonRecycler.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.lessonRecycler.setAdapter(new LessonAdapter(module.lessonPreviewDTOS()));
        }

        @Override
        public int getItemCount() {
            return modules.size();
        }
    }

    static class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {
        private final List<LessonPreviewDTO> lessons;

        LessonAdapter(List<LessonPreviewDTO> lessons) {
            this.lessons = lessons;
        }

        static class LessonViewHolder extends RecyclerView.ViewHolder {
            TextView titleAndNumber, teacher;
            ImageView teacherAvatar;

            LessonViewHolder(View itemView) {
                super(itemView);
                titleAndNumber = itemView.findViewById(R.id.lessonNumberAndTitle);
                teacher = itemView.findViewById(R.id.lessonTeacher);
                teacherAvatar = itemView.findViewById(R.id.teacherAvatar);
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
            LessonPreviewDTO lesson = lessons.get(position);
            Long lessonId = lesson.id();

            holder.titleAndNumber.setText(lesson.name());
            holder.teacher.setText(lesson.authorName());

            teacherTextHandle(holder.teacher, lessonId);

            Glide.with(holder.teacherAvatar.getContext()).clear(holder.teacherAvatar);
            Glide.with(holder.teacherAvatar.getContext())
                    .load(lesson.authorPhotoPath())
                    .into(holder.teacherAvatar);


            holder.itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, LessonPage.class);
                intent.putExtra("lessonId", lesson.id());
                context.startActivity(intent);
                lessonClient.updateLastLesson(lessonId, new LessonClient.UpdateLastLessonCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(String message) {
                        Activity activity = (Activity) v.getContext();
                        activity.runOnUiThread(() ->
                                Toast.makeText(activity,
                                                message,
                                                Toast.LENGTH_SHORT)
                                        .show());
                    }
                });
            });
        }

        private void teacherTextHandle(TextView teacher, Long lessonId) {
            teacher.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(v.getContext(), ProfilePage.class);
                intent.putExtra("fromPage", "lesson");
                intent.putExtra("lessonId", lessonId);
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return lessons.size();
        }

    }
}
