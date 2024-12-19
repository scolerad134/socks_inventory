package com.socks.mapper.impl;

import com.socks.mapper.SocksMapper;
import com.socks.model.Socks;
import com.socks.openapi.model.SocksRequestDto;
import com.socks.openapi.model.SocksResponseDto;
import org.springframework.stereotype.Component;

@Component
public class SocksMapperImpl implements SocksMapper {

    /**
     * Маппинг из SocksRequestDto в Socks.
     *
     * @param socksRequestDto входные параметры с информацией о носках
     * @return socks приход носков
     */
    @Override
    public Socks toSocks(SocksRequestDto socksRequestDto) {

        if ( socksRequestDto == null ) {
            return null;
        }

        Socks socks = new Socks();

        socks.setColor(socksRequestDto.getColor());
        socks.setCottonPart(socksRequestDto.getCottonPart());
        socks.setQuantity(socksRequestDto.getQuantity());

        return socks;
    }

    /**
     * Маппинг из SocksRequestDto в Socks.
     *
     * @param socks входные параметры с информацией о носках
     * @return socksResponseDto ответ с информацией о носках
     */
    @Override
    public SocksResponseDto toSocksResponseDto(Socks socks) {

        if ( socks == null ) {
            return null;
        }

        SocksResponseDto socksResponseDto = new SocksResponseDto();

        socksResponseDto.setId(socks.getId());
        socksResponseDto.setColor(socks.getColor());
        socksResponseDto.setCottonPart(socks.getCottonPart());
        socksResponseDto.setQuantity(socks.getQuantity());

        return socksResponseDto;
    }
}
