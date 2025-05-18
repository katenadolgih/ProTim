package org.hse.protim.clients.retrofit.search;

import org.hse.protim.DTO.CompanyDTO;
import org.hse.protim.DTO.SpecialistsDTO;
import org.hse.protim.DTO.project.ProjectDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApi {
    @GET("search/projects")
    Call<List<ProjectDTO>> getFilterProjects(
            @Query("name") String name,
            @Query("sectionAndStamp") List<String> sectionAndStamp,
            @Query("softwareSkill") List<String> softwareSkill
    );

    @GET("search/specialists")
    Call<List<SpecialistsDTO>> getFilterSpecialists(
            @Query("name") String name,
            @Query("sectionAndStamp") List<String> sectionAndStamp,
            @Query("softwareSkill") List<String> softwareSkill
    );

    @GET("search/companies")
    Call<List<CompanyDTO>> getFilterCompanies(
            @Query("name") String name
    );
}
