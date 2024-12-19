package com.socks.service;

import com.socks.exception.IncorrectDataFormatException;
import com.socks.exception.InsufficientSocksQuantityException;
import com.socks.model.Socks;
import com.socks.repository.SocksRepository;
import com.socks.service.impl.SocksServiceImpl;
import com.socks.util.parser.FileParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocksServiceTest {

    @Mock
    private SocksRepository socksRepository;

    @Mock
    private FileParser fileParser;

    @InjectMocks
    private SocksServiceImpl socksService;

    private Socks socks;

    @BeforeEach
    void setUp() {
        socks = new Socks();
        socks.setId(1L);
        socks.setColor("red");
        socks.setCottonPart(50.0);
        socks.setQuantity(100);
    }

    @Test
    void saveSocks_ShouldSaveSocks() {
        when(socksRepository.save(socks)).thenReturn(socks);

        Socks savedSocks = socksService.saveSocks(socks);

        assertNotNull(savedSocks);
        assertEquals(socks.getId(), savedSocks.getId());
        verify(socksRepository, times(1)).save(socks);
    }

    @Test
    void deleteSocks_ShouldDecreaseQuantity_WhenSufficientStockExists() {
        Socks existingSocks = new Socks(1L, "red", 50.0, 200);
        when(socksRepository.findByColorAndCottonPart(socks.getColor(), socks.getCottonPart()))
            .thenReturn(Optional.of(existingSocks));

        boolean result = socksService.deleteSocks(socks);

        assertTrue(result);
        assertEquals(100, existingSocks.getQuantity());
        verify(socksRepository, times(1)).save(existingSocks);
    }

    @Test
    void deleteSocks_ShouldThrowException_WhenInsufficientStock() {
        Socks existingSocks = new Socks(1L, "red", 50.0, 50);
        when(socksRepository.findByColorAndCottonPart(socks.getColor(), socks.getCottonPart()))
            .thenReturn(Optional.of(existingSocks));

        assertThrows(InsufficientSocksQuantityException.class, () -> socksService.deleteSocks(socks));
        verify(socksRepository, never()).save(any());
    }

    @Test
    void findBySorted_ShouldReturnQuantity() {
        String color = "red";
        String comparison = "equal";
        Double cottonPart = 50.0;
        when(socksRepository.findSocksQuantityByFilter(color, comparison, cottonPart))
            .thenReturn(100);

        int quantity = socksService.findBySorted(color, comparison, cottonPart);

        assertEquals(100, quantity);
        verify(socksRepository, times(1)).findSocksQuantityByFilter(color, comparison, cottonPart);
    }

    @Test
    void updateSocks_ShouldUpdateSocks_WhenExists() {
        when(socksRepository.findById(socks.getId())).thenReturn(Optional.of(socks));
        when(socksRepository.save(socks)).thenReturn(socks);

        Socks updatedSocks = socksService.updateSocks(socks.getId(), socks);

        assertNotNull(updatedSocks);
        assertEquals(socks.getId(), updatedSocks.getId());
        verify(socksRepository, times(1)).save(socks);
    }

    @Test
    void updateSocks_ShouldReturnNull_WhenNotExists() {
        when(socksRepository.findById(socks.getId())).thenReturn(Optional.empty());

        Socks result = socksService.updateSocks(socks.getId(), socks);

        assertNull(result);
        verify(socksRepository, never()).save(any());
    }

    @Test
    void uploadSocksBatch_ShouldProcessCsvFile() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        List<Socks> socksList = List.of(socks);
        when(file.getContentType()).thenReturn("text/csv");
        when(fileParser.parseCsvFile(file)).thenReturn(socksList);

        socksService.uploadSocksBatch(file);

        verify(fileParser, times(1)).parseCsvFile(file);
        verify(socksRepository, times(1)).saveAll(socksList);
    }

    @Test
    void uploadSocksBatch_ShouldProcessExcelFile() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        List<Socks> socksList = List.of(socks);
        when(file.getContentType()).thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        when(fileParser.parseExcelFile(file)).thenReturn(socksList);

        socksService.uploadSocksBatch(file);

        verify(fileParser, times(1)).parseExcelFile(file);
        verify(socksRepository, times(1)).saveAll(socksList);
    }

    @Test
    void uploadSocksBatch_ShouldThrowException_WhenUnsupportedFileFormat() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");

        assertThrows(IncorrectDataFormatException.class, () -> socksService.uploadSocksBatch(file));
        verify(fileParser, never()).parseCsvFile(any());
        verify(fileParser, never()).parseExcelFile(any());
        verify(socksRepository, never()).saveAll(any());
    }
}
