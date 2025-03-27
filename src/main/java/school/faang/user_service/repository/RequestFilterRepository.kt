package school.faang.user_service.repository

import org.apache.catalina.filters.RequestFilter
import org.springframework.data.jpa.repository.JpaRepository

interface RequestFilterRepository : JpaRepository<RequestFilter, Long> {
}