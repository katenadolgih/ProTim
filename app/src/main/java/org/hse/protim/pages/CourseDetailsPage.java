package org.hse.protim.pages;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.hse.protim.DTO.courses.CourseDetailDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.courses.CourseClient;

public class CourseDetailsPage extends BaseActivity {

    // Объявляем переменные
    private ImageButton buttonBack;
    private CourseClient courseClient;
    private RetrofitProvider retrofitProvider;
    private Long courseId;
    private TextView courseTitle, courseDetailsFormat, courseDetailsPrice,
            courseDetailsDuration, courseDetailsWhoWhom, courseDetailsWhatMaster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details_page);

        init();
        handle();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.course_details_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setData();
    }

    private void init() {
        buttonBack = findViewById(R.id.button_back);
        retrofitProvider = new RetrofitProvider(this);
        courseClient = new CourseClient(retrofitProvider);
        courseId = getIntent().getLongExtra("COURSE_ID", -1);
        courseTitle = findViewById(R.id.courseTitle);
        courseDetailsFormat = findViewById(R.id.courseDetailsFormat);
        courseDetailsPrice = findViewById(R.id.courseDetailsPrice);
        courseDetailsDuration = findViewById(R.id.courseDetailsDuration);
        courseDetailsWhoWhom = findViewById(R.id.courseDetailsWhoWhom);
        courseDetailsWhatMaster = findViewById(R.id.courseDetailsWhatMaster);
    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
    }

    private void setData() {
        courseClient.getCourseDetail(courseId, new CourseClient.CourseDetailCallback() {
            @Override
            public void onSuccess(CourseDetailDTO courseDetailDTO) {
                courseTitle.setText(courseDetailDTO.name());
                courseDetailsFormat.setText(courseDetailDTO.format());
                courseDetailsPrice.setText(courseDetailDTO.price());
                courseDetailsDuration.setText(courseDetailDTO.duration());
                courseDetailsWhoWhom.setText(courseDetailDTO.whoWhom());
                courseDetailsWhatMaster.setText(courseDetailDTO.whatMaster());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(CourseDetailsPage.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
