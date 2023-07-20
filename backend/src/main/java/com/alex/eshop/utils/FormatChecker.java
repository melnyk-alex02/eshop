package com.alex.eshop.utils;

import com.alex.eshop.exception.InvalidDataException;
import org.springframework.web.multipart.MultipartFile;

public class FormatChecker {
    public static String type = "text/csv";

    public static boolean isCsv(MultipartFile file) {
        if (!type.equals(file.getContentType())) {
            throw new InvalidDataException("Unsupported file format");
        }
        else{
            return true;
        }
    }
}