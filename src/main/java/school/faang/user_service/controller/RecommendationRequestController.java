package school.faang.user_service.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.service.RecommendationRequsetService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationRequestController {

    private final RecommendationRequsetService recommendationRequsetService;

    @PostMapping("/request")
    public ResponseEntity<RecommendationRequestDto> requestRecommendation(@RequestBody @Valid RecommendationRequestDto recommendationRequestDto) {
        RecommendationRequestDto createdRequest = recommendationRequsetService.create(recommendationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }
}
