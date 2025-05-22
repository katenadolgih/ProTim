package org.hse.protim.DTO.courses;

import org.hse.protim.DTO.lesson.LessonPreviewDTO;

import java.util.List;

public record CourseProgramDTO(
        String moduleName,
        List<LessonPreviewDTO> lessonPreviewDTOS
) {
}
