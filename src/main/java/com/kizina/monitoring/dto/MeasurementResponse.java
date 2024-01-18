package com.kizina.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementResponse {

    private List<GetMeasurementDTO> measurements;

    private long totalMeasurements;
}
