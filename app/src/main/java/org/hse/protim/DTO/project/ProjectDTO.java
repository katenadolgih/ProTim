package org.hse.protim.DTO.project;

import java.util.List;

public record ProjectDTO(Long projectId, String photoPath, List<String> tags,
                         String name, Long userAuthorId, String fullName, Integer likesCount) {}
