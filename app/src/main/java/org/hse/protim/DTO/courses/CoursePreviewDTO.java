package org.hse.protim.DTO.courses;

import java.util.List;

public record CoursePreviewDTO(Long id, String name,
                               String price, String duration, String startDate,
                               String hours, List<String> tags) {
}
