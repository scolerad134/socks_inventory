package com.socks.util.parser;

import com.socks.model.Socks;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileParser {

    public List<Socks> parseCsvFile(MultipartFile file) throws IOException {
        List<Socks> socksList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] columns = line.split(",");
                Socks socks = new Socks();
                socks.setColor(columns[0].trim());
                socks.setCottonPart(Double.parseDouble(columns[1].trim()));
                socks.setQuantity(Integer.parseInt(columns[2].trim()));
                socksList.add(socks);
            }
        }
        return socksList;
    }

    public List<Socks> parseExcelFile(MultipartFile file) throws IOException {
        List<Socks> socksList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;

            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                Socks socks = new Socks();
                socks.setColor(getStringCellValue(row.getCell(0)));

                socks.setCottonPart(getNumericCellValue(row.getCell(1)));

                socks.setQuantity((int) getNumericCellValue(row.getCell(2)));

                socksList.add(socks);
            }
        }
        return socksList;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        }
        return null;
    }

    private double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Невозможно преобразовать значение ячейки в число: " + cell.getStringCellValue());
            }
        }
        return 0;
    }

}
