package ru.netology.myservicetransfermoney.exception;

public class ConfirmOperationException extends RuntimeException {

    public ConfirmOperationException(String message) {
        super(message);
    }
}
