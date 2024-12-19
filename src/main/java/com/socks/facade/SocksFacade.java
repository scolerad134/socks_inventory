package com.socks.facade;

import com.socks.model.Socks;
import com.socks.openapi.model.SocksRequestDto;
import com.socks.openapi.model.SocksResponseDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Фасад для работы с носками.
 */
public interface SocksFacade {

    /**
     * Регистрация прихода носков.
     *
     * @param socksRequestDto входные параметры для регистрации прихода
     *
     */
    SocksResponseDto addSocksIncome(SocksRequestDto socksRequestDto);

    /**
     * Регистрация отпуска носков.
     *
     * @param socksRequestDto входные параметры для регистрации отпуска
     *
     */
    Boolean removeSocksOutcome(SocksRequestDto socksRequestDto);

    /**
     * Получение общего количества носков с фильтрацией.
     *
     * @param color цвет носков
     * @param cottonPartComparison оператор сравнения (moreThan, lessThan, equal)
     * @param cottonPart процент содержания хлопка
     *
     */
    int getSocksQuantity(String color, String cottonPartComparison, Double cottonPart);

    /**
     * Обновление данных носков.
     *
     * @param id идентификатор носков
     * @param socksDto входные параметры для обновления данных носков
     *
     */
    SocksResponseDto updateSocks(Long id, SocksRequestDto socksDto);

    /**
     * Загрузка партий носков.
     *
     * @param file файл с данными
     *
     */
    void uploadSocksBatch(MultipartFile file);

}
