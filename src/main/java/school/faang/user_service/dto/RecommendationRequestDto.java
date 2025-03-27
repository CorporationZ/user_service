package school.faang.user_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.List;
@Builder
public record RecommendationRequestDto(
        Long id,
        String message,
        String status,
        List<Long> skills,
        Long requesterId,
        Long receiverId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
