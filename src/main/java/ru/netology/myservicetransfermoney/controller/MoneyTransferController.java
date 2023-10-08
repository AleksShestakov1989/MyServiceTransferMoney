package ru.netology.myservicetransfermoney.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.myservicetransfermoney.service.TransferService;
import ru.netology.myservicetransfermoney.web.request.ConfirmOperationRequest;
import ru.netology.myservicetransfermoney.web.request.TransferRequest;
import ru.netology.myservicetransfermoney.web.response.MoneyTransferResponse;

@RestController
@CrossOrigin(origins = "${cross.origin.host.name}")

public class MoneyTransferController {

    private final TransferService transferService;

    @Autowired
    public MoneyTransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public MoneyTransferResponse transfer(@RequestBody TransferRequest transferRequest) {
        MoneyTransferResponse response = transferService.transfer(transferRequest);
        return response;
    }

    @PostMapping("/confirmOperation")
    public MoneyTransferResponse confirmOperation(@RequestBody ConfirmOperationRequest operationRequest) {
        MoneyTransferResponse response = transferService.confirmOperation(operationRequest);
        return response;
    }
}

