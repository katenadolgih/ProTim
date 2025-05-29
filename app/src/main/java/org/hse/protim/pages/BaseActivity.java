package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.hse.protim.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected LinearLayout navHome, navSearch, navCourses, navFavorites, navProfile;
    protected static int lastHighlightIcon, lastHighlightText;
    protected PopupWindow popupWindow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        if (contentFrame != null) {
            LayoutInflater.from(this).inflate(layoutResID, contentFrame, true);
        }
        initCustomBottomNav();
        highlightCurrentTab();
    }

    private void initCustomBottomNav() {
        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navCourses = findViewById(R.id.nav_courses);
        navFavorites = findViewById(R.id.nav_favorites);
        navProfile = findViewById(R.id.nav_profile);


        navHome.setOnClickListener(v -> navigateTo(HomePage.class));
        navSearch.setOnClickListener(v -> navigateTo(SearchPage.class));
        navCourses.setOnClickListener(v -> navigateTo(CoursesPage.class));
        navFavorites.setOnClickListener(v -> navigateTo(FavoritesPage.class));
        navProfile.setOnClickListener(v -> navigateTo(ProfilePage.class));
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("fromPage", "");
        // Там ещё клир таск было
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
        if (!this.getClass().equals(activityClass)) {
            finish();
        }
    }

    protected void highlightCurrentTab() {
        if (!(this instanceof FiltersPage)) {
            resetTabStyles();
        } else {
            highlightTab(lastHighlightIcon, lastHighlightText);
        }

        if (this instanceof HomePage
                || this instanceof ActualCoursesPage
                || this instanceof CourseDetailsPage
                || this instanceof NewProjectsPage
                || this instanceof PopularProjectsPage
                || this instanceof NotificationPage
                || this instanceof RatedPage) {
            lastHighlightIcon = R.id.icon_home;
            lastHighlightText = R.id.text_home;
            highlightTab(lastHighlightIcon, lastHighlightText);
        } else if (this instanceof SearchPage) {
            lastHighlightIcon = R.id.icon_search;
            lastHighlightText = R.id.text_search;
            highlightTab(lastHighlightIcon, lastHighlightText);
        } else if (this instanceof CoursesPage
                || this instanceof ProgramPage
                || this instanceof LessonPage) {
            lastHighlightIcon = R.id.icon_courses;
            lastHighlightText = R.id.text_courses;
            highlightTab(lastHighlightIcon, lastHighlightText);
        } else if (this instanceof FavoritesPage
                || this instanceof SelectionsAllPage
                || this instanceof SelectionPage
                || this instanceof SelectionCreatingPage
                || this instanceof SelectionEditingPage
                || this instanceof SelectionDetailsPage) {
            lastHighlightIcon = R.id.icon_favorites;
            lastHighlightText = R.id.text_favorites;
            highlightTab(lastHighlightIcon, lastHighlightText);
        } else if (this instanceof ProfilePage
                || this instanceof SettingsPage
                || this instanceof EditProfilePage) {
            lastHighlightIcon = R.id.icon_profile;
            lastHighlightText = R.id.text_profile;
            highlightTab(lastHighlightIcon, lastHighlightText);
        }
    }

    private void highlightTab(int iconId, int textId) {
        ImageView icon = findViewById(iconId);
        TextView text = findViewById(textId);

        if (icon != null)
            icon.setColorFilter(ContextCompat.getColor(this, R.color.active_field));
        if (text != null)
            text.setTextColor(ContextCompat.getColor(this, R.color.active_field));
    }

    private void resetTabStyles() {
        int[] iconIds = {
                R.id.icon_home, R.id.icon_search, R.id.icon_courses, R.id.icon_favorites, R.id.icon_profile
        };
        int[] textIds = {
                R.id.text_home, R.id.text_search, R.id.text_courses, R.id.text_favorites, R.id.text_profile
        };

        for (int iconId : iconIds) {
            ImageView icon = findViewById(iconId);
            if (icon != null)
                icon.setColorFilter(ContextCompat.getColor(this, R.color.gray_search_text));
        }

        for (int textId : textIds) {
            TextView text = findViewById(textId);
            if (text != null)
                text.setTextColor(ContextCompat.getColor(this, R.color.gray_search_text)); // исправлено
        }
    }
    public void showFavoritePopup() {
        FavoriteBottomSheet bottomSheet = new FavoriteBottomSheet();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }
    public void showSelectionPopup() {
        SelectionBottomSheet bottomSheet = new SelectionBottomSheet();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }


}

