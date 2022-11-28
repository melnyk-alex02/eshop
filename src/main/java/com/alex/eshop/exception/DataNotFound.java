package com.alex.eshop.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DataNotFound extends RuntimeException{
    private final static Logger logger = LoggerFactory.getLogger(DataNotFound.class);
    public DataNotFound(String message){
        super(message);
        logger.error("Exception", this);
    }
}
