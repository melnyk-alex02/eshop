package com.alex.eshop.utils;

import org.springframework.web.multipart.MultipartFile;

public class FormatChecker {
    public static String type = "text/csv";

    public static boolean isCsv(MultipartFile file){
        return type.equals(file.getContentType());
    }
}