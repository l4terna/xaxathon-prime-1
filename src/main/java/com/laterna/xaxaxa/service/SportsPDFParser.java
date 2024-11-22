package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.dto.SportCategoryDto;
import com.laterna.xaxaxa.dto.SportEventDto;
import com.laterna.xaxaxa.util.SSLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class SportsPDFParser {

    public Map<SportCategoryDto, List<SportEventDto>> parse(String pdfUrl) {
        Map<SportCategoryDto, List<SportEventDto>> categoryEventsMap = new LinkedHashMap<>();
        File pdfFile = null;

        try {
            System.out.println("Начинаем загрузку PDF файла...");
            pdfFile = downloadPDF(pdfUrl);
            System.out.println("Начинаем парсинг PDF файла...");

            try (PDDocument document = PDDocument.load(pdfFile)) {
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(true);
                stripper.setStartPage(1);
                stripper.setEndPage(document.getNumberOfPages());

                String text = stripper.getText(document);
                String[] lines = text.split("\n");

                SportCategoryDto currentCategory = null;
                String previousLine = "";

                String currentEventFirstLine = null;
                String currentEventSecondLine = null;
                List<String> descriptionLines = new ArrayList<>();
                boolean isCollectingEvent = false;
                int lineCounter = 0;

                for (String line : lines) {
                    line = line.trim();

                    if (line.isEmpty()) {
                        continue;
                    }

                    // Определение новой категории
                    if (line.equals("Основной состав")) {
                        if (!previousLine.isEmpty()) {
                            currentCategory = SportCategoryDto.builder()
                                    .name(previousLine.trim())
                                    .build();

                            categoryEventsMap.putIfAbsent(currentCategory, new ArrayList<>());
                        }
                        continue;
                    }

                    // Определение начала нового события
                    if (line.matches("^\\d+\\s+.*")) {
                        if (currentEventFirstLine != null) {
                            // Обработка предыдущего события
                            SportEventDto event = parseEvent(currentEventFirstLine, currentEventSecondLine, descriptionLines);
                            if (event != null && currentCategory != null) {
                                categoryEventsMap.get(currentCategory).add(event);
                            }
                        }

                        // Начинаем новое событие
                        currentEventFirstLine = line;
                        currentEventSecondLine = null;
                        descriptionLines = new ArrayList<>();
                        isCollectingEvent = true;
                        lineCounter = 0;
                    } else {
                        if (isCollectingEvent && lineCounter == 0 && !line.equals("Основной состав") && !line.startsWith("Стр.")) {
                            currentEventSecondLine = line;
                            lineCounter++;
                        } else {
                            // Собираем дополнительные строки в descriptionLines
                            descriptionLines.add(line);
                        }
                    }

                    previousLine = line;
                }

                // Обработка последнего события
                if (currentEventFirstLine != null) {
                    SportEventDto event = parseEvent(currentEventFirstLine, currentEventSecondLine, descriptionLines);
                    if (event != null && currentCategory != null) {
                        categoryEventsMap.get(currentCategory).add(event);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (pdfFile != null && pdfFile.exists()) {
                pdfFile.delete();
                System.out.println("\nВременные файлы удалены");
            }
        }

        return categoryEventsMap;
    }

    private File downloadPDF(String urlString) throws IOException {
        SSLUtil.disableSSLVerification();

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM
                || status == HttpURLConnection.HTTP_SEE_OTHER) {

            String newUrl = connection.getHeaderField("Location");
            connection = (HttpURLConnection) new URL(newUrl).openConnection();
        }

        Path tempFile = Files.createTempFile("downloaded_pdf_", ".pdf");

        try (InputStream in = connection.getInputStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile.toFile();
    }

    private SportEventDto parseEvent(String firstLine, String secondLine, List<String> descriptionLines) {
        EventData eventData = parseTwoLines(firstLine, secondLine, descriptionLines);
        if (eventData != null) {
            return SportEventDto.builder()
                    .id(eventData.id)
                    .name(eventData.name)
                    .dateStart(eventData.dateStart)
                    .dateEnd(eventData.dateEnd)
                    .location(eventData.location)
                    .participants(eventData.participants)
                    .description(eventData.description)
                    .build();
        }
        return null;
    }

    private static class EventData {
        String id;
        String name;
        String location;
        String dateStart;
        String dateEnd;
        int participants;
        String description;
    }

    private EventData parseTwoLines(String firstLine, String secondLine, List<String> descriptionLines) {
        EventData result = new EventData();

        String[] parts = firstLine.split("\\s+");
        if (parts.length < 4) return null;

        result.id = parts[0];

        int dateIndex = -1;
        int participantsIndex = -1;

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                dateIndex = i;
                break;
            }
        }

        for (int i = parts.length - 1; i >= 0; i--) {
            if (parts[i].matches("\\d+")) {
                participantsIndex = i;
                break;
            }
        }

        if (dateIndex == -1) {
            System.err.println("Ошибка: Дата не найдена в строке: " + firstLine);
            return null;
        }

        // Отмечаем использованные части
        boolean[] assigned = new boolean[parts.length];
        assigned[0] = true; // id

        // Имя
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 1; i < dateIndex; i++) {
            nameBuilder.append(parts[i]).append(" ");
            assigned[i] = true;
        }
        result.name = nameBuilder.toString().trim();

        // Дата начала
        result.dateStart = parts[dateIndex];
        assigned[dateIndex] = true;

        // Количество участников
        if (participantsIndex != -1 && participantsIndex != dateIndex) {
            try {
                result.participants = Integer.parseInt(parts[participantsIndex]);
                assigned[participantsIndex] = true;
            } catch (NumberFormatException e) {
                System.err.println("Ошибка парсинга количества участников: " + parts[participantsIndex]);
                result.participants = 0;
            }
        } else {
            result.participants = 0;
        }

        // Сбор оставшейся информации в description
        StringBuilder descriptionBuilder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (!assigned[i]) {
                descriptionBuilder.append(parts[i]).append(" ");
            }
        }

        // Обработка второй строки
        if (secondLine != null && !secondLine.trim().isEmpty()) {
            String[] secondParts = secondLine.trim().split("\\s+");
            boolean[] secondAssigned = new boolean[secondParts.length];

            for (int i = 0; i < secondParts.length; i++) {
                if (secondParts[i].matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                    result.dateEnd = secondParts[i];
                    secondAssigned[i] = true;

                    // Место проведения
                    StringBuilder locationBuilder = new StringBuilder();
                    for (int j = i + 1; j < secondParts.length; j++) {
                        locationBuilder.append(secondParts[j]).append(" ");
                        secondAssigned[j] = true;
                    }
                    result.location = locationBuilder.toString().trim();
                    break;
                }
            }

            // Сбор оставшейся информации из второй строки
            for (int i = 0; i < secondParts.length; i++) {
                if (!secondAssigned[i]) {
                    descriptionBuilder.append(secondParts[i]).append(" ");
                }
            }
        }

        // Добавление дополнительных строк в описание
        for (String descLine : descriptionLines) {
            descriptionBuilder.append(descLine).append(" ");
        }

        result.description = descriptionBuilder.toString().trim();

        return result;
    }
}
