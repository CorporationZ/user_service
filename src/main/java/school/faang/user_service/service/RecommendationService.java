package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.dto.SkillOfferDto;


import school.faang.user_service.entity.recommendation.Recommendation;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.RecommendationRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final SkillOfferRepository skillOfferRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;


    public Long create(RecommendationDto dto) {
        validateRecommendation(dto);
        Optional<Recommendation> lastRecommendation = recommendationRepository.findFirstByAuthorIdAndReceiverIdOrderByCreatedAtDesc(dto.authorId(), dto.receiverId());
        if (lastRecommendation.isPresent()){
            LocalDateTime lastCreatedAt = lastRecommendation.get().getCreatedAt();
            if (lastCreatedAt.plusMonths(6).isAfter(LocalDateTime.now())){
                throw new DataValidationException("You can give only one recommendation in 6 months");
            }
        }
        Long recommendationId = recommendationRepository.create(dto.authorId(), dto.receiverId(), dto.content());

        for (SkillOfferDto skillOffer : dto.skillOffers()){
            if (skillRepository.findById(skillOffer.skillId()).isEmpty()){
                throw new DataValidationException("there is no skill with id:" + skillOffer.skillId());
            }
            addSkill(skillOffer, recommendationId);
        }
        return recommendationId;
    }

    public void addSkill(SkillOfferDto dto, Long recommendationId){
        if (dto == null || dto.skillId() == null || recommendationId == null){
            throw new DataValidationException("Skills and recommendations should not be empty");
        }
        skillOfferRepository.create(dto.skillId(), recommendationId);
    }


    public void update(RecommendationDto dto) {
        validateRecommendation(dto);
        Optional<Recommendation> recommendation = recommendationRepository.findFirstByAuthorIdAndReceiverIdOrderByCreatedAtDesc(dto.authorId(), dto.receiverId());
        recommendationRepository.update(recommendation.get().getId(), dto.content());
        skillOfferRepository.deleteAllByRecommendationId(recommendation.get().getId());

        for (SkillOfferDto skillOffer : dto.skillOffers()){
            if (skillRepository.findById(skillOffer.skillId()).isEmpty()){
                throw new DataValidationException("there is no skill with id:" + skillOffer.skillId());
            }
            addSkill(skillOffer, recommendation.get().getId());
        }
    }

    private void validateRecommendation(RecommendationDto dto) {
        if (dto.authorId() == null || dto.receiverId() == null || userRepository.findById(dto.authorId()).isEmpty() || userRepository.findById(dto.receiverId()).isEmpty()){
            throw new DataValidationException("Invalid author or/and receiver id");
        }
        if (dto.content() == null || dto.content().trim().isEmpty()){
            throw new DataValidationException("The content should not be empty!");
        }
    }

    public void deleteRecommendation(Long id){
        recommendationRepository.deleteById(id);
        skillOfferRepository.deleteAllByRecommendationId(id);
    }




}
