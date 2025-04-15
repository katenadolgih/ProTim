package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActualCoursesPage extends BaseActivity {

    private ImageButton buttonBack;
    private RecyclerView recyclerCurrentCourses;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_courses_page);

        init();
        handle();

        recyclerCurrentCourses.setLayoutManager(new LinearLayoutManager(this));
        titleView.setText(R.string.Actual_courses);

        // Создаем список курсов
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(
                "Базовый курс повышения квалификации «Технологии информационного моделирования. Renga: архитектура, конструктив и инженерные сети трам пам пам»",
                Arrays.asList("AI", "Машинное обучение", "Python", "Машинное обучение","Машинное обучение", "Python"),
                "Старт каждый пн с 31 марта 2025・24 часа・3 недели Старт каждый пн с 31 марта 2025・24 часа・3 недели Старт каждый пн с 31 марта 2025・24 часа・3 недели",
                "От 3 000 ₽"
        ));
        courses.add(new Course(
                "Курс по мобильной разработке",
                Arrays.asList("Android", "Kotlin"),
                "Старт 15 апреля – 15 мая",
                "От 2 900 ₽"
        ));
        courses.add(new Course(
                "Курс по мобильной разработке",
                Arrays.asList("Android", "Kotlin", "Kotlin"),
                "Старт 15 апреля – 15 мая",
                "От 2 900 ₽"
        ));

        // Устанавливаем адаптер
        CourseAdapter adapter = new CourseAdapter(courses);
        recyclerCurrentCourses.setAdapter(adapter);
    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        recyclerCurrentCourses = findViewById(R.id.actualCoursesRecycler);
        titleView = findViewById(R.id.title_text);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
    }

    private static class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

        private final List<Course> courses;

        public CourseAdapter(List<Course> courses) {
            this.courses = courses;
        }

        @NonNull
        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_course_full, parent, false);
            return new CourseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            Course course = courses.get(position);
            holder.bind(course);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), CourseDetailsPage.class);
                holder.itemView.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }

        static class CourseViewHolder extends RecyclerView.ViewHolder {
            private final FlexboxLayout tagsContainer;
            private final TextView courseTitle;
            private final TextView courseDates;
            private final TextView coursePrice;

            public CourseViewHolder(@NonNull View itemView) {
                super(itemView);
                tagsContainer = itemView.findViewById(R.id.tags_container);
                courseTitle = itemView.findViewById(R.id.courseTitle);
                courseDates = itemView.findViewById(R.id.courseDates);
                coursePrice = itemView.findViewById(R.id.coursePrice);
            }

            public void bind(Course course) {
                // Очищаем предыдущие теги
                tagsContainer.removeAllViews();

                // Добавляем новые теги
                for (String tag : course.getTags()) {
                    TextView tagView = new TextView(itemView.getContext());
                    tagView.setText(tag);
                    tagView.setTextSize(10);
                    tagView.setTextColor(itemView.getContext().getResources().getColor(R.color.button_color));
                    tagView.setBackgroundResource(R.drawable.tag_shape);
                    tagView.setPadding(16, 8, 16, 8);

                    FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                            FlexboxLayout.LayoutParams.WRAP_CONTENT,
                            FlexboxLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 8, 8);
                    tagView.setLayoutParams(params);

                    tagsContainer.addView(tagView);
                }

                // Устанавливаем остальные данные
                courseTitle.setText(course.getTitle());
                courseDates.setText(course.getDates());
                coursePrice.setText(course.getPrice());
            }
        }
    }

    private static class Course {
        private final String title;
        private final List<String> tags;
        private final String dates;
        private final String price;

        public Course(String title, List<String> tags, String dates, String price) {
            this.title = title;
            this.tags = tags;
            this.dates = dates;
            this.price = price;
        }

        public String getTitle() { return title; }
        public List<String> getTags() { return tags; }
        public String getDates() { return dates; }
        public String getPrice() { return price; }
    }
}