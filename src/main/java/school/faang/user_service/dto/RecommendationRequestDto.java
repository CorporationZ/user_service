package school.faang.user_service.dto;

import lombok.Builder;

import school.faang.user_service.entity.recommendation.SkillRequest;


import java.time.LocalDateTime;
import java.util.List;
@Builder
public record RecommendationRequestDto(
        Long id,
        String message,
        String status,
        List<SkillRequest> skills,
        Long requesterId,
        Long receiverId,
        Long recommendationId,
        String rejectionReason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
