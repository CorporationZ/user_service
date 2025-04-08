package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import school.faang.user_service.client.PaymentServiceClient;
import school.faang.user_service.dto.Currency;
import school.faang.user_service.dto.PaymentRequest;
import school.faang.user_service.dto.PaymentResponse;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.premium.Premium;
import school.faang.user_service.entity.premium.PremiumPeriod;
import school.faang.user_service.repository.premium.PremiumRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PremiumService {

    private final PremiumRepository premiumRepository;
    private final UserService userService;
    private final PaymentServiceClient paymentServiceClient;
    private final Random random = new Random();

    public void buyPremium(Long userId, PremiumPeriod period) {

        User user = userService.findUserById(userId);

        if (premiumRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("User already has a premium subscription");
        }

        try {
            PaymentRequest paymentRequest = new PaymentRequest(
                    random.nextLong(10000, 99999),
                    period.getPrice(),
                    Currency.USD);

            ResponseEntity<PaymentResponse> paymentResponse = paymentServiceClient.payment(paymentRequest);

            if (paymentResponse.getStatusCode().is2xxSuccessful()) {
                PaymentResponse body = paymentResponse.getBody();

                if (body.status().equals("SUCCESS")) {
                    Premium premium = Premium.builder()
                            .user(user)
                            .startDate(LocalDateTime.now())
                            .endDate(LocalDateTime.now().plusDays(period.getDay()))
                            .build();
                    premiumRepository.save(premium);
                } else {
                    throw new RuntimeException(body.status());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Payment failed: " + e.getMessage());
        }
    }


}
