package com.socks.facade.impl;

import com.socks.facade.SocksFacade;
import com.socks.mapper.SocksMapper;
import com.socks.model.Socks;
import com.socks.openapi.model.SocksRequestDto;
import com.socks.openapi.model.SocksResponseDto;
import com.socks.service.SocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Реализация интерфейса SocksFacade.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SocksFacadeImpl implements SocksFacade {

    private final SocksService socksService;

    private final SocksMapper socksMapper;

    /**
     * Регистрация прихода носков.
     *
     * @param socksRequestDto входные параметры для регистрации прихода
     *
     */
    @Override
    public SocksResponseDto addSocksIncome(SocksRequestDto socksRequestDto) {
        log.debug("addSocksIncome - start, socksRequestDto = {}", socksRequestDto);
        Socks socks = socksService.saveSocks(socksMapper.toSocks(socksRequestDto));
        SocksResponseDto socksResponseDto = socksMapper.toSocksResponseDto(socks);
        log.debug("addSocksIncome - end, socksRequestDto = {}", socks);
        return socksResponseDto;
    }

    /**
     * Регистрация отпуска носков.
     *
     * @param socksRequestDto входные параметры для регистрации отпуска
     *
     */
    @Override
    public Boolean removeSocksOutcome(SocksRequestDto socksRequestDto) {
        log.debug("removeSocksOutcome - start, socksRequestDto = {}", socksRequestDto);
        return socksService.deleteSocks(socksMapper.toSocks(socksRequestDto));
    }

    /**
     * Получение общего количества носков с фильтрацией.
     *
     * @param color цвет носков
     * @param cottonPartComparison оператор сравнения (moreThan, lessThan, equal)
     * @param cottonPart процент содержания хлопка
     *
     */
    @Override
    public int getSocksQuantity(String color, String cottonPartComparison, Double cottonPart) {
        log.debug("getSocksQuantity - start, color = {}, color = {}, color = {}", color, cottonPartComparison, cottonPart);
        int quantity = socksService.findBySorted(color, cottonPartComparison, cottonPart);
        log.debug("getSocksQuantity - end, color = {}, color = {}, color = {}", color, cottonPartComparison, cottonPart);
        return quantity;
    }


    /**
     * Обновление данных носков.
     *
     * @param id идентификатор носков
     * @param socksDto входные параметры для обновления данных носков
     *
     */
    @Override
    public SocksResponseDto updateSocks(Long id, SocksRequestDto socksDto) {
        log.debug("updateSocks - start, id = {}, socksDto = {}", id, socksDto);
        Socks socks = socksService.updateSocks(id, socksMapper.toSocks(socksDto));
        SocksResponseDto socksResponseDto = socksMapper.toSocksResponseDto(socks);
        log.debug("updateSocks - end, id = {}, socksDto = {}", id, socksDto);
        return socksResponseDto;
    }

    /**
     * Загрузка партий носков.
     *
     * @param file файл с данными
     *
     */
    @Override
    public void uploadSocksBatch(MultipartFile file) {
        log.debug("uploadSocksBatch - start, file = {}", file.getOriginalFilename());
        socksService.uploadSocksBatch(file);
        log.debug("uploadSocksBatch - end, file = {}", file.getOriginalFilename());
    }

}
