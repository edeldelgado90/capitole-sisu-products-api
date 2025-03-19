package com.capitole.sisu.products.adapter.out.repository;

import com.capitole.sisu.products.adapter.out.model.CurrentPriceEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface CurrentPriceRepository extends ReactiveCrudRepository<CurrentPriceEntity, Long> {
    @Query("""
                SELECT p.*
                FROM prices p
                WHERE p.brand_id = :brandId
                  AND p.product_id = :productId
                  AND p.start_date <= :date
                  AND p.end_date >= :date
                ORDER BY p.priority DESC
                LIMIT 1
            """)
    Mono<CurrentPriceEntity> findByProductIdAndBrandIdAndDate(Long productId, Long brandId, LocalDateTime date);
}
