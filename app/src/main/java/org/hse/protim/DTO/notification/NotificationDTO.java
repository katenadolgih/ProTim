package org.hse.protim.DTO.notification;

import java.time.LocalDateTime;

public record NotificationDTO(String photoPath, String text, Long userId, String time) {}
