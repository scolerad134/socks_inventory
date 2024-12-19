package com.socks.mapper;

import com.socks.model.Socks;
import com.socks.openapi.model.SocksRequestDto;
import com.socks.openapi.model.SocksResponseDto;

public interface SocksMapper {

    /**
     * Маппинг из SocksRequestDto в Socks.
     *
     * @param socksRequestDto входные параметры с информацией о носках
     * @return socks приход носков
     */
    Socks toSocks(SocksRequestDto socksRequestDto);

    /**
     * Маппинг из SocksRequestDto в Socks.
     *
     * @param socks входные параметры с информацией о носках
     * @return socksResponseDto ответ с информацией о носках
     */
    SocksResponseDto toSocksResponseDto(Socks socks);
}
