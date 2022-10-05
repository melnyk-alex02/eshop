package com.alex.eshop.eshop.ExceptionHandling;

public class NoSuchCategoryException extends RuntimeException{
    public NoSuchCategoryException(String message){
        super(message);
    }
}
