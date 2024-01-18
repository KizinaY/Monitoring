package com.kizina.monitoring.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kizina.monitoring.controller.MeasurementController;
import com.kizina.monitoring.dto.CreateMeasurementDTO;
import com.kizina.monitoring.dto.GetMeasurementDTO;
import com.kizina.monitoring.dto.MeasurementResponse;
import com.kizina.monitoring.entity.Measurement;
import com.kizina.monitoring.mapper.MeasurementMapper;
import com.kizina.monitoring.service.MeasurementService;
import com.kizina.monitoring.service.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeasurementController.class)
public class MeasurementControllerContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeasurementService measurementService;

    @MockBean
    private MeasurementMapper measurementMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturn200CodeWhenSubmitMeasurementForExistingUser() throws Exception {
        long userId = 1L;
        LocalDateTime testTimestamp = LocalDateTime.of(2020, 10, 10, 10, 10, 10);
        CreateMeasurementDTO createMeasurementDTO = new CreateMeasurementDTO(10, 10, 10);
        Measurement measurementBeforeSave = new Measurement(null, userId, 10, 10, 10, testTimestamp);
        Measurement measurementAfterSave = new Measurement(1L, userId, 10, 10, 10, testTimestamp);
        GetMeasurementDTO getMeasurementDTO = new GetMeasurementDTO(1L, userId, 10, 10, 10, testTimestamp);

        when(measurementMapper.map(createMeasurementDTO, userId)).thenReturn(measurementBeforeSave);
        when(measurementService.save(measurementBeforeSave)).thenReturn(measurementAfterSave);
        when(measurementMapper.map(measurementAfterSave)).thenReturn(getMeasurementDTO);

        mockMvc.perform(post("/measurements/" + userId + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMeasurementDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.gasValue").value(10))
                .andExpect(jsonPath("$.hotWaterValue").value(10))
                .andExpect(jsonPath("$.coldWaterValue").value(10))
                .andExpect(jsonPath("$.createdAt").value("2020-10-10T10:10:10"));
    }

    @Test
    public void shouldReturn404CodeWhenSubmitMeasurementForNotExistingUser() throws Exception {
        long userId = 1L;
        LocalDateTime testTimestamp = LocalDateTime.of(2020, 10, 10, 10, 10, 10);
        CreateMeasurementDTO createMeasurementDTO = new CreateMeasurementDTO(10, 10, 10);
        Measurement measurementBeforeSave = new Measurement(null, userId, 10, 10, 10, testTimestamp);

        when(measurementMapper.map(createMeasurementDTO, userId)).thenReturn(measurementBeforeSave);
        when(measurementService.save(measurementBeforeSave)).thenThrow(new UserNotFoundException(userId));

        mockMvc.perform(post("/measurements/" + userId + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMeasurementDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorPhrase").value("Not Found"))
                .andExpect(jsonPath("$.message").value("There is no user with id: " + userId));
    }

    @Test
    public void shouldReturn200CodeWhenGetMeasurementHistoryForExistingUser() throws Exception {
        long userId = 1L;
        int page = 1;
        int size = 2;
        LocalDateTime firstTimestamp = LocalDateTime.of(2020, 10, 10, 10, 10, 10);
        LocalDateTime secondTimestamp = LocalDateTime.of(2020, 10, 10, 10, 10, 10);
        Measurement firstMeasurement = new Measurement(1L, userId, 1, 1, 1, firstTimestamp);
        Measurement secondMeasurement = new Measurement(2L, userId, 2, 2, 2, secondTimestamp);
        Page<Measurement> measurements = new PageImpl<>(Arrays.asList(firstMeasurement, secondMeasurement));
        GetMeasurementDTO firstMeasurementDTO = new GetMeasurementDTO(1L, userId, 1, 1, 1, firstTimestamp);
        GetMeasurementDTO secondMeasurementDTO = new GetMeasurementDTO(2L, userId, 2, 2, 2, secondTimestamp);
        List<GetMeasurementDTO> measurementDTOs = Arrays.asList(firstMeasurementDTO, secondMeasurementDTO);
        MeasurementResponse measurementResponse = new MeasurementResponse(measurementDTOs, 2);

        when(measurementService.findAllByUserId(userId, page, size)).thenReturn(measurements);
        when(measurementMapper.map(measurements)).thenReturn(measurementResponse);

        mockMvc.perform(get("/measurements/" + userId + "/history")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.totalMeasurements").value(2))
                .andExpect(jsonPath("$.measurements[0].id").value(1))
                .andExpect(jsonPath("$.measurements[0].userId").value(userId))
                .andExpect(jsonPath("$.measurements[1].id").value(2))
                .andExpect(jsonPath("$.measurements[1].userId").value(userId));
    }


    @Test
    public void shouldReturn404CodeWhenGetMeasurementHistoryForNotExistingUser() throws Exception {
        long userId = 1L;
        int page = 1;
        int size = 2;
        when(measurementService.findAllByUserId(userId, page, size)).thenThrow(new UserNotFoundException(userId));

        mockMvc.perform(get("/measurements/" + userId + "/history")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorPhrase").value("Not Found"))
                .andExpect(jsonPath("$.message").value("There is no user with id: " + userId));
    }
}
