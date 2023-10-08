package ru.netology.myservicetransfermoney.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.netology.myservicetransfermoney.model.Amount;
import ru.netology.myservicetransfermoney.model.Card;
import ru.netology.myservicetransfermoney.web.request.TransferRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TransferRepositoryTest {
    @Autowired
    TransferRepositoryImpl transferRepository;

    public static final String OPERATION_ID = "1";
    public static final int ID = 1;
    public static final String CONFIRMATION_CODE = "0000";
    public static final TransferRequest TRANSFER_REQUEST = new TransferRequest("1111111122222222", "01/29", "345",
            "2222222233333333", new Amount(15000, "RUR"));
    public static final Card TEST_CARD_FROM = new Card("1111111111111111", "01/25", "123", new Amount(1000000, "RUR"));
    public static final Card TEST_CARD_TO = new Card("2222222222222222", "01/25", "345", new Amount(0, "RUR"));
    public static final String CARD_NUMBER_FROM = "1111111111111111";
    public static final String CARD_NUMBER_TO = "2222222222222222";

    @Test
    @DisplayName("Сохранение TransferRequest")
    void saveTransfer_Test() {
        String operationId = OPERATION_ID;
        TransferRequest requestExp = TRANSFER_REQUEST;
        transferRepository.saveTransfer(operationId, requestExp);

        TransferRequest requestActual = transferRepository.getTransferRequest(operationId);
        assertEquals(requestExp, requestActual);
    }

    @Test
    @DisplayName("Получение ID операции")
    void getId_Test() {
        int operationIdExpected = ID;
        int operationIdActual = transferRepository.getId();
        assertEquals(operationIdExpected, operationIdActual);
    }

    @Test
    @DisplayName("Сохранение кода подтверждения")
    void saveCode_Test() {

        String operationId = OPERATION_ID;
        String codeExpected = CONFIRMATION_CODE;
        transferRepository.saveCode(operationId, codeExpected);

        String codeActual = transferRepository.getCode(operationId);
        assertEquals(codeExpected, codeActual);
    }

    @Test
    @DisplayName("Сохранение карты")
    void saveCard_Test() {
        Card testCardFrom = TEST_CARD_FROM;
        Card testCardTo = TEST_CARD_TO;

        transferRepository.saveCard(testCardFrom, testCardTo);

        Card cardFromActual = transferRepository.getCard(CARD_NUMBER_FROM);
        Card cardToActual = transferRepository.getCard(CARD_NUMBER_TO);

        assertEquals(testCardFrom, cardFromActual);
        assertEquals(testCardTo, cardToActual);
    }
}
