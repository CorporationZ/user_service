package school.faang.user_service.dto;

import lombok.Builder;
import school.faang.user_service.entity.RequestStatus;

import java.time.LocalDate;

@Builder
public record RequestFilterDto(
        Long requesterId,
        Long receiverId,
        RequestStatus status,
        LocalDate createdAt,
        LocalDate updatedAt
) {}
