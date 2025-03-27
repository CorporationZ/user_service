package school.faang.user_service.dto;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.List;

public record RecommendationDto(
        Long id,
        Long authorId,
        Long receiverId,
        String content,
        List<SkillOfferDto> skillOffers,
        LocalDateTime createdAt
) {
}
