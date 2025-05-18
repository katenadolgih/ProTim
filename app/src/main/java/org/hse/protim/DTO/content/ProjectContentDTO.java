package org.hse.protim.DTO.content;

public record ProjectContentDTO(
        Long id,
        String contentType,
        String content,
        String contentUrl,
        Integer contentOrder,
        Long projectId
) {
}
