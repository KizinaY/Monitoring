package com.kizina.monitoring.mapper;

import com.kizina.monitoring.dto.CreateMeasurementDTO;
import com.kizina.monitoring.dto.GetMeasurementDTO;
import com.kizina.monitoring.dto.MeasurementResponse;
import com.kizina.monitoring.entity.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MeasurementMapper {

    public Measurement map(CreateMeasurementDTO createMeasurementDTO, long userId) {
        return Measurement.builder()
                .userId(userId)
                .gasValue(createMeasurementDTO.getGasValue())
                .hotWaterValue(createMeasurementDTO.getHotWaterValue())
                .coldWaterValue(createMeasurementDTO.getColdWaterValue())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public GetMeasurementDTO map(Measurement measurement) {
        return GetMeasurementDTO.builder()
                .id(measurement.getId())
                .userId(measurement.getUserId())
                .gasValue(measurement.getGasValue())
                .hotWaterValue(measurement.getHotWaterValue())
                .coldWaterValue(measurement.getColdWaterValue())
                .createdAt(measurement.getCreatedAt())
                .build();
    }

    public MeasurementResponse map(Page<Measurement> page) {
        List<GetMeasurementDTO> measurements = page.getContent().stream()
                .map(this::map)
                .collect(Collectors.toList());
        return MeasurementResponse.builder()
                .measurements(measurements)
                .totalMeasurements(page.getTotalElements())
                .build();
    }
}
