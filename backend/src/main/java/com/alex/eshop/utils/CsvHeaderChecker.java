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
    public static boolean checkHeaders(MultipartFile file, String[] headers) {
        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        );
             CSVParser csvParser = new CSVParser(
                     fileReader,
                     CSVFormat.DEFAULT.builder().setHeader(headers).build()
             )
        ) {
            CSVRecord csvRecord = csvParser.iterator().next();
            if (csvRecord == null) {
                throw new InvalidDataException("File is empty. Please, load another one");
            }
            if (csvRecord.size() > headers.length || csvRecord.size() < headers.length) {
                throw new InvalidDataException("File: '" + file.getOriginalFilename() +
                        "' has invalid amount of records(In file: " + csvRecord.size() +
                        ", Expected: " + headers.length + ").");
            }

            for (String expectedHeader : headers) {
                if (!csvRecord.isSet(expectedHeader)) {
                    throw new InvalidDataException("There is no header  '" + expectedHeader +
                            "' in file: " + file.getOriginalFilename() +
                            ". Please, load another file");
                }
            }

            for (int i = 0; i < csvRecord.size(); i++) {
                String header = csvRecord.get(i);
                System.out.println(header);

                if (!header.equalsIgnoreCase(headers[i])) {
                    System.out.println(headers[i]);
                    throw new InvalidDataException("Please check order of headers in file: '" + file.getOriginalFilename() + "'.");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }
}