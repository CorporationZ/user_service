package school.faang.user_service.repository.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.faang.user_service.entity.recommendation.RecommendationRequest;

import java.time.LocalDate;
import java.util.Optional;

public interface RecommendationRequestRepository extends JpaRepository<RecommendationRequest, Long> {

    @Query(nativeQuery = true, value = """
            SELECT * FROM recommendation_request
            WHERE requester_id = ?1 AND receiver_id = ?2 AND status = 1
            ORDER BY created_at DESC
            LIMIT 1
            """)

    Optional<RecommendationRequest> findLatestPendingRequest(long requesterId, long receiverId);
    boolean existsByRequesterIdAndReceiverIdAndCreatedAtAfter(Long requesterId, Long receiverId, LocalDate createdAt);


    @Query(nativeQuery = true, value = """
            INSERT INTO recommendation (requester, receiver, message, recommendation_id)
            VALUES (?1, ?2, ?3, ?4) returning id
            """)
    Long create(long requester, long receiver, String message, Long recommenderId);

}