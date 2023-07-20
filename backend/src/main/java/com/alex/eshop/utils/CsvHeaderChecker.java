package com.alex.eshop.utils;

import com.alex.eshop.exception.InvalidDataException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CsvHeaderChecker {
    public static void checkHeaders(MultipartFile file, String[] expectedHeaders) {
        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        );
             CSVParser csvParser = new CSVParser(
                     fileReader,
                     CSVFormat.DEFAULT
             )
        ) {
            CSVRecord csvRecord = csvParser.iterator().next();
            if (csvRecord == null) {
                throw new InvalidDataException("File is empty. Please, load another one");
            }
            if (csvRecord.size() > expectedHeaders.length || csvRecord.size() < expectedHeaders.length) {
                throw new InvalidDataException("File: '" + file.getOriginalFilename() +
                        "' has invalid amount of records(In file: " + csvRecord.size() +
                        ", Expected: " +
                        expectedHeaders.length + ").");
            }

            for (int i = 0; i < csvRecord.size(); i++) {
                String header = csvRecord.get(i);

                if (!header.equalsIgnoreCase(expectedHeaders[i])) {
                    throw new InvalidDataException("Please, check presence or/and order of headers in file: '" + file.getOriginalFilename() + "'.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}