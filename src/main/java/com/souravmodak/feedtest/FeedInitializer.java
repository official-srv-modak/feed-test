package com.souravmodak.feedtest;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FeedInitializer {

    @Autowired
    private FeedRepository feedRepository;
    private static final AtomicLong idCounter = new AtomicLong(1); // Start ID from 1

    @PostConstruct
    public void initDatabase() {
        if (feedRepository.count() == 0) { // Check if database is empty
            List<Feed> feeds = readExcelData("src/main/resources/dummy_data_unique_images.xlsx"); // Replace with actual file path
            feedRepository.saveAll(feeds);
        }
    }

    private List<Feed> readExcelData(String filePath) {
        List<Feed> feeds = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Feed feed = new Feed(
                        idCounter.getAndIncrement(), // Generate unique ID
                        (int) row.getCell(1).getNumericCellValue(), // Age
                        row.getCell(0).getStringCellValue(), // Name
                        row.getCell(2).getNumericCellValue(), // Distance
                        row.getCell(3).getStringCellValue()  // Image URL
                );
                feeds.add(feed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feeds;
    }
}

