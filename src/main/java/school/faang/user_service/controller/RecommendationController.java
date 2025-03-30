package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.service.RecommendationService;


@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/create")
    public ResponseEntity<?> createRecommendation(@RequestBody RecommendationDto dto) {
        Long recommendationId = recommendationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("A recommendation with id " + recommendationId + " is created");
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateRecommendation(@RequestBody RecommendationDto dto) {
        recommendationService.update(dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated");
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRecommendation(@RequestParam Long id){
        recommendationService.deleteRecommendation(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("deleted");
    }



}
