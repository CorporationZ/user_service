package school.faang.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.entity.recommendation.SkillRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecommendationRequestMapper {

    @Mapping(target = "skills", source = "skills", qualifiedByName = "mapSkillIdsToSkillRequests")
    RecommendationRequest toEntity(RecommendationRequestDto dto);

    RecommendationRequestDto toDto(RecommendationRequest entity);

    @Named("mapSkillIdsToSkillRequests")
    default List<SkillRequest> mapSkillIdsToSkillRequests(List<Long> skillIds) {
        if (skillIds == null) {
            return new ArrayList<>();
        }
        return skillIds.stream()
                .map(skillId -> {
                    SkillRequest skillRequest = new SkillRequest();
                    skillRequest.setSkill(skillId);
                    return skillRequest;
                })
                .collect(Collectors.toList());
    }
}

