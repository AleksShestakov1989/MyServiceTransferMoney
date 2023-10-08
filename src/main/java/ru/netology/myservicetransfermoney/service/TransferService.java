package ru.netology.myservicetransfermoney.service;

import org.springframework.stereotype.Service;
import ru.netology.myservicetransfermoney.web.request.ConfirmOperationRequest;
import ru.netology.myservicetransfermoney.web.request.TransferRequest;
import ru.netology.myservicetransfermoney.web.response.MoneyTransferResponse;

@Service
public interface TransferService {

    MoneyTransferResponse transfer(TransferRequest transferRequest);

    MoneyTransferResponse confirmOperation(ConfirmOperationRequest operationRequest);
}
