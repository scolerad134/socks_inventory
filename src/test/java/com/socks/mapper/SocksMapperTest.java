package com.socks.mapper;

import com.socks.mapper.impl.SocksMapperImpl;
import com.socks.model.Socks;
import com.socks.openapi.model.SocksRequestDto;
import com.socks.openapi.model.SocksResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocksMapperTest {

    private SocksMapperImpl socksMapper;

    private Socks socks;
    private SocksRequestDto socksRequestDto;

    @BeforeEach
    void setUp() {
        socksMapper = new SocksMapperImpl();

        socks = new Socks();
        socks.setId(1L);
        socks.setColor("red");
        socks.setCottonPart(50.0);
        socks.setQuantity(100);

        socksRequestDto = new SocksRequestDto()
            .color("red")
            .cottonPart(50.0)
            .quantity(100);
    }

    @Test
    void toSocks_ShouldMapSocksRequestDtoToSocks() {
        Socks result = socksMapper.toSocks(socksRequestDto);

        assertNotNull(result);
        assertEquals(socksRequestDto.getColor(), result.getColor());
        assertEquals(socksRequestDto.getCottonPart(), result.getCottonPart());
        assertEquals(socksRequestDto.getQuantity(), result.getQuantity());
    }

    @Test
    void toSocks_ShouldReturnNull_WhenInputIsNull() {
        Socks result = socksMapper.toSocks(null);
        assertNull(result);
    }

    @Test
    void toSocksResponseDto_ShouldMapSocksToSocksResponseDto() {
        SocksResponseDto result = socksMapper.toSocksResponseDto(socks);

        assertNotNull(result);
        assertEquals(socks.getId(), result.getId());
        assertEquals(socks.getColor(), result.getColor());
        assertEquals(socks.getCottonPart(), result.getCottonPart());
        assertEquals(socks.getQuantity(), result.getQuantity());
    }

    @Test
    void toSocksResponseDto_ShouldReturnNull_WhenInputIsNull() {
        SocksResponseDto result = socksMapper.toSocksResponseDto(null);
        assertNull(result);
    }
}
