package com.kizina.monitoring.controller;

import com.kizina.monitoring.dto.CreateMeasurementDTO;
import com.kizina.monitoring.dto.GetMeasurementDTO;
import com.kizina.monitoring.dto.MeasurementResponse;
import com.kizina.monitoring.entity.Measurement;
import com.kizina.monitoring.mapper.MeasurementMapper;
import com.kizina.monitoring.service.MeasurementService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@AllArgsConstructor
@Validated
@RequestMapping("/measurements")
@RestController
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeasurementMapper measurementMapper;

    @PostMapping("/{userId}/submit")
    public GetMeasurementDTO submitMeasurement(@PathVariable Long userId,
                                               @RequestBody @Valid CreateMeasurementDTO createMeasurementDTO) {
        Measurement measurement = measurementMapper.map(createMeasurementDTO, userId);
        Measurement savedMeasurement = measurementService.save(measurement);
        return measurementMapper.map(savedMeasurement);
    }


    @GetMapping("/{userId}/history")
    public MeasurementResponse getMeasurementHistory(@PathVariable Long userId,
                                                     @RequestParam(required = false, defaultValue = "1") @Min(1) Integer page,
                                                     @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        Page<Measurement> measurements = measurementService.findAllByUserId(userId, page, size);
        return measurementMapper.map(measurements);
    }
}
