package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.dto.RequestFilterDto;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.entity.recommendation.SkillOffer;
import school.faang.user_service.entity.recommendation.SkillRequest;
import school.faang.user_service.mapper.RecommendationRequestMapper;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.RecommendationRequestRepository;
import school.faang.user_service.repository.recommendation.SkillRequestRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationRequsetService {
    private final RecommendationRequestRepository recommendationRequestRepository;
    private final SkillRequestRepository skillRequestRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final RecommendationRequestMapper recommendationRequestMapper;

    public RecommendationRequestDto create(RecommendationRequestDto requestDto) {

        User requester = userRepository.findById(requestDto.requesterId())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));
        User receiver = userRepository.findById(requestDto.receiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));


        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        boolean existsRecentRequest = recommendationRequestRepository.existsByRequesterIdAndReceiverIdAndCreatedAtAfter(
                requestDto.requesterId(), requestDto.receiverId(), LocalDate.from(sixMonthsAgo)); //  `LocalDate.from(sixMonthsAgo)` o‘rniga `sixMonthsAgo`
        if (existsRecentRequest) {
            throw new IllegalStateException("Recommendation request can only be sent once every 6 months");
        }

//        Long requestId = RecommendationRequestRepository.

        for (SkillRequest skill : requestDto.skills()) {
            if (skillRepository.findById(skill.getId()).isPresent()) {
                throw new IllegalStateException("Skill already exists");
            }
        }


        RecommendationRequest recommendationRequest = recommendationRequestMapper.toEntity(requestDto);
        recommendationRequest.setStatus(RequestStatus.PENDING);
        recommendationRequest.setCreatedAt(LocalDateTime.now());
        recommendationRequest.setUpdatedAt(LocalDateTime.now());

        RecommendationRequest savedRequest = recommendationRequestRepository.save(recommendationRequest);


        List<SkillRequest> skillRequests = requestDto.skills().stream()
                .map(skill -> {
                    SkillRequest skillRequest = new SkillRequest(); // `null` emas, default konstruktor ishlatilmoqda
                    skillRequest.setRequest(requestDto.id());
                    skillRequest.setSkill(skill.getSkill());
                    return skillRequest;
                })
                .collect(Collectors.toList());


        skillRequestRepository.saveAll(skillRequests);


        return recommendationRequestMapper.toDto(savedRequest);

    }
    public List<RecommendationRequestDto> getRequests(RequestFilterDto filter) {
        return recommendationRequestRepository.findAll().stream()
                .filter(req -> filter.status() == null || req.getStatus().equals(filter.status()))
                .filter(req -> filter.requesterId() == null || req.getRequester().equals(filter.requesterId()))
                .filter(req -> filter.receiverId() == null || req.getReceiver().equals(filter.receiverId()))
                .filter(req -> filter.createdAt() == null || !req.getCreatedAt().equals(filter.createdAt()))
                .filter(req -> filter.updatedAt() == null || !req.getUpdatedAt().equals(filter.updatedAt()))
                .map(recommendationRequestMapper::toDto)
                .collect(Collectors.toList());
    }
    public RecommendationRequestDto getRequest(long id) {
        RecommendationRequest request = recommendationRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recommendation request not found"));
        return recommendationRequestMapper.toDto(request);
    }

    public RecommendationRequestDto rejectRequest(long id, RejectionDto rejection) {
        RecommendationRequest request = recommendationRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recommendation request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Request is already processed");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(rejection.reason());
        request.setUpdatedAt(LocalDateTime.now());

        recommendationRequestRepository.save(request);

        return recommendationRequestMapper.toDto(request);
    }

}

