package com.socks.service;

import com.socks.model.Socks;
import com.socks.openapi.model.SocksRequestDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * API сервиса для работы с {@link Socks}.
 */
public interface SocksService {

    /**
     * Регистрация прихода носков.
     *
     * @param socks входные параметры для регистрации прихода
     *
     */
    Socks saveSocks(Socks socks);

    /**
     * Регистрация отпуска носков.
     *
     * @param socks входные параметры для регистрации отпуска
     *
     */
    Boolean deleteSocks(Socks socks);

    /**
     * Получение общего количества носков с фильтрацией.
     *
     * @param color цвет носков
     * @param cottonPartComparison оператор сравнения (moreThan, lessThan, equal)
     * @param cottonPart процент содержания хлопка
     *
     */
    int findBySorted(String color, String cottonPartComparison, Double cottonPart);

    /**
     * Обновление данных носков.
     *
     * @param id идентификатор носков
     * @param socksDto входные параметры для обновления данных носков
     *
     */
    Socks updateSocks(Long id, Socks socksDto);

    /**
     * Загружает партии носков из файла.
     *
     * @param file Загруженный файл CSV или Excel.
     */
    void uploadSocksBatch(MultipartFile file);
}
