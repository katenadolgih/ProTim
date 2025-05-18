package org.hse.protim.clients.retrofit.search;

import org.hse.protim.DTO.CompanyDTO;
import org.hse.protim.DTO.SpecialistsDTO;
import org.hse.protim.DTO.project.ProjectDTO;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.projects.ProjectApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchClient {
    private final SearchApi searchApi;

    public SearchClient(RetrofitProvider retrofitProvider) {
        this.searchApi = retrofitProvider.getAuthorizedRetrofit().create(SearchApi.class);
    }

    public void getFilterProjects(String name,
                            List<String> sectionAndStamp,
                            List<String> softwareSkill, ProjectCallback callback) {
        searchApi.getFilterProjects(name, sectionAndStamp, softwareSkill).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProjectDTO>> call, Response<List<ProjectDTO>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<ProjectDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getFilterSpecialists(String name,
                                     List<String> sectionAndStamp,
                                     List<String> softwareSkill, SpecialistsCallback callback) {
        searchApi.getFilterSpecialists(name, sectionAndStamp, softwareSkill).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<SpecialistsDTO>> call, Response<List<SpecialistsDTO>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<SpecialistsDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public void getFilterCompanies(String name, CompanyCallback callback) {
        searchApi.getFilterCompanies(name).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CompanyDTO>> call, Response<List<CompanyDTO>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<CompanyDTO>> call, Throwable throwable) {
                callback.onError(throwable.getMessage());
            }
        });
    }

    public interface ProjectCallback {
        void onSuccess(List<ProjectDTO> projects);
        void onError(String message);
    }

    public interface SpecialistsCallback {
        void onSuccess(List<SpecialistsDTO> specialists);
        void onError(String message);
    }

    public interface CompanyCallback {
        void onSuccess(List<CompanyDTO> companies);
        void onError(String message);
    }
}
