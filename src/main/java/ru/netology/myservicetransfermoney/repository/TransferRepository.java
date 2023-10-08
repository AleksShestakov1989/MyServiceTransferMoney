package ru.netology.myservicetransfermoney.repository;

import ru.netology.myservicetransfermoney.model.Card;
import ru.netology.myservicetransfermoney.web.request.TransferRequest;

public interface TransferRepository {

    void saveTransfer(String operationId, TransferRequest request);

    TransferRequest getTransferRequest(String operationId);

    int getId();

    void saveCode(String operationId, String confirmationCode);

    String getCode(String operationId);


    void saveCard(Card currentCardFrom, Card currentCardTo);

    Card getCard(String cardNumber);
}
