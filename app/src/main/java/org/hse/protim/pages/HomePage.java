package org.hse.protim.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BaseActivity {

    private ViewPager2 viewPagerSlider;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    private DotsIndicator dotsIndicator;
    private int dotsCount;
    private ImageView[] dots;

    private RecyclerView recyclerCourses;
    private TextView seeAllCourses;

    private RecyclerView recyclerProjects;
    private TextView seeAllProjects;

    private RecyclerView recyclerPopularProjects;
    private TextView seeAllPopularProjects;

    private ImageView notificationIcon;
    private ImageView buttonFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        init();
        handle();

        setupSlider();
        setupRecyclerView();
    }

    private void init() {
        viewPagerSlider = findViewById(R.id.viewPagerSlider);
        dotsIndicator = findViewById(R.id.dots_indicator);

        recyclerCourses = findViewById(R.id.recyclerCourses);
        seeAllCourses = findViewById(R.id.seeAllCourses);

        recyclerProjects = findViewById(R.id.recyclerProjects);
        seeAllProjects = findViewById(R.id.seeAllProjects);

        recyclerPopularProjects = findViewById(R.id.recyclerPopularProjects);
        seeAllPopularProjects = findViewById(R.id.seeAllPopularProjects);

        notificationIcon = findViewById(R.id.notificationIcon);

    }

    private void handle() {
        seeAllCourses.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ActualCoursesPage.class);
            startActivity(intent);
        });

        seeAllProjects.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, NewProjectsPage.class);
            startActivity(intent);
        });

        seeAllPopularProjects.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, PopularProjectsPage.class);
            startActivity(intent);
        });

        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, NotificationPage.class);
            startActivity(intent);
        });
    }

    private void setupSlider() {
        List<Integer> sliderImages = new ArrayList<>();
        sliderImages.add(R.drawable.slide1);
        sliderImages.add(R.drawable.slide2);
        sliderImages.add(R.drawable.slide3);

        SliderAdapter adapter = new SliderAdapter(sliderImages);
        viewPagerSlider.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPagerSlider);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int desiredHeight = (int) (metrics.heightPixels * 0.6);

        ViewGroup.LayoutParams params = viewPagerSlider.getLayoutParams();
        params.height = desiredHeight;
        viewPagerSlider.setLayoutParams(params);

        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = viewPagerSlider.getCurrentItem() + 1;
                if (nextItem >= sliderImages.size()) nextItem = 0;
                viewPagerSlider.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 7000);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 7000);

        viewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 7000);
            }
        });
    }

    private void setupRecyclerView() {
        // --- Курсы ---
        recyclerCourses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<View> courseViews = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);

        View course1 = inflater.inflate(R.layout.item_course, recyclerCourses, false);

        ((TextView) course1.findViewById(R.id.courseTitle)).setText("Основы Java для Android");
        ((TextView) course1.findViewById(R.id.courseDates)).setText("С 10 апреля");
        ((TextView) course1.findViewById(R.id.coursePrice)).setText("Бесплатно");

        View course2 = inflater.inflate(R.layout.item_course, recyclerCourses, false);
        ((TextView) course2.findViewById(R.id.courseTitle)).setText("Проектирование интерфейсов");
        ((TextView) course2.findViewById(R.id.courseDates)).setText("С 15 апреля");
        ((TextView) course2.findViewById(R.id.coursePrice)).setText("1990 ₽");

        courseViews.add(course1);
        courseViews.add(course2);

        // Заведение тегов вручную -------
        FlexboxLayout tagsContainer = course1.findViewById(R.id.tags_container);

        List<String> tags = new ArrayList<>();
        tags.add("Android");
        tags.add("UI/UX");
        tags.add("TeamWork");

        LayoutInflater tagInflater = LayoutInflater.from(this);
        for (String tag : tags) {
            TextView tagView = (TextView) tagInflater.inflate(R.layout.item_tag, tagsContainer, false);
            tagView.setText(tag);
            tagsContainer.addView(tagView);
        }

        recyclerCourses.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(courseViews.get(viewType)) {};
            }
            @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}
            @Override public int getItemCount() { return courseViews.size(); }
            @Override public int getItemViewType(int position) { return position; }
        });

        // --- Новые проекты ---
        recyclerProjects.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<View> projectViews = new ArrayList<>();

        View project1 = inflater.inflate(R.layout.item_project, recyclerProjects, false);
        ((ImageView) project1.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project1.findViewById(R.id.projectDescription)).setText("Описание проекта 1 ооочен очень оооооооочень боооольшооооооооооооооооооооооооооооооооооооое");
        ((TextView) project1.findViewById(R.id.projectHashtags)).setText("#UI #Design #Mobile");
        ((TextView) project1.findViewById(R.id.projectAuthor)).setText("Авторов Автор");
        ((TextView) project1.findViewById(R.id.likesCount)).setText("29");

        View project2 = inflater.inflate(R.layout.item_project, recyclerProjects, false);
        ((ImageView) project2.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) project2.findViewById(R.id.projectDescription)).setText("Описание проекта 2");
        ((TextView) project2.findViewById(R.id.projectHashtags)).setText("#UX #Design #Mobile");
        ((TextView) project2.findViewById(R.id.projectAuthor)).setText("Илларионов Инокентий");
        ((TextView) project2.findViewById(R.id.likesCount)).setText("2999");

        projectViews.add(project1);
        projectViews.add(project2);

        recyclerProjects.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(projectViews.get(viewType)) {};
            }
            @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}
            @Override public int getItemCount() { return projectViews.size(); }
            @Override public int getItemViewType(int position) { return position; }
        });

        // Вручную - лайки и избраное, переход на страницу Оценили -------
        TextView likes1 = project1.findViewById(R.id.likesCount);
        likes1.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, RatedPage.class);
            startActivity(intent);
        });

        ImageButton likeButton1 = project1.findViewById(R.id.likeButton);
        likeButton1.setOnClickListener(v -> {
            boolean selected = likeButton1.isSelected();
            likeButton1.setSelected(!selected);
        });

        ImageButton favoriteButton1 = project1.findViewById(R.id.favoriteButton);
        if (favoriteButton1 != null) {
            favoriteButton1.setOnClickListener(v -> {
                boolean selected = favoriteButton1.isSelected();
                favoriteButton1.setSelected(!selected);
                showFavoritePopup();
                showSelectionPopup();

            });
        }


        // --- Популярные проекты ---
        recyclerPopularProjects.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<View> popularProjectViews = new ArrayList<>();

        View popularProject1 = inflater.inflate(R.layout.item_project, recyclerPopularProjects, false);
        ((ImageView) popularProject1.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) popularProject1.findViewById(R.id.projectDescription)).setText("Популярный проект 1");
        ((TextView) popularProject1.findViewById(R.id.projectHashtags)).setText("#Trending #Mobile");
        ((TextView) popularProject1.findViewById(R.id.projectAuthor)).setText("Илларионов Инокентий");
        ((TextView) popularProject1.findViewById(R.id.likesCount)).setText("2999");

        View popularProject2 = inflater.inflate(R.layout.item_project, recyclerPopularProjects, false);
        ((ImageView) popularProject2.findViewById(R.id.projectImage)).setImageResource(R.drawable.photo_project);
        ((TextView) popularProject2.findViewById(R.id.projectDescription)).setText("Популярный проект 2");
        ((TextView) popularProject2.findViewById(R.id.projectHashtags)).setText("#Trending #UI");
        ((TextView) popularProject2.findViewById(R.id.projectAuthor)).setText("Илларионов Инокентий");
        ((TextView) popularProject2.findViewById(R.id.likesCount)).setText("2999");

        popularProjectViews.add(popularProject1);
        popularProjectViews.add(popularProject2);

        recyclerPopularProjects.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(popularProjectViews.get(viewType)) {};
            }
            @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}
            @Override public int getItemCount() { return popularProjectViews.size(); }
            @Override public int getItemViewType(int position) { return position; }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}
