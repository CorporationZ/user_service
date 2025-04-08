package school.faang.user_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.entity.premium.PremiumPeriod;
import school.faang.user_service.service.PremiumService;

@RestController
@RequestMapping("/premium")
@RequiredArgsConstructor
public class PremiumController {

    private final PremiumService premiumService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyPremium(@RequestParam Long userId, @RequestParam Integer day) {
        PremiumPeriod period = PremiumPeriod.fromDays(day);
        premiumService.buyPremium(userId, period);
        return ResponseEntity.ok("Premium purchased successfully !!!");
    }

}
