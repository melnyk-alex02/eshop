package com.alex.eshop.eshop.ExceptionHandling;

public class NoSuchItemException extends RuntimeException{
    public NoSuchItemException(String message){
        super(message);
    }
}
