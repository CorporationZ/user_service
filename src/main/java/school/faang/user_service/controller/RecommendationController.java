package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.dto.SkillOfferDto;
import school.faang.user_service.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<RecommendationDto> createRecommendation(
            @RequestParam Long authorId,
            @RequestParam Long receiverId,
            @RequestParam String content,
            @RequestBody List<SkillOfferDto> skillOffers
    ) {
        RecommendationDto recommendation = recommendationService.create(authorId, receiverId, content, skillOffers);
        return ResponseEntity.status(HttpStatus.CREATED).body(recommendation);
    }


    @PutMapping
    public ResponseEntity<?> updateRecommendation(
            @RequestParam Long authorId,
            @RequestParam Long receiverId,
            @RequestParam String content,
            @RequestBody List<SkillOfferDto> skillOffers
    ) {
        recommendationService.update(authorId, receiverId, content, skillOffers);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<Page<RecommendationDto>> getRecommendationsForReceiver(
            @PathVariable Long receiverId,
            Pageable pageable
    ) {
        Page<RecommendationDto> recommendations = recommendationService.getRecommendationsForReceiver(receiverId, pageable);
        return ResponseEntity.ok(recommendations);
    }


    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<RecommendationDto>> getRecommendationsByAuthor(
            @PathVariable Long authorId,
            Pageable pageable
    ) {
        Page<RecommendationDto> recommendations = recommendationService.getRecommendationsByAuthor(authorId, pageable);
        return ResponseEntity.ok(recommendations);
    }
}
