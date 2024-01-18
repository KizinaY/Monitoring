package com.kizina.monitoring.service;

import com.kizina.monitoring.entity.Measurement;
import com.kizina.monitoring.entity.User;
import com.kizina.monitoring.repository.MeasurementRepository;
import com.kizina.monitoring.repository.UserRepository;
import com.kizina.monitoring.service.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {

    @InjectMocks
    private MeasurementService measurementService;
    @Mock
    private MeasurementRepository measurementRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldFindAllByUserIdWhenUserExists() {
        long userId = 1L;
        int page = 1;
        int size = 2;
        User user = User.builder()
                .id(userId)
                .build();
        LocalDateTime testTimestamp = LocalDateTime.now();
        Measurement firstMeasurement = new Measurement(1L, 1L, 1, 1, 1, testTimestamp);
        Measurement secondMeasurement = new Measurement(2L, 1L, 2, 2, 2, testTimestamp);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(measurementRepository.findAllByUserId(eq(userId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(firstMeasurement, secondMeasurement)));

        Page<Measurement> result = measurementService.findAllByUserId(userId, page, size);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(firstMeasurement, result.getContent().get(0));
        assertEquals(secondMeasurement, result.getContent().get(1));
        assertEquals(2, result.getTotalElements());
    }

    @ParameterizedTest
    @ValueSource(longs = {10L, 11L, 12L})
    void shouldThrowExceptionWhenFindAllByNotExistingUser(Long userId) {
        int page = 1;
        int size = 2;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> measurementService.findAllByUserId(userId, page, size));
        verifyNoInteractions(measurementRepository);
    }

    @Test
    public void shouldSaveMeasurementWhenUserExists() {
        long userId = 1L;
        Measurement measurement = Measurement.builder().userId(userId).build();
        User user = User.builder()
                .id(userId)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(measurementRepository.save(measurement)).thenReturn(measurement);

        Measurement savedMeasurement = measurementService.save(measurement);

        assertNotNull(savedMeasurement);
        verify(userRepository, times(1)).findById(userId);
        verify(measurementRepository, times(1)).save(measurement);
    }

    @ParameterizedTest
    @ValueSource(longs = {10L, 11L, 12L})
    public void shouldThrowExceptionWhenSaveMeasurementForNotExistingUser(Long userId) {
        Measurement measurement = Measurement.builder().userId(userId).build();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> measurementService.save(measurement));
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(measurementRepository);
    }
}