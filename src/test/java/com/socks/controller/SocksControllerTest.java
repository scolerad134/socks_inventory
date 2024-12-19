package com.socks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socks.facade.SocksFacade;
import com.socks.openapi.model.SocksRequestDto;
import com.socks.openapi.model.SocksResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SocksController.class)
class SocksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SocksFacade socksFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private SocksRequestDto socksRequestDto;
    private SocksResponseDto socksResponseDto;

    @BeforeEach
    void setUp() {
        socksRequestDto = new SocksRequestDto()
            .color("red")
            .cottonPart(50.0)
            .quantity(100);

        socksResponseDto = new SocksResponseDto();
        socksResponseDto.setId(1L);
        socksResponseDto.setColor("red");
        socksResponseDto.setCottonPart(50.0);
        socksResponseDto.setQuantity(100);
    }

    @Test
    void addSocksIncome_ShouldReturnCreatedStatus() throws Exception {
        when(socksFacade.addSocksIncome(any(SocksRequestDto.class))).thenReturn(socksResponseDto);

        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.color").value("red"))
            .andExpect(jsonPath("$.cottonPart").value(50.0))
            .andExpect(jsonPath("$.quantity").value(100));

        verify(socksFacade, times(1)).addSocksIncome(any(SocksRequestDto.class));
    }

    @Test
    void removeSocksOutcome_ShouldReturnOkWhenSuccessful() throws Exception {
        when(socksFacade.removeSocksOutcome(any(SocksRequestDto.class))).thenReturn(true);

        mockMvc.perform(post("/api/socks/outcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksRequestDto)))
            .andExpect(status().isOk())
            .andExpect(content().string("The batch of socks was written off successfully"));

        verify(socksFacade, times(1)).removeSocksOutcome(any(SocksRequestDto.class));
    }

    @Test
    void getSocksQuantity_ShouldReturnQuantity() throws Exception {
        when(socksFacade.getSocksQuantity("red", "equal", 50.0)).thenReturn(100);

        mockMvc.perform(get("/api/socks")
                .param("color", "red")
                .param("cottonPartComparison", "equal")
                .param("cottonPart", "50"))
            .andExpect(status().isOk())
            .andExpect(content().string("100"));

        verify(socksFacade, times(1)).getSocksQuantity("red", "equal", 50.0);
    }

    @Test
    void updateSocks_ShouldReturnUpdatedSocksResponseDto() throws Exception {
        when(socksFacade.updateSocks(eq(1L), any(SocksRequestDto.class))).thenReturn(socksResponseDto);

        mockMvc.perform(put("/api/socks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.color").value("red"))
            .andExpect(jsonPath("$.cottonPart").value(50.0))
            .andExpect(jsonPath("$.quantity").value(100));

        verify(socksFacade, times(1)).updateSocks(eq(1L), any(SocksRequestDto.class));
    }

    @Test
    void uploadSocksBatch_ShouldReturnSuccessMessage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "socks.csv", "text/csv", "content".getBytes());

        mockMvc.perform(multipart("/api/socks/batch").file(file))
            .andExpect(status().isOk())
            .andExpect(content().string("Socks batch successfully uploaded"));

        verify(socksFacade, times(1)).uploadSocksBatch(any());
    }
}
