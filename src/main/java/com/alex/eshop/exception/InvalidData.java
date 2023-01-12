package com.alex.eshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidData extends RuntimeException {
    public InvalidData(String message){
        super(message);
    }
}
