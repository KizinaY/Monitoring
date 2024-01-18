package com.kizina.monitoring.repository;

import com.kizina.monitoring.entity.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends PagingAndSortingRepository<Measurement, Long> {

    Page<Measurement> findAllByUserId(Long userId, Pageable pageable);
}
