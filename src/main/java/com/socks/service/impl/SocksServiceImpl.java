package com.socks.service.impl;

import com.socks.exception.ErrorProcessingFileException;
import com.socks.exception.IncorrectDataFormatException;
import com.socks.exception.InsufficientSocksQuantityException;
import com.socks.exception.NoSuchSocksException;
import com.socks.util.parser.FileParser;
import com.socks.model.Socks;
import com.socks.repository.SocksRepository;
import com.socks.service.SocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Реализация {@link SocksService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;

    private final FileParser fileParser;

    /**
     * Создает приход.
     *
     * @param socks приход носков.
     *
     * @return созданный приход
     */
    @Override
    public Socks saveSocks(Socks socks) {
        log.debug("saveSocks - start, socks = {}", socks);
        Socks savedSocks = socksRepository.save(socks);
        log.debug("saveSocks - end, with socks = {}", savedSocks);
        return savedSocks;
    }

    /**
     * Создает отпуск.
     *
     * @param socks отпуск носков.
     *
     * @return подтверждение отпуска
     */
    @Override
    public Boolean deleteSocks(Socks socks) {

        log.debug("deleteSocks - start, socks = {}", socks);

        Optional<Socks> socksOptional = socksRepository.findByColorAndCottonPart(socks.getColor(), socks.getCottonPart());

        if (socksOptional.isPresent()) {

            Socks existSocks = socksOptional.orElse(null);

            if (existSocks.getQuantity() < socks.getQuantity()) {
                throw new InsufficientSocksQuantityException("Нехватка носков на складе");
            }

            existSocks.setQuantity(existSocks.getQuantity() - socks.getQuantity());
            socksRepository.save(existSocks);

            log.debug("deleteSocks - start, socks = {}", socks);
            return true;
        }

        throw new NoSuchSocksException("Нет носков такого цвета и с таким процентным содержанием хлопка");

    }

    /**
     * Ищет отпуск, применяя сортировку.
     *
     * @param color Цвет носков
     * @param cottonPartComparison Оператор сравнения
     * @param cottonPart Процент содержания хлопка
     *
     * @return общее количество носков с примененной фильтрацией
     */
    @Override
    public int findBySorted(String color, String cottonPartComparison, Double cottonPart) {
        log.debug("findBySorted - start, color = {}, color = {}, color = {}", color, cottonPartComparison, cottonPart);
        Integer quantity = socksRepository.findSocksQuantityByFilter(color, cottonPartComparison, cottonPart);
        log.debug("findBySorted - end, color = {}, color = {}, color = {}", color, cottonPartComparison, cottonPart);
        return quantity == null ? 0 : quantity;
    }

    /**
     * Обновление данных носков.
     *
     * @param id идентификатор носков
     * @param socks измененные данные носков
     *
     * @return носки с измененными данными
     */
    @Override
    public Socks updateSocks(Long id, Socks socks) {
        log.debug("updateSocks - start, id = {}, socksDto = {}", id, socks);

        if (socksRepository.findById(id).isPresent()) {
            socks.setId(id);
            socksRepository.save(socks);

            log.debug("updateSocks - end, id = {}, socksDto = {}", id, socks);

            return socks;
        }

        log.debug("updateSocks - end, id = {}, socksDto = {}", id, socks);

        return null;
    }

    /**
     * Загружает партии носков из файла.
     *
     * @param file Загруженный файл CSV или Excel.
     */
    @Override
    public void uploadSocksBatch(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            List<Socks> socksList;

            if ("text/csv".equals(contentType)) {
                socksList = fileParser.parseCsvFile(file);
            } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
                socksList = fileParser.parseExcelFile(file);
            } else {
                throw new IncorrectDataFormatException("Некорректный формат данных: " + contentType);
            }

            socksRepository.saveAll(socksList);

        } catch (IncorrectDataFormatException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorProcessingFileException("Ошибка при обработке файла: " + file.getOriginalFilename(), e);
        }
    }

}
