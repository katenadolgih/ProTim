package org.hse.protim.pages;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FlexboxLayout selectedFiltersContainer;
    private ImageButton settingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        settingsButton = findViewById(R.id.settings_button);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter());

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Проекты"); break;
                case 1: tab.setText("Специалисты"); break;
                case 2: tab.setText("Компании"); break;
            }
        }).attach();

        selectedFiltersContainer = findViewById(R.id.selected_filters_container);
        ArrayList<String> selectedSections = getIntent().getStringArrayListExtra("selected_sections");
        ArrayList<String> selectedSoftware = getIntent().getStringArrayListExtra("selected_software");

        if (selectedSections != null) {
            addFilterTags(selectedSections);
        }
        if (selectedSoftware != null) {
            addFilterTags(selectedSoftware);
        }
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(SearchPage.this, FiltersPage.class);
            startActivity(intent);
        });
    }

    private void addFilterTags(ArrayList<String> filters) {
        for (String filter : filters) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_filter_tag, selectedFiltersContainer, false);
            TextView text = view.findViewById(R.id.tag_text);
            ImageView close = view.findViewById(R.id.tag_close);

            text.setText(filter);
            view.setSelected(true);
            text.setTextColor(ContextCompat.getColor(this, R.color.white));
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.tag_selector));

            selectedFiltersContainer.addView(view);
        }
    }

    // 🔹 ViewPager Adapter
    private class ViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override public int getItemCount() { return 3; }
        @Override public int getItemViewType(int position) { return position; }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            if (viewType == 0) {
                View view = inflater.inflate(R.layout.page_projects, parent, false);
                return new ProjectsViewHolder(view);
            } else if (viewType == 1) {
                View view = inflater.inflate(R.layout.page_specialists, parent, false);
                return new SpecialistsViewHolder(view);
            } else {
                View view = inflater.inflate(R.layout.page_companies, parent, false);
                return new CompaniesViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ProjectsViewHolder) {
                ((ProjectsViewHolder) holder).bind(getProjectList());
            } else if (holder instanceof SpecialistsViewHolder) {
                ((SpecialistsViewHolder) holder).bind(getSpecialistList());
            } else if (holder instanceof CompaniesViewHolder) {
                ((CompaniesViewHolder) holder).bind(getCompanyList());
            }
        }

        // ---------- Holders ----------
        class ProjectsViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recycler;
            ProjectsViewHolder(View v) {
                super(v);
                recycler = v.findViewById(R.id.recycler_projects);
                recycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
            }
            void bind(List<Project> items) {
                recycler.setAdapter(new ProjectsAdapter(items));
            }
        }

        class SpecialistsViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recycler;
            SpecialistsViewHolder(View v) {
                super(v);
                recycler = v.findViewById(R.id.recycler_specialists);
                recycler.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
            }
            void bind(List<Specialist> items) {
                recycler.setAdapter(new SpecialistsAdapter(items));
            }
        }

        class CompaniesViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recycler;
            CompaniesViewHolder(View v) {
                super(v);
                recycler = v.findViewById(R.id.recycler_companies);
                recycler.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
            }
            void bind(List<Company> items) {
                recycler.setAdapter(new CompaniesAdapter(items));
            }
        }

    }

    // 🔹 Общий адаптер для строк (проекты, компании)
    private class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {
        private final List<String> data;
        SimpleTextAdapter(List<String> data) { this.data = data; }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            ViewHolder(View v) {
                super(v);
                text = v.findViewById(android.R.id.text1);
            }
        }

        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.text.setText(data.get(position));
        }

        public int getItemCount() { return data.size(); }
    }

    public static class Project {
        public String description, hashtags, author;
        public int imageRes, likes;
        public boolean isLiked, isFavorite;

        public Project(String description, String hashtags, String author,
                       int imageRes, int likes, boolean isLiked, boolean isFavorite) {
            this.description = description; this.hashtags = hashtags; this.author = author; this.imageRes = imageRes; this.likes = likes; this.isLiked = isLiked; this.isFavorite = isFavorite;
        }
    }


    public static class Specialist {
        public String name, status, city, university;
        public int imageRes;
        public Specialist(String name, String status, String city, String university, int imageRes) {
            this.name = name; this.status = status; this.city = city; this.university = university; this.imageRes = imageRes;
        }
    }
    public static class Company {
        public String name, status, city;
        public int imageRes;

        public Company(String name, String status, String city, int imageRes) {
            this.name = name; this.status = status;  this.city = city; this.imageRes = imageRes;
        }
    }
    public static class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

        private final List<Project> projects;

        public ProjectsAdapter(List<Project> projects) {
            this.projects = projects;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ShapeableImageView projectImage;
            ImageButton favoriteButton, likeButton;
            TextView projectDescription, projectHashtags, projectAuthor, likesCount;

            public ViewHolder(View itemView) {
                super(itemView);
                projectImage = itemView.findViewById(R.id.projectImage);
                favoriteButton = itemView.findViewById(R.id.favoriteButton);
                likeButton = itemView.findViewById(R.id.likeButton);
                projectDescription = itemView.findViewById(R.id.projectDescription);
                projectHashtags = itemView.findViewById(R.id.projectHashtags);
                projectAuthor = itemView.findViewById(R.id.projectAuthor);
                likesCount = itemView.findViewById(R.id.likesCount);
            }
        }

        @NonNull
        @Override
        public ProjectsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_project, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProjectsAdapter.ViewHolder holder, int position) {
            Project p = projects.get(position);

            holder.projectImage.setImageResource(p.imageRes);
            holder.projectDescription.setText(p.description);
            holder.projectHashtags.setText(p.hashtags);
            holder.projectAuthor.setText(p.author);
            holder.likesCount.setText(String.valueOf(p.likes));

            // Пример: изменение изображения кнопок (если у тебя selector — не обязательно)
            holder.favoriteButton.setSelected(p.isFavorite);
            holder.likeButton.setSelected(p.isLiked);

            // Обработчики кликов (можно заменить на коллбэки)
            holder.favoriteButton.setOnClickListener(v -> {
                p.isFavorite = !p.isFavorite;
                notifyItemChanged(position);
            });

            holder.likeButton.setOnClickListener(v -> {
                p.isLiked = !p.isLiked;
                p.likes += p.isLiked ? 1 : -1;
                notifyItemChanged(position);
            });

            holder.projectAuthor.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Автор: " + p.author, Toast.LENGTH_SHORT).show();
            });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ProjectDetailsPage.class);
                intent.putExtra("project_name", p.description);
                v.getContext().startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return projects.size();
        }
    }

    public static class SpecialistsAdapter extends RecyclerView.Adapter<SpecialistsAdapter.ViewHolder> {
        private final List<Specialist> list;
        public SpecialistsAdapter(List<Specialist> list) { this.list = list; }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView photo;
            TextView status, name, city, university;
            public ViewHolder(View v) {
                super(v);
                photo = v.findViewById(R.id.specialist_photo);
                status = v.findViewById(R.id.specialist_status);
                name = v.findViewById(R.id.specialist_name);
                city = v.findViewById(R.id.specialist_city);
                university = v.findViewById(R.id.specialist_university);
            }
        }

        @NonNull
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_specialist, parent, false);
            return new ViewHolder(v);
        }

        public void onBindViewHolder(ViewHolder h, int i) {
            Specialist s = list.get(i);
            h.photo.setImageResource(s.imageRes);
            h.status.setText(s.status);
            h.name.setText(s.name);
            h.city.setText(s.city);
            h.university.setText(s.university);
        }

        public int getItemCount() { return list.size(); }
    }
    public static class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.ViewHolder> {
        private final List<Company> list;
        public CompaniesAdapter(List<Company> list) { this.list = list; }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView logo;
            TextView status, name, city;
            public ViewHolder(View v) {
                super(v);
                logo = v.findViewById(R.id.company_logo);
                status = v.findViewById(R.id.company_status);
                name = v.findViewById(R.id.company_name);
                city = v.findViewById(R.id.company_city);
            }
        }

        @NonNull
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_company, parent, false);
            return new ViewHolder(v);
        }

        public void onBindViewHolder(ViewHolder h, int i) {
            Company c = list.get(i);
            h.logo.setImageResource(c.imageRes);
            h.status.setText(c.status);
            h.name.setText(c.name);
            h.city.setText(c.city);
        }

        public int getItemCount() { return list.size(); }
    }
    // 🔹 Данные

    private List<Specialist> getSpecialistList() {
        List<Specialist> list = new ArrayList<>();
        list.add(new Specialist("Михалева Софья", "Ищу работу", "Пермь", "ПСК", R.drawable.ic_user));
        list.add(new Specialist("Иванова Алекса", "Открыт к предложениям", "Екатеринбург", "УрФУ", R.drawable.ic_user));
        list.add(new Specialist("Ипалева Софья", "Ищу работу", "Пермь", "ПСК", R.drawable.ic_user));
        list.add(new Specialist("Иваненченко Виктория", "Открыт к предложениям", "Екатеринбург", "УрФУ", R.drawable.ic_user));
        list.add(new Specialist("Ипалева Софья", "Ищу работу", "Пермь", "ПСК", R.drawable.ic_user));
        list.add(new Specialist("Иваненченко Виктория", "Открыт к предложениям", "Екатеринбург", "УрФУ", R.drawable.ic_user));
        return list;
    }


    private List<Project> getProjectList() {
        List<Project> list = new ArrayList<>();
        list.add(new Project("Протим 2.0 — цифровизация инженерного контента", "#BIM #UX #Проектирование",
                "Иван Петров", R.drawable.photo_project, 124, false, false));
        list.add(new Project("Умный город — мониторинг объектов ЖКХ", "#SmartCity #IoT",
                "Софья Кузнецова", R.drawable.photo_project, 98, true, true));
        list.add(new Project("AI-советник — нейросеть для подбора решений", "#AI #ML #UX",
                "Алексей Смирнов", R.drawable.photo_project, 243, false, true));
        return list;
    }


    private List<Company> getCompanyList() {
        List<Company> list = new ArrayList<>();
        list.add(new Company("Кайрос Инжиниринг", "Ищем специалистов", "Пермь", R.drawable.ic_user));
        list.add(new Company("Тинькофф", "Активно растем", "Москва", R.drawable.logo_protim_48x48));
        list.add(new Company("Яндекс", "Удаленная работа", "Санкт-Петербург", R.drawable.logo_protim_48x48));
        return list;
    }
}
