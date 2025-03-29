package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.dto.SkillOfferDto;
import school.faang.user_service.entity.recommendation.Recommendation;

import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.repository.recommendation.RecommendationRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final SkillOfferRepository skillOfferRepository;


    public RecommendationDto create(Long authorId, Long receiverId, String content, List<SkillOfferDto> skillOffers) {
        validateRecommendation(authorId, receiverId, content);

        Long recommendationId = recommendationRepository.create(authorId, receiverId, content);

        List<SkillOfferDto> savedSkillOffers = skillOffers.stream()
                .map(skillOffer -> new SkillOfferDto(skillOfferRepository.create(skillOffer.skillId(), recommendationId), skillOffer.skillId()))
                .collect(Collectors.toList());

        return new RecommendationDto(recommendationId, authorId, receiverId, content, savedSkillOffers, LocalDateTime.now());
    }


    public void update(Long authorId, Long receiverId, String content, List<SkillOfferDto> skillOffers) {
        validateRecommendation(authorId, receiverId, content);

        recommendationRepository.update(authorId, receiverId, content);

        Optional<Recommendation> recommendation = recommendationRepository.findFirstByAuthorIdAndReceiverIdOrderByCreatedAtDesc(authorId, receiverId);
        recommendation.ifPresent(r -> {
            skillOfferRepository.deleteAllByRecommendationId(r.getId());

            skillOffers.forEach(skillOffer -> skillOfferRepository.create(skillOffer.skillId(), r.getId()));
        });
    }

    public Page<RecommendationDto> getRecommendationsForReceiver(Long receiverId, Pageable pageable) {
        return recommendationRepository.findAllByReceiverId(receiverId, pageable)
                .map(this::mapToDto);
    }


    public Page<RecommendationDto> getRecommendationsByAuthor(Long authorId, Pageable pageable) {
        return recommendationRepository.findAllByAuthorId(authorId, pageable)
                .map(this::mapToDto);
    }


    private void validateRecommendation(Long authorId, Long receiverId, String content) {
        if (authorId == null || receiverId == null) {
            throw new DataValidationException("Author ID va Receiver ID bo‘sh bo‘lishi mumkin emas.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new DataValidationException("Tavsiyanoma mazmuni bo‘sh bo‘lishi mumkin emas.");
        }
    }

    private RecommendationDto mapToDto(Recommendation recommendation) {
        List<SkillOfferDto> skillOffers = skillOfferRepository.findAllByUserId(recommendation.getReceiver().getId())
                .stream()
                .map(skillOffer -> new SkillOfferDto(skillOffer.getId(), skillOffer.getSkill().getId()))
                .collect(Collectors.toList());

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
