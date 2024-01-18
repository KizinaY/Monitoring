package com.kizina.monitoring.service;

import com.kizina.monitoring.entity.Measurement;
import com.kizina.monitoring.repository.MeasurementRepository;
import com.kizina.monitoring.repository.UserRepository;
import com.kizina.monitoring.service.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MeasurementService {

    private static final String DATE_FIELD = "date";

    private final MeasurementRepository measurementRepository;
    private final UserRepository userRepository;

    public Measurement save(Measurement measurement) {
        userRepository.findById(measurement.getUserId())
                .orElseThrow(() -> new UserNotFoundException(measurement.getUserId()));
        return measurementRepository.save(measurement);
    }

    public Page<Measurement> findAllByUserId(Long userId, Integer page, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return measurementRepository.findAllByUserId(userId,
                PageRequest.of(page - 1, size, Sort.by(DATE_FIELD).descending()));
    }
}
