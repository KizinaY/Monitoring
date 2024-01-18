package com.kizina.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMeasurementDTO {

    @Min(0)
    @NotNull
    private Integer gasValue;

    @Min(0)
    @NotNull
    private Integer hotWaterValue;

    @Min(0)
    @NotNull
    private Integer coldWaterValue;
}
