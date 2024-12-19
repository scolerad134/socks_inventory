package com.socks.facade;

import com.socks.facade.impl.SocksFacadeImpl;
import com.socks.mapper.SocksMapper;
import com.socks.model.Socks;
import com.socks.openapi.model.SocksRequestDto;
import com.socks.openapi.model.SocksResponseDto;
import com.socks.service.SocksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocksFacadeTest {

    @Mock
    private SocksService socksService;

    @Mock
    private SocksMapper socksMapper;

    @InjectMocks
    private SocksFacadeImpl socksFacade;

    private Socks socks;
    private SocksRequestDto socksRequestDto;
    private SocksResponseDto socksResponseDto;

    @BeforeEach
    void setUp() {
        socks = new Socks(1L, "red", 50.0, 100);

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
    void addSocksIncome_ShouldReturnSocksResponseDto() {
        when(socksMapper.toSocks(socksRequestDto)).thenReturn(socks);
        when(socksService.saveSocks(socks)).thenReturn(socks);
        when(socksMapper.toSocksResponseDto(socks)).thenReturn(socksResponseDto);

        SocksResponseDto result = socksFacade.addSocksIncome(socksRequestDto);

        assertNotNull(result);
        assertEquals(socksResponseDto, result);
        verify(socksMapper, times(1)).toSocks(socksRequestDto);
        verify(socksService, times(1)).saveSocks(socks);
        verify(socksMapper, times(1)).toSocksResponseDto(socks);
    }

    @Test
    void removeSocksOutcome_ShouldReturnTrue() {
        when(socksMapper.toSocks(socksRequestDto)).thenReturn(socks);
        when(socksService.deleteSocks(socks)).thenReturn(true);

        Boolean result = socksFacade.removeSocksOutcome(socksRequestDto);

        assertTrue(result);
        verify(socksMapper, times(1)).toSocks(socksRequestDto);
        verify(socksService, times(1)).deleteSocks(socks);
    }

    @Test
    void getSocksQuantity_ShouldReturnQuantity() {
        String color = "red";
        String comparison = "equal";
        Double cottonPart = 50.0;

        when(socksService.findBySorted(color, comparison, cottonPart)).thenReturn(100);

        int result = socksFacade.getSocksQuantity(color, comparison, cottonPart);

        assertEquals(100, result);
        verify(socksService, times(1)).findBySorted(color, comparison, cottonPart);
    }

    @Test
    void updateSocks_ShouldReturnUpdatedSocksResponseDto() {
        Long id = 1L;
        when(socksMapper.toSocks(socksRequestDto)).thenReturn(socks);
        when(socksService.updateSocks(id, socks)).thenReturn(socks);
        when(socksMapper.toSocksResponseDto(socks)).thenReturn(socksResponseDto);

        SocksResponseDto result = socksFacade.updateSocks(id, socksRequestDto);

        assertNotNull(result);
        assertEquals(socksResponseDto, result);
        verify(socksMapper, times(1)).toSocks(socksRequestDto);
        verify(socksService, times(1)).updateSocks(id, socks);
        verify(socksMapper, times(1)).toSocksResponseDto(socks);
    }

    @Test
    void uploadSocksBatch_ShouldCallService() {
        MultipartFile file = mock(MultipartFile.class);

        socksFacade.uploadSocksBatch(file);

        verify(socksService, times(1)).uploadSocksBatch(file);
    }
}
