package com.socks.controller;

import com.socks.facade.SocksFacade;
import com.socks.openapi.api.ApiApi;
import com.socks.openapi.model.SocksRequestDto;
import com.socks.openapi.model.SocksResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер для работы с маппингом "/socks".
 */

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/socks")
public class SocksController implements ApiApi {

    private final SocksFacade socksFacade;

    @Override
    @Operation(summary = "Добавить партию носков", description = "Добавляет новую партию носков на склад с переданными данными.")
    @PostMapping("/income")
    public ResponseEntity<SocksResponseDto> addSocksIncome(
        @Valid
        @RequestBody
        @Parameter(
            description = "Объект с данными для добавления носков. Включает цвет, процент хлопка и количество.",
            required = true
        ) SocksRequestDto socksDto) {
        log.debug("POST-request, addSocksIncome - start, socks = {}", socksDto);
        SocksResponseDto socksResponseDto = socksFacade.addSocksIncome(socksDto);
        log.debug("POST-request, addSocksIncome - end, socks = {}", socksDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(socksResponseDto);
    }

    @Override
    @Operation(summary = "Списать партию носков", description = "Списывает партию носков со склада, если их хватает.")
    @PostMapping("/outcome")
    public ResponseEntity<String> removeSocksOutcome(
        @Valid
        @RequestBody
        @Parameter(
            description = "Объект с данными для списания носков. Включает цвет, процент хлопка и количество.",
            required = true
        ) SocksRequestDto socksDto) {
        log.debug("DELETE-request, removeSocksOutcome - start, socksDto = {}", socksDto);
        return socksFacade.removeSocksOutcome(socksDto) ? ResponseEntity.ok("The batch of socks was written off successfully")
            : ResponseEntity.notFound().build();
    }

    @Override
    @Operation(summary = "Получить количество носков", description = "Возвращает общее количество носков на складе с фильтрацией.")
    @GetMapping
    public ResponseEntity<Integer> getSocksQuantity(
        @RequestParam
        @Parameter(name = "color", description = "Цвет носков.", required = true, example = "red")
        String color,
        @RequestParam
        @Parameter(name = "cottonPartComparison", description = "Процент хлопка носков (оператор сравнения equal, moreThan, lessThan).", required = true, example = "equal")
        String cottonPartComparison,
        @RequestParam
        @Parameter(name = "cottonPart", description = "Значение процента хлопка.", required = true, example = "50")
        Double cottonPart
    ) {
        log.debug("GET-request, getSocksQuantity - start, color = {}, cottonPartComparison = {}, cottonPart = {}",
            color, cottonPartComparison, cottonPart);
        int quantity = socksFacade.getSocksQuantity(color, cottonPartComparison, cottonPart);
        log.debug("GET-request, getSocksQuantity - end, quantity = {}", quantity);
        return ResponseEntity.ok(quantity);
    }

    @Override
    @Operation(summary = "Обновить данные носков", description = "Позволяет изменить параметры носков (цвет, процент хлопка, количество).")
    @PutMapping("/{id}")
    public ResponseEntity<SocksResponseDto> updateSocks(
        @PathVariable
        @Parameter(name = "id", description = "Идентификатор записи носков, которую нужно обновить.", required = true)
        Long id,
        @Valid
        @RequestBody
        @Parameter(name = "socksRequestDto", description = "Объект с обновлёнными данными носков.", required = true)
        SocksRequestDto socksDto
    ) {
        log.debug("PUT-request, updateSocks - start, id = {}, socks = {}", id, socksDto);
        SocksResponseDto updated = socksFacade.updateSocks(id, socksDto);
        log.debug("PUT-request, updateSocks - end, id = {}, socks = {}", id, socksDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @Override
    @Operation(summary = "Загрузить партии носков из файла", description = "Загружает партии носков из файла формата CSV или Excel.")
    @PostMapping("/batch")
    public ResponseEntity<String> uploadSocksBatch(
        @RequestParam("file")
        @Parameter(
            description = "Файл с партиями носков (формат: CSV или Excel).",
            required = true
        ) MultipartFile file) {
        log.debug("POST-request, uploadSocksBatch - start, file name = {}", file.getOriginalFilename());
        socksFacade.uploadSocksBatch(file);
        log.debug("POST-request, uploadSocksBatch - end, file name = {}", file.getOriginalFilename());
        return ResponseEntity.ok("Socks batch successfully uploaded");
    }
}
