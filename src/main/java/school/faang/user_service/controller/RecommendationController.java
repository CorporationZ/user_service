package school.faang.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.RecommendationDto;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    @PostMapping("/create")
    public ResponseEntity<?> giveRecommendation(@RequestBody RecommendationDto recommendationDto){

    }
}
