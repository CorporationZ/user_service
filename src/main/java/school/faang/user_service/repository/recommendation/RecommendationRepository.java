package school.faang.user_service.repository.recommendation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.entity.recommendation.Recommendation;

import java.util.Optional;

public interface RecommendationRepository extends CrudRepository<Recommendation, Long> {

    @Query(nativeQuery = true, value = """
            INSERT INTO recommendation (author_id, receiver_id, content)
            VALUES (?1, ?2, ?3) returning id
            """)
    Long create(long authorId, long receiverId, String content);


    @Query(nativeQuery = true, value = """
            DELETE FROM recommendation 
            WHERE id = (
                SELECT id FROM recommendation 
                WHERE author_id = ?1 
                ORDER BY id DESC 
                LIMIT 1
            ) RETURNING id
            """)
    Long deleteLastByAuthorId(long authorId);


    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
        UPDATE recommendation 
        SET content = ?2, updated_at = now()
        WHERE id = ?1
    """)
    void update(long id, String content);


    Page<Recommendation> findAllByReceiverId(long receiverId, Pageable pageable);

    Page<Recommendation> findAllByAuthorId(long authorId, Pageable pageable);

    Optional<Recommendation> findFirstByAuthorIdAndReceiverIdOrderByCreatedAtDesc(long authorId, long receiverId);
}