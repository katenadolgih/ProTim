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

import org.hse.protim.DTO.courses.OwnedCourseDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.courses.CourseClient;

import java.util.ArrayList;
import java.util.List;

public class CoursesPage extends BaseActivity {

    RecyclerView recyclerView;
    TextView emptyMessage;;
    private RetrofitProvider retrofitProvider;
    private CourseClient courseClient;

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
        retrofitProvider = new RetrofitProvider(CoursesPage.this);
        courseClient = new CourseClient(retrofitProvider);
    }

    private void handle() {
        courseClient.getOwnedCourse(new CourseClient.OwnedCourseCallback() {
            @Override
            public void onSuccess(List<OwnedCourseDTO> ownedCourseDTO) {
                if (ownedCourseDTO.isEmpty()) {
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

                    CourseAdapter adapter = new CourseAdapter(CoursesPage.this, ownedCourseDTO);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CoursesPage.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(CoursesPage.this, message, Toast.LENGTH_LONG)
                        .show());
            }
        });

    }

    public static class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

        private final List<OwnedCourseDTO> courses;
        private final Context context;

        public CourseAdapter(Context context, List<OwnedCourseDTO> courses) {
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
            OwnedCourseDTO course = courses.get(position);
            holder.title.setText(course.name());
            holder.dates.setText(course.courseDate());
            holder.progressBar.setProgress(course.progress());

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProgramPage.class);
                intent.putExtra("courseId", course.courseId());
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }
    }
}
