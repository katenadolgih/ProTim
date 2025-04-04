package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.hse.protim.R;

public class HomePage extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNav = findViewById(R.id.bottomNavigation);
//        setupNavigation();
    }

//    private void setupNavigation() {
//        bottomNav.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    startActivity(new Intent(this, HomePage.class));
//                    return true;
//                case R.id.nav_search:
//                    startActivity(new Intent(this, SearchPage.class));
//                    return true;
//                case R.id.nav_courses:
//                    startActivity(new Intent(this, CoursesPage.class));
//                    return true;
//                case R.id.nav_favorites:
//                    startActivity(new Intent(this, FavoritesPage.class));
//                    return true;
//                case R.id.nav_profile:
//                    startActivity(new Intent(this, ProfilePage.class));
//                    return true;
//            }
//            return false;
//        });
//
//    }

}
