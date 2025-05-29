package org.hse.protim.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.hse.protim.DTO.CompanyDTO;
import org.hse.protim.DTO.SpecialistsDTO;
import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.DTO.project.RetLikesDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.projects.ProjectClient;
import org.hse.protim.clients.retrofit.search.SearchClient;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
//    private FlexboxLayout selectedFiltersContainer;
    private ImageButton settingsButton;
    private static ProjectClient projectClient;
    private RetrofitProvider retrofitProvider;
    private ProjectsAdapter projectsAdapter;
    private SpecialistsAdapter specialistsAdapter;
    private CompaniesAdapter companiesAdapter;
    private List<ProjectDTO> projectDTOS = new ArrayList<>();
    private List<SpecialistsDTO> specialistsDTOS = new ArrayList<>();
    private List<CompanyDTO> companyDTOS = new ArrayList<>();
    private EditText searchInput;
    private List<String> selectedSoftware;
    private List<String> selectedSections;
    private SearchClient searchClient;
    private ImageView searchIcon;
    private ImageView clearSearch;
    private int startPage;
    private int beforePage;
    private String searchInputString;
    private boolean isFirstTabSelection;
    private boolean isTabSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        settingsButton = findViewById(R.id.settings_button);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter());
        startPage = getIntent().getIntExtra("start_page", 0);
        beforePage = getIntent().getIntExtra("before_page", 0);
        isFirstTabSelection = true;
        viewPager.setCurrentItem(startPage, false);

        retrofitProvider = new RetrofitProvider(SearchPage.this);
        projectClient = new ProjectClient(retrofitProvider);
        searchClient = new SearchClient(retrofitProvider);

        searchInput = findViewById(R.id.search_input);
        String searchInputInt = getIntent().getStringExtra("searchInput");
        searchInputString = searchInputInt == null ? "" : searchInputInt;

        searchInput.setText(searchInputString);


        searchIcon = findViewById(R.id.search_icon);
        clearSearch = findViewById(R.id.clear_search);

        searchIcon.setOnClickListener(v -> loadData());
        clearSearch.setOnClickListener(v -> searchInput.setText(""));
        tabLayoutHandle();

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Проекты");
                    break;
                case 1:
                    tab.setText("Специалисты");
                    break;
                case 2:
                    tab.setText("Компании");
                    break;
            }
        }).attach();


//        selectedFiltersContainer = findViewById(R.id.selected_filters_container);
//
//        if (selectedSections != null) {
//            addFilterTags(selectedSections);
//        }
//        if (selectedSoftware != null) {
//            addFilterTags(selectedSoftware);
//        }
        settingsButtonHandle();
    }

//    private void loadCurr() {
//        switch(tabLayout.getSelectedTabPosition()) {
//            case 1:
//                loadSpecialists();
//                loadWithoutFilterProjects();
//                loadWithoutFilterCompanies();
//                break;
//            case 2:
//                loadCompanies();
//                loadWithoutFilterProjects();
//                loadWithoutFiltersSpecialists();
//                break;
//            default:
//                loadProjects();
//                loadWithoutFiltersSpecialists();
//                loadWithoutFilterCompanies();
//        }
//    }

    private void settingsButtonHandle() {
        ArrayList<String> selectedSectionsIntent = getIntent().getStringArrayListExtra("selected_sections");
        ArrayList<String> selectedSoftwareIntent = getIntent().getStringArrayListExtra("selected_software");

        selectedSections = selectedSectionsIntent == null || !isTabSelected ? new ArrayList<>() : selectedSectionsIntent;
        selectedSoftware = selectedSoftwareIntent == null || !isTabSelected ? new ArrayList<>() : selectedSoftwareIntent;

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(SearchPage.this, FiltersPage.class);
            intent.putExtra("searchInput", searchInput.getText().toString());
            intent.putStringArrayListExtra("selected_sections", selectedSectionsIntent);
            intent.putStringArrayListExtra("selected_software", selectedSoftwareIntent);
            intent.putExtra("start_page", tabLayout.getSelectedTabPosition());
            intent.putExtra("before_page", beforePage);
            startActivity(intent);
        });
    }

    private void tabLayoutHandle() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                if (isFirstTabSelection) {
                    searchInput.setText(searchInputString);
                    isFirstTabSelection = false;
                }
                else {
                    searchInput.setText("");
                    isTabSelected = false;
                }

                switch (pos) {
                    case 1:
                        searchInput.setHint("Имя специалиста");
                        settingsButtonHandle();
                        loadSpecialists();
                        break;
                    case 2:
                        searchInput.setHint("Название компании");
                        settingsButtonHandle();
                        loadCompanies();
                        break;
                    default:
                        settingsButtonHandle();
                        searchInput.setHint("Название проекта");
                        loadProjects();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void loadData() {
        switch (tabLayout.getSelectedTabPosition()) {
            case 1:
                loadSpecialists();
                break;
            case 2:
                loadCompanies();
                break;
            default:
                loadProjects();
        }
    }

//    private void addFilterTags(ArrayList<String> filters) {
//        for (String filter : filters) {
//            View view = LayoutInflater.from(this).inflate(R.layout.item_filter_tag, selectedFiltersContainer, false);
//            TextView text = view.findViewById(R.id.tag_text);
//            ImageView close = view.findViewById(R.id.tag_close);
//
//            text.setText(filter);
//            view.setSelected(true);
//            text.setTextColor(ContextCompat.getColor(this, R.color.white));
//            view.setBackground(ContextCompat.getDrawable(this, R.drawable.tag_selector));
//
//            selectedFiltersContainer.addView(view);
//        }
//    }

    // 🔹 ViewPager Adapter
    private class ViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public int getItemCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

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
                ((ProjectsViewHolder) holder).bind(projectDTOS);
            } else if (holder instanceof SpecialistsViewHolder) {
                ((SpecialistsViewHolder) holder).bind(specialistsDTOS);
            } else if (holder instanceof CompaniesViewHolder) {
                ((CompaniesViewHolder) holder).bind(companyDTOS);
            }
        }

        // ---------- Holders ----------
        class ProjectsViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recycler;

            ProjectsViewHolder(View v) {
                super(v);
                recycler = v.findViewById(R.id.recycler_projects);
                projectsAdapter = new ProjectsAdapter(projectDTOS);
                recycler.setAdapter(projectsAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
            }

            void bind(List<ProjectDTO> items) {
            }
        }

        class SpecialistsViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recycler;

            SpecialistsViewHolder(View v) {
                super(v);
                recycler = v.findViewById(R.id.recycler_specialists);
                specialistsAdapter = new SpecialistsAdapter(specialistsDTOS);
                recycler.setAdapter(specialistsAdapter);
                recycler.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
            }

            void bind(List<SpecialistsDTO> items) {
            }
        }

        class CompaniesViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recycler;

            CompaniesViewHolder(View v) {
                super(v);
                recycler = v.findViewById(R.id.recycler_companies);
                companiesAdapter = new CompaniesAdapter(companyDTOS);
                recycler.setAdapter(companiesAdapter);
                recycler.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
            }

            void bind(List<CompanyDTO> items) {
            }
        }

    }

    // 🔹 Общий адаптер для строк (проекты, компании)
    private class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {
        private final List<String> data;

        SimpleTextAdapter(List<String> data) {
            this.data = data;
        }

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

        public int getItemCount() {
            return data.size();
        }
    }

    public static class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

        private final List<ProjectDTO> projects;

        public ProjectsAdapter(List<ProjectDTO> projects) {
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
            ProjectDTO p = projects.get(position);
            Long projectId = p.projectId();

            Glide.with(holder.itemView.getContext()).clear(holder.projectImage);
            Glide.with(holder.itemView.getContext())
                    .load(p.photoPath())
                    .into(holder.projectImage);

            holder.projectDescription.setText(p.name());
            holder.projectHashtags.setText(String.join(" ", p.tags()));
            holder.projectAuthor.setText(p.fullName());
            holder.likesCount.setText(String.valueOf(p.likesCount()));

            holder.projectAuthor.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, ProfilePage.class);
                intent.putExtra("fromPage", "project");
                intent.putExtra("projectId", projectId);
                context.startActivity(intent);
            });

            projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
                @Override
                public void onSuccess(Boolean isLike) {
                    holder.favoriteButton.setSelected(isLike);
                }

                @Override
                public void onError(String message) {
                }
            });
            projectClient.checkLikeStatus(p.projectId(), new ProjectClient.LikeCallback() {
                @Override
                public void onSuccess(Boolean isLike) {
                    holder.likeButton.setSelected(isLike);
                }

                @Override
                public void onError(String message) {
                }
            });

            holder.favoriteButton.setOnClickListener(v -> {
                projectClient.updateFavourites(projectId, new ProjectClient.PutLikeCallBack() {
                    @Override
                    public void onSuccess() {
                        projectClient.checkFavourites(projectId, new ProjectClient.LikeCallback() {
                            @Override
                            public void onSuccess(Boolean isLike) {
                                holder.favoriteButton.setSelected(isLike);
                            }

                            @Override
                            public void onError(String message) {
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                    }
                });
            });

            holder.likeButton.setOnClickListener(v -> {
                projectClient.putLike(projectId, new ProjectClient.PutLikeCallBack() {
                    @Override
                    public void onSuccess() {
                        projectClient.getProjectLikeCount(projectId, new ProjectClient.LikeCountCallback() {
                            @Override
                            public void onSuccess(Integer count) {
                                holder.likesCount.setText(String.valueOf(count));
                                holder.likeButton.setSelected(!holder.likeButton.isSelected());
                            }

                            @Override
                            public void onError(String message) {
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            });

            holder.likesCount.setOnClickListener(v -> {
                Context currContext = v.getContext();
                Intent intent = new Intent(currContext, RatedPage.class);
                intent.putExtra("PROJECT_ID", projectId);
                currContext.startActivity(intent);
            });

//            holder.projectAuthor.setOnClickListener(v -> {
//                Toast.makeText(v.getContext(), "Автор: " + p.author, Toast.LENGTH_SHORT).show();
//            });
//
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ProjectDetailsPage.class);
                intent.putExtra("project_id", p.projectId());
                intent.putExtra("project_name", p.name());
                intent.putExtra("author", p.fullName());
                v.getContext().startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return projects.size();
        }
    }

    public static class SpecialistsAdapter extends RecyclerView.Adapter<SpecialistsAdapter.ViewHolder> {
        private final List<SpecialistsDTO> list;

        public SpecialistsAdapter(List<SpecialistsDTO> list) {
            this.list = list;
        }

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
            SpecialistsDTO s = list.get(i);
            String photoPath = s.photoUrl();

            Glide.with(h.itemView.getContext()).clear(h.photo);
            Glide.with(h.itemView.getContext())
                    .load(photoPath)
                    .error(R.drawable.ic_profile)
                    .into(h.photo);

            specialistClickHandle(h.itemView, s.id());


            h.status.setText(s.status());
            h.name.setText(s.fullName());
            h.city.setText(s.city());
            h.university.setText(s.education());
        }

        public int getItemCount() {
            return list.size();
        }
    }

    private static void specialistClickHandle(View specialistItem, Long specialistId) {
        specialistItem.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ProfilePage.class);
            intent.putExtra("fromPage", "specialist");
            intent.putExtra("userId", specialistId);
            context.startActivity(intent);
        });
    }

    public static class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.ViewHolder> {
        private final List<CompanyDTO> list;

        public CompaniesAdapter(List<CompanyDTO> list) {
            this.list = list;
        }

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

            CompanyDTO c = list.get(i);
            Glide.with(h.itemView.getContext()).clear(h.itemView);

            Glide.with(h.itemView.getContext())
                    .load(c.photoPath())
                    .into(h.logo);

            h.status.setText(c.status());
            h.name.setText(c.name());
            h.city.setText(c.city());
        }

        public int getItemCount() {
            return list.size();
        }
    }
    // 🔹 Данные

    private void loadSpecialists() {
        String name = searchInput.getText().toString();
        searchClient.getFilterSpecialists(name, selectedSections, selectedSoftware, new SearchClient.SpecialistsCallback() {
            @Override
            public void onSuccess(List<SpecialistsDTO> specialists) {
                runOnUiThread(() -> {
                    specialistsDTOS.clear();
                    specialistsDTOS.addAll(specialists);
                    if (specialistsAdapter != null) {
                        specialistsAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SearchPage.this, message, Toast.LENGTH_LONG).show());
            }
        });
    }


    private void loadProjects() {
        String name = searchInput.getText().toString();
        searchClient.getFilterProjects(name, selectedSections, selectedSoftware, new SearchClient.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectDTO> projects) {
                runOnUiThread(() -> {
                    projectDTOS.clear();
                    projectDTOS.addAll(projects);
                    if (projectsAdapter != null) {
                        projectsAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SearchPage.this, message, Toast.LENGTH_LONG).show());
            }
        });
    }


    private void loadCompanies() {
        String name = searchInput.getText().toString();
        searchClient.getFilterCompanies(name, new SearchClient.CompanyCallback() {
            @Override
            public void onSuccess(List<CompanyDTO> companies) {
                runOnUiThread(() -> {
                    companyDTOS.clear();
                    companyDTOS.addAll(companies);
                    if (companiesAdapter != null) {
                        companiesAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(String message) {
            }
        });
    }
//
//    private void loadWithoutFiltersSpecialists() {
//        searchClient.getFilterSpecialists("", null, null, new SearchClient.SpecialistsCallback() {
//            @Override
//            public void onSuccess(List<SpecialistsDTO> specialists) {
//                runOnUiThread(() -> {
//                    specialistsDTOS.clear();
//                    specialistsDTOS.addAll(specialists);
//                    if (specialistsAdapter != null) {
//                        specialistsAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String message) {
//                runOnUiThread(() -> Toast.makeText(SearchPage.this, message, Toast.LENGTH_LONG).show());
//            }
//        });
//    }
//
//
//    private void loadWithoutFilterProjects() {
//        searchClient.getFilterProjects("", null, null, new SearchClient.ProjectCallback() {
//            @Override
//            public void onSuccess(List<ProjectDTO> projects) {
//                runOnUiThread(() -> {
//                    projectDTOS.clear();
//                    projectDTOS.addAll(projects);
//                    if (projectsAdapter != null) {
//                        projectsAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String message) {
//                runOnUiThread(() -> Toast.makeText(SearchPage.this, message, Toast.LENGTH_LONG).show());
//            }
//        });
//    }
//
//
//    private void loadWithoutFilterCompanies() {
//        searchClient.getFilterCompanies("", new SearchClient.CompanyCallback() {
//            @Override
//            public void onSuccess(List<CompanyDTO> companies) {
//                runOnUiThread(() -> {
//                    companyDTOS.clear();
//                    companyDTOS.addAll(companies);
//                    if (companiesAdapter != null) {
//                        companiesAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String message) {
//            }
//        });
//    }
}
