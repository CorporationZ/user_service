package school.faang.user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.dto.RequestFilterDto;
import school.faang.user_service.service.RecommendationRequsetService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationRequestController {

    private final RecommendationRequsetService recommendationRequsetService;

    @PostMapping("/create")
    public ResponseEntity<RecommendationRequestDto> requestRecommendation(@Valid  @RequestBody RecommendationRequestDto requestDto) {
        RecommendationRequestDto createdRequest = recommendationRequsetService.create(requestDto);
        return ResponseEntity.ok(createdRequest);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RecommendationRequestDto>> getRecommendationRequests(@RequestBody RequestFilterDto filter) {
        List<RecommendationRequestDto> requests = recommendationRequsetService.getRequests(filter);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendationRequestDto> getRecommendationRequest(@PathVariable Long id) {
        RecommendationRequestDto request = recommendationRequsetService.getRequest(id);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<RecommendationRequestDto> rejectRequest(@PathVariable Long id, @RequestBody RejectionDto rejectionDto) {
        RecommendationRequestDto updatedRequest = recommendationRequsetService.rejectRequest(id, rejectionDto);
        return ResponseEntity.ok(updatedRequest);
    }





}
