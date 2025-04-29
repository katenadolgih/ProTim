package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import org.hse.protim.DTO.courses.CoursePreviewDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.courses.CourseClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActualCoursesPage extends BaseActivity {

    private ImageButton buttonBack;
    private RecyclerView recyclerCurrentCourses;
    private TextView titleView;
    private CourseClient courseClient;
    private RetrofitProvider retrofitProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_courses_page);

        init();
        handle();

        recyclerCurrentCourses.setLayoutManager(new LinearLayoutManager(this));
        titleView.setText(R.string.Actual_courses);

        courseClient.getAllCourses(new CourseClient.CourseCallback() {
            @Override
            public void onSuccess(List<CoursePreviewDTO> courses) {
                runOnUiThread(() -> {
                    CourseAdapter adapter = new CourseAdapter(courses);
                    recyclerCurrentCourses.setAdapter(adapter);
                });
            }
            @Override
            public void onError(String message) {
                Toast.makeText(ActualCoursesPage.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        recyclerCurrentCourses = findViewById(R.id.actualCoursesRecycler);
        titleView = findViewById(R.id.title_text);
        retrofitProvider = new RetrofitProvider(this);
        courseClient = new CourseClient(retrofitProvider);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
    }

    private static class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

        private final List<CoursePreviewDTO> courses;

        public CourseAdapter(List<CoursePreviewDTO> courses) {
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
            CoursePreviewDTO course = courses.get(position);
            holder.bind(course);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), CourseDetailsPage.class);
                intent.putExtra("COURSE_ID", course.id());
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

            public void bind(CoursePreviewDTO course) {
                // Очищаем предыдущие теги
                tagsContainer.removeAllViews();

                // Добавляем новые теги
                for (String tag : course.tags()) {
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

                String dateText = String.format("%s • %s • %s", course.startDate(),
                        course.hours(), course.duration());

                courseTitle.setText(course.name());
                courseDates.setText(dateText);
                coursePrice.setText(course.price());
            }
        }
    }

}