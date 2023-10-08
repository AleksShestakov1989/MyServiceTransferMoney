package ru.netology.myservicetransfermoney.exception;

public class ErrorInputDataException extends RuntimeException {

    public ErrorInputDataException(String message) {
        super(message);
    }
}
