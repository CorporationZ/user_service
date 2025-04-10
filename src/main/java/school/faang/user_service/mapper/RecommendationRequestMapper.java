package school.faang.user_service.mapper;

import org.mapstruct.Mapper;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.entity.recommendation.RecommendationRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationRequestMapper {

    RecommendationRequest toEntity(RecommendationRequestDto dto);

    RecommendationRequestDto toDto(RecommendationRequest entity);

    List<RecommendationRequestDto> toListDto(List<RecommendationRequest> entities);
}
