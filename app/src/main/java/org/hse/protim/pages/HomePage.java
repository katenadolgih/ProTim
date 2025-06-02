package org.hse.protim.pages;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.hse.protim.DTO.courses.CoursePreviewDTO;
import org.hse.protim.DTO.notification.LastNotificationDTO;
import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.courses.CourseClient;
import org.hse.protim.clients.retrofit.notification.NotificationClient;
import org.hse.protim.clients.retrofit.projects.ProjectClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomePage extends BaseActivity {

    private ViewPager2 viewPagerSlider;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;
    private DotsIndicator dotsIndicator;
    private RecyclerView recyclerCourses;
    private TextView seeAllCourses;
    private RecyclerView recyclerProjects;
    private TextView seeAllProjects;

    private RecyclerView recyclerPopularProjects;
    private TextView seeAllPopularProjects;

    private ImageView notificationIcon, goToUser;
    private RetrofitProvider retrofitProvider;
    private CourseClient courseClient;
    private ProjectClient projectClient;
    private NotificationClient notificationClient;

    private Map<Long, List<ImageButton>> buttonLikeMap = new HashMap<>();
    private Map<Long, List<TextView>> textViewMap = new HashMap<>();
    private Map<Long, List<ImageButton>> favouritesMap = new HashMap<>();
    private ImageView buttonFavorite;
    private ShapeableImageView profileAvatar;
    private TextView userName;
    private LinearLayout topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        init();
        handle();

        setupSlider();
        setupRecyclerView();
        setupNotification();
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

        retrofitProvider = new RetrofitProvider(this);
        courseClient = new CourseClient(retrofitProvider);

        projectClient = new ProjectClient(retrofitProvider);
        notificationClient = new NotificationClient(retrofitProvider);

        profileAvatar = findViewById(R.id.profileAvatar);
        userName = findViewById(R.id.userName);
        topBar = findViewById(R.id.topBar);
        goToUser = findViewById(R.id.goToUser);
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

    private void setupNotification() {
        notificationClient.getLastNotification(new NotificationClient.GetLastNotificationCallback() {
            @Override
            public void onSuccess(LastNotificationDTO lastNotificationDTO) {
                if (lastNotificationDTO == null) {
                    topBar.setVisibility(View.GONE);
                } else {
                    userName.setText(lastNotificationDTO.fullName());
                    Glide.with(HomePage.this)
                            .load(lastNotificationDTO.photoPath())
                            .error(R.drawable.ic_profile)
                            .placeholder(R.drawable.ic_profile)
                            .into(profileAvatar);
                    goToUser.setOnClickListener(v -> {
                        Intent intent = new Intent(HomePage.this, ProfilePage.class);
                        intent.putExtra("userId", lastNotificationDTO.userId());
                        intent.putExtra("fromPage", "specialist");
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show());
            }
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
        LayoutInflater inflater = LayoutInflater.from(this);
        setCourses(inflater);
        setNewProjects(inflater);
        setPopularProjects(inflater);
    }

    private void setPopularProjects(LayoutInflater inflater) {
        projectClient.getProjects("popularity", 3, null, null, null,
                new ProjectClient.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projects) {
                recyclerPopularProjects.setLayoutManager(new LinearLayoutManager(HomePage.this, LinearLayoutManager.VERTICAL, false));
                List<View> popularProjectViews = new ArrayList<>();

                for (ProjectDTO project : projects) {
                    View projectView = inflater.inflate(R.layout.item_project, recyclerPopularProjects, false);

                    ((TextView) projectView.findViewById(R.id.projectDescription)).setText(project.name());
                    ((TextView) projectView.findViewById(R.id.projectHashtags)).setText(project.tags().stream().map(tag -> "#" + tag)
                            .collect(Collectors.joining("  ")));
                    TextView projectAuthor = projectView.findViewById(R.id.projectAuthor);
                    projectAuthor.setText(project.fullName());
                    ((TextView) projectView.findViewById(R.id.likesCount)).setText(project.likesCount().toString());

                    ImageView imageView = projectView.findViewById(R.id.projectImage);
                    Glide.with(HomePage.this)
                            .load(project.photoPath())
                            .into(imageView);


                    ImageButton favoriteButton = projectView.findViewById(R.id.favoriteButton);
                    TextView likesText = projectView.findViewById(R.id.likesCount);
                    ImageButton likeButton = projectView.findViewById(R.id.likeButton);

                    Long projectId = project.projectId();
                    List<ImageButton> buttons = buttonLikeMap.computeIfAbsent(projectId, k -> new ArrayList<>());
                    buttons.add(likeButton);
                    List<TextView> textViews = textViewMap.computeIfAbsent(projectId, k -> new ArrayList<>());
                    textViews.add(likesText);
                    List<ImageButton> favouritesButtons = favouritesMap.computeIfAbsent(projectId, k -> new ArrayList<>());
                    favouritesButtons.add(favoriteButton);

                    likeButtonHandler(project.projectId(), likeButton, likesText);
                    favouritesHandler(projectId, favoriteButton);
                    projectAuthorHandler(projectId, projectAuthor);

                    popularProjectViews.add(projectView);

                }

                recyclerPopularProjects.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return new RecyclerView.ViewHolder(popularProjectViews.get(viewType)) {};
                    }
                    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                        ProjectDTO p = projects.get(position);
                        holder.itemView.setOnClickListener(v -> projectClickHandle(p));
                    }
                    @Override public int getItemCount() { return popularProjectViews.size(); }
                    @Override public int getItemViewType(int position) { return position; }
                });
            }

            @Override
            public void onError(String message) {
                Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void projectAuthorHandler(Long projectId, TextView projectAuthor) {
        projectAuthor.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ProfilePage.class);
            intent.putExtra("projectId", projectId);
            intent.putExtra("fromPage", "project");
            startActivity(intent);
        });
    }

    private void projectClickHandle(ProjectDTO p) {
        Intent intent = new Intent(HomePage.this, ProjectDetailsPage.class);
        intent.putExtra("project_id", p.projectId());
        intent.putExtra("project_name", p.name());
        intent.putExtra("author", p.fullName());
        startActivity(intent);
    }

    private void setNewProjects(LayoutInflater inflater) {
        projectClient.getProjects("new", 3, null, null, null,
                new ProjectClient.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projects) {
                recyclerProjects.setLayoutManager(new LinearLayoutManager(HomePage.this));
                List<View> projectViews = new ArrayList<>();

                for (ProjectDTO project : projects) {
                    View projectView = inflater.inflate(R.layout.item_project, recyclerProjects, false);

                    ((TextView) projectView.findViewById(R.id.projectDescription)).setText(project.name());
                    ((TextView) projectView.findViewById(R.id.projectHashtags)).setText(project.tags().stream().map(tag -> "#" + tag)
                            .collect(Collectors.joining("  ")));
                    TextView projectAuthor = projectView.findViewById(R.id.projectAuthor);
                    projectAuthor.setText(project.fullName());
                    ((TextView) projectView.findViewById(R.id.likesCount)).setText(project.likesCount().toString());

                    ImageView imageView = projectView.findViewById(R.id.projectImage);
                    Glide.with(HomePage.this)
                            .load(project.photoPath())
                            .into(imageView);

                    ImageButton likeButton = projectView.findViewById(R.id.likeButton);
                    ImageButton favoriteButton = projectView.findViewById(R.id.favoriteButton);
                    TextView likesText = projectView.findViewById(R.id.likesCount);

                    Long projectId = project.projectId();
                    List<ImageButton> buttons = buttonLikeMap.computeIfAbsent(projectId, k -> new ArrayList<>());
                    buttons.add(likeButton);
                    List<TextView> textViews = textViewMap.computeIfAbsent(projectId, k -> new ArrayList<>());
                    textViews.add(likesText);
                    List<ImageButton> favouritesButtons = favouritesMap.computeIfAbsent(projectId, k -> new ArrayList<>());
                    favouritesButtons.add(favoriteButton);

                    likeButtonHandler(projectId, likeButton, likesText);
                    favouritesHandler(projectId, favoriteButton);
                    projectAuthorHandler(projectId, projectAuthor);



                    projectViews.add(projectView);
                }

                recyclerProjects.setAdapter(new RecyclerView.Adapter<>() {
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return new RecyclerView.ViewHolder(projectViews.get(viewType)) {
                        };
                    }

                    @Override
                    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                        ProjectDTO p = projects.get(position);
                        holder.itemView.setOnClickListener(v -> projectClickHandle(p));
                    }

                    @Override
                    public int getItemCount() {
                        return projectViews.size();
                    }

                    @Override
                    public int getItemViewType(int position) {
                        return position;
                    }
                });
            }

            @Override
            public void onError(String message) {
                Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateLikeButtons(Long projectId, boolean isLiked) {
        List<ImageButton> buttons = buttonLikeMap.get(projectId);
        if (buttons != null) {
            for (ImageButton button : buttons) {
                button.setSelected(isLiked);
            }
        }
    }

    private void updateLikeCounts(Long projectId, int count) {
        List<TextView> textViews = textViewMap.get(projectId);
        if (textViews != null) {
            for (TextView textView : textViews) {
                textView.setText(String.valueOf(count));
            }
        }
    }
    private void likeButtonHandler(Long projectId, ImageButton likeButton, TextView likesText) {
        projectClient.checkLikeStatus(projectId, new ProjectClient.LikeCallback() {
            @Override
            public void onSuccess(Boolean isLike) {
                updateLikeButtons(projectId, isLike);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show();
            }
        });

        likeButton.setOnClickListener(v -> {
            projectClient.putLike(projectId, new ProjectClient.PutLikeCallBack() {
                @Override
                public void onSuccess() {
                    boolean newLikeState = !likeButton.isSelected();

                    updateLikeButtons(projectId, newLikeState);
                    projectClient.getProjectLikeCount(projectId, new ProjectClient.LikeCountCallback() {
                        @Override
                        public void onSuccess(Integer count) {
                            updateLikeCounts(projectId, count);
                        }
                        @Override
                        public void onError(String message) {
                            Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show();
                }
            });
        });

        likesText.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, RatedPage.class);
            intent.putExtra("PROJECT_ID", projectId);
            startActivity(intent);
        });
    }

    private void favouritesHandler(Long projectId, ImageButton favouritesButton) {
        projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
            @Override
            public void onSuccess(Boolean isLike) {
                updateFavouritesButtons(projectId, isLike);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show();
            }
        });

        favouritesButton.setOnClickListener(v -> {
            projectClient.updateFavourites(projectId, new ProjectClient.PutLikeCallBack() {
                @Override
                public void onSuccess() {
                    projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
                        @Override
                        public void onSuccess(Boolean isLike) {
                            updateFavouritesButtons(projectId, isLike);
                            if (isLike) {
                                showFavoritePopup();
                                showSelectionPopup(projectId);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void updateFavouritesButtons(Long projectId, boolean isLiked) {
        List<ImageButton> favourites = favouritesMap.get(projectId);
        if (favourites != null) {
            for (ImageButton button : favourites) {
                button.setSelected(isLiked);
            }
        }
    }

    private void setCourses(LayoutInflater inflater) {
        recyclerCourses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        LayoutInflater tagInflater = LayoutInflater.from(this);

        courseClient.getMainCourses(new CourseClient.CourseCallback() {
            @Override
            public void onSuccess(List<CoursePreviewDTO> courses) {
                runOnUiThread(() -> {
                    List<View> courseViews = new ArrayList<>();

                    for (CoursePreviewDTO course : courses) {
                        View courseView = inflater.inflate(R.layout.item_course, recyclerCourses, false);

                        ((TextView) courseView.findViewById(R.id.courseTitle)).setText(course.name());

                        String dateText = String.format("%s • %s • %s",
                                course.startDate(), course.hours(), course.duration());
                        ((TextView) courseView.findViewById(R.id.courseDates)).setText(dateText);
                        ((TextView) courseView.findViewById(R.id.coursePrice)).setText(course.price());

                        FlexboxLayout tagsContainer = courseView.findViewById(R.id.tags_container);
                        for (String tag : course.tags()) {
                            TextView tagView = (TextView) tagInflater.inflate(R.layout.item_tag, tagsContainer, false);
                            tagView.setText(tag);
                            tagsContainer.addView(tagView);
                        }

                        courseView.setOnClickListener(v -> {
                            Intent intent = new Intent(HomePage.this, CourseDetailsPage.class);
                            intent.putExtra("COURSE_ID", course.id());
                            startActivity(intent);
                        });
                        courseViews.add(courseView);
                    }

                    recyclerCourses.setAdapter(new RecyclerView.Adapter<>() {
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return new RecyclerView.ViewHolder(courseViews.get(viewType)) {
                            };
                        }

                        @Override
                        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                        }

                        @Override
                        public int getItemCount() {
                            return courseViews.size();
                        }

                        @Override
                        public int getItemViewType(int position) {
                            return position;
                        }
                    });
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(HomePage.this, "Ошибка загрузки курсов: " + message, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}
