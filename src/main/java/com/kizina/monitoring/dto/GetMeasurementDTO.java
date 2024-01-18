package com.kizina.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMeasurementDTO {

    private Long id;

    private Long userId;

    private Integer gasValue;

    private Integer hotWaterValue;

    private Integer coldWaterValue;

    private LocalDateTime createdAt;
}
