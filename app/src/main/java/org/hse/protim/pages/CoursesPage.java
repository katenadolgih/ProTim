package org.hse.protim.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class CoursesPage extends BaseActivity {

    RecyclerView recyclerView;
    TextView emptyMessage;
    List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_page);
        init();
        handle();
    }

    private void init() {
        recyclerView = findViewById(R.id.coursesRecyclerView);
        emptyMessage = findViewById(R.id.emptyMessage);
        courseList = getMockCourses(); // Временно
    }

    private void handle() {
        if (courseList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);

            String fullText = "Похоже у вас пока нет курсов к прохождению, выберите подходящий в разделе Актуальные курсы.";
            SpannableString ss = new SpannableString(fullText);

            int start = fullText.indexOf("Актуальные курсы");
            int end = start + "Актуальные курсы".length();

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(CoursesPage.this, "Переход на Актуальные курсы", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.active_field));
                    ds.setFakeBoldText(true);
                    ds.setUnderlineText(false);
                }
            };

            ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            emptyMessage.setText(ss);
            emptyMessage.setMovementMethod(LinkMovementMethod.getInstance());

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);

            CourseAdapter adapter = new CourseAdapter(this, courseList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }

    // Пример курсов
    private List<Course> getMockCourses() {
        List<Course> list = new ArrayList<>();
        list.add(new Course("Курс по Android", "12.03 – 25.04", 30));
        list.add(new Course("Figma для начинающих", "01.04 – 10.05", 80));
        return list;
    }

    // Модель
    public static class Course {
        public String title;
        public String dates;
        public int progress;

        public Course(String title, String dates, int progress) {
            this.title = title;
            this.dates = dates;
            this.progress = progress;
        }
    }

    // Адаптер
    public static class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

        private final List<Course> courses;
        private final Context context;

        public CourseAdapter(Context context, List<Course> courses) {
            this.context = context;
            this.courses = courses;
        }

        public static class CourseViewHolder extends RecyclerView.ViewHolder {
            TextView title, dates;
            ProgressBar progressBar;

            public CourseViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.courseTitle);
                dates = itemView.findViewById(R.id.courseDates);
                progressBar = itemView.findViewById(R.id.courseProgress);
            }
        }

        @NonNull
        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_bought_course, parent, false);
            return new CourseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            Course course = courses.get(position);
            holder.title.setText(course.title);
            holder.dates.setText(course.dates);
            holder.progressBar.setProgress(course.progress);

            // Переход на ProgramPage
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProgramPage.class);
                intent.putExtra("courseTitle", course.title); // можно передавать название
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }
    }
}
