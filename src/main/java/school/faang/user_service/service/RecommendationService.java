package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.dto.SkillOfferDto;


import school.faang.user_service.entity.recommendation.Recommendation;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.RecommendationRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;

import java.time.LocalDateTime;
import java.util.List;
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
            addSkill(skillOffer, recommendationId, dto.receiverId());
        }
        return recommendationId;
    }

    public void addSkill(SkillOfferDto dto, Long recommendationId, Long receiverId){
        if (dto == null || dto.skillId() == null || recommendationId == null){
            throw new DataValidationException("Skills and recommendations should not be empty");
        }
        if (skillOfferRepository.findAllOffersOfSkill(dto.skillId(), receiverId).isEmpty()){
            skillOfferRepository.create(dto.skillId(), recommendationId);
        }
    }

    @Transactional
    public void update(RecommendationDto dto) {
        validateRecommendation(dto);
        Recommendation recommendation = recommendationRepository
                .findFirstByAuthorIdAndReceiverIdOrderByCreatedAtDesc(dto.authorId(), dto.receiverId())
                .orElseThrow(() -> new DataValidationException("Recommendation not found for authorId: "
                                                               + dto.authorId() + " and receiverId: " + dto.receiverId()));
        recommendationRepository.update(recommendation.getId(), dto.content());
        skillOfferRepository.deleteAllByRecommendationId(recommendation.getId());

        for (SkillOfferDto skillOffer : dto.skillOffers()){
            if (skillRepository.findById(skillOffer.skillId()).isEmpty()){
                throw new DataValidationException("there is no skill with id:" + skillOffer.skillId());
            }
            addSkill(skillOffer, recommendation.getId(), dto.receiverId());
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

    public List<RecommendationDto> getAllUserRecommendations(Long receiverId){
        Page<Recommendation> recommendation = recommendationRepository.findAllByReceiverId(receiverId, Pageable.unpaged());
        return recommendation.stream()
                .map(this::convertToDto).toList();
    }

    public List<RecommendationDto> getAllGivenRecommendations(Long authorId){
        Page<Recommendation> recommendation = recommendationRepository.findAllByAuthorId(authorId, Pageable.unpaged());
        return recommendation.stream()
                .map(this::convertToDto).toList();
    }

    private RecommendationDto convertToDto(Recommendation recommendation) {
        List<SkillOfferDto> skillOffers = recommendation.getSkillOffers().stream()
                .map(skillOffer -> new SkillOfferDto(skillOffer.getId(), skillOffer.getSkill().getId()))
                .toList();

        return new RecommendationDto(
                recommendation.getId(),
                recommendation.getAuthor().getId(),
                recommendation.getReceiver().getId(),
                recommendation.getContent(),
                skillOffers,
                recommendation.getCreatedAt()
        );
    }




}
