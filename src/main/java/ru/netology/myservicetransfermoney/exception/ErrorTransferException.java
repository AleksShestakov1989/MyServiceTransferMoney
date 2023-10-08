package ru.netology.myservicetransfermoney.exception;

public class ErrorTransferException extends RuntimeException {

    public ErrorTransferException(String message) {
        super(message);
    }
}
