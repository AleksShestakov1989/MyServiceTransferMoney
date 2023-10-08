package ru.netology.myservicetransfermoney.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.netology.myservicetransfermoney.model.Amount;
import ru.netology.myservicetransfermoney.model.Card;
import ru.netology.myservicetransfermoney.repository.TransferRepositoryImpl;
import ru.netology.myservicetransfermoney.web.request.ConfirmOperationRequest;
import ru.netology.myservicetransfermoney.web.request.TransferRequest;
import ru.netology.myservicetransfermoney.web.response.MoneyTransferResponse;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class TransferServiceTest {
    @InjectMocks
    TransferServiceImpl transferServiceImpl;

    @Mock
    TransferRepositoryImpl transferRepository;

    public static final String OPERATION_ID = "1";
    public static final String CONFIRMATION_CODE = "0000";
    public static final TransferRequest TRANSFER_REQUEST = new TransferRequest("1111111111111111", "01/25", "123",
            "2222222222222222", new Amount(10000, "RUR"));
    public static final Card TEST_CARD_FROM = new Card("1111111111111111", "01/25", "123", new Amount(1000000, "RUR"));
    public static final Card TEST_CARD_TO = new Card("2222222222222222", "10/25", "234", new Amount(0, "RUR"));
    public static final String CARD_NUMBER_FROM = "1111111111111111";
    public static final String CARD_NUMBER_TO = "2222222222222222";


    @Test
    @DisplayName("Подтверждение операции")
    void confirmOperation_Test() {
        //given
        ConfirmOperationRequest operationRequest = new ConfirmOperationRequest(OPERATION_ID, CONFIRMATION_CODE);
        String operationId = operationRequest.getOperationId();

        String operationCodeFromRepository = CONFIRMATION_CODE;
        MoneyTransferResponse responseExpected = new MoneyTransferResponse(operationId);
        TransferServiceImpl spy = Mockito.spy(transferServiceImpl);

        //when
        when(transferRepository.getCode(operationId)).thenReturn(operationCodeFromRepository);
        Mockito.doNothing().when(spy).cardBalanceChange(operationId);

        //then
        MoneyTransferResponse responseActual = spy.confirmOperation(operationRequest);
        assertEquals(responseExpected.getOperationId(), responseActual.getOperationId());
    }

    @Test
    @DisplayName("Преобразование копеек в рубли")
    void amountValueConversion_Test() {
        Amount amount = new Amount(100000, "RUR");
        Amount amountExpected = new Amount(1000, "RUR");
        transferServiceImpl.amountValueConversion(amount);
        assertEquals(amount, amountExpected);
    }


    @ParameterizedTest
    @CsvSource({
            "1111111111111111,2222222222222222",
            "3333333333333333,4444444444444444",
            "5555555555555555,6666666666666666"
    })
    @DisplayName("Проверка номера карты отправителя и получателя")
    void cardNumberVerification_Test(String cardFromNumber, String cardToNumber) {

        assertNotNull(cardFromNumber);
        assertNotNull(cardToNumber);
        assertThat(cardFromNumber).matches("[0-9]{16}");
        assertThat(cardToNumber).matches("[0-9]{16}");
        assertThat(cardFromNumber).isNotEqualTo(cardToNumber);
    }


    @ParameterizedTest
    @CsvSource({
            "01/25",
            "02/26",
            "03/27",
            "04/28",
            "05/29"
    })
    @DisplayName("Проверка срока действия карты отправителя")
    void cardDateVerificationVersionOne_Test(String cardFromValidTill) {

        assertThat(cardFromValidTill).isNotNull();

        final String[] yearAndMonth = cardFromValidTill.split("/");
        final int enteredMonth = Integer.parseInt(yearAndMonth[0]);
        final int enteredYear = Integer.parseInt(yearAndMonth[1]) + 2000;
        assertTrue(enteredMonth <= 12);
        assertTrue(enteredMonth >= 1);
        assertTrue(enteredYear > LocalDate.now().getYear());
    }


    @ParameterizedTest
    @MethodSource("argsAmountFactory")
    @DisplayName("Проверка суммы и валюты операции")
    void transferAmountVerification_Test(Amount amount) {

        assertNotNull(amount);

        int amountValue = amount.getValue();
        String currency = amount.getCurrency();

        assertTrue(amountValue > 0);
        assertThat(currency).isEqualTo("RUR");
    }

    static Stream<Amount> argsAmountFactory() {
        return Stream.of(new Amount(1000, "RUR"),
                new Amount(10000, "RUR"),
                new Amount(20000, "RUR"),
                new Amount(300, "RUR"));
    }

    @Test
    @DisplayName("Изменение баланса на картах отправителя и получателя")
    void cardBalanceChange() {
        //given
        String operationId = OPERATION_ID;
        TransferRequest requestData = new TransferRequest("1111111111111111", "01/25",
                "123", "2222222222222222", new Amount(10000, "RUR"));
        String cardNumberFrom = CARD_NUMBER_FROM;
        String cardNumberTo = CARD_NUMBER_TO;

        Card cardFrom = TEST_CARD_FROM;
        Card cardTo = TEST_CARD_TO;
        int newBalanceCardFromExpected = 989900;
        int newBalanceCardToExpected = 10000;

        //when
        when(transferRepository.getTransferRequest(operationId)).thenReturn(requestData);
        when(transferRepository.getCard(cardNumberFrom)).thenReturn(cardFrom);
        when(transferRepository.getCard(cardNumberTo)).thenReturn(cardTo);

        //then
        transferServiceImpl.cardBalanceChange(operationId);

        assertEquals(newBalanceCardFromExpected, cardFrom.getBalanceCard().getValue());
        assertEquals(newBalanceCardToExpected, cardTo.getBalanceCard().getValue());

    }
}
