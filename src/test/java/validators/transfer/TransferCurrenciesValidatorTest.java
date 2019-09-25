package validators.transfer;

import answer.Answer;
import datasource.Account;
import datasource.Balance;
import handler.MoneyTransferPayload;
import org.junit.Test;

import static answer.Answer.failureAnswer;
import static org.assertj.core.api.Assertions.assertThat;


public class TransferCurrenciesValidatorTest {
    private TransferValidator validator = new TransferCurrenciesValidator(null);
    private Account accountFrom = new Account(1, true);
    private Account accountTo = new Account(2, true);

    @Test
    public void invalidTransferIfBalanceFromCurrencyDoesNotMatch() {
        Balance balanceFrom = new Balance(1, 2, 3.0, "USD");
        Balance balanceTo = new Balance(1, 2, 3.0, "EUR");
        MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "EUR");

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(failureAnswer("currency conversion is not supported yet"));
    }

    @Test
    public void invalidTransferIfBalanceToCurrencyDoesNotMatch() {
        Balance balanceFrom = new Balance(1, 2, 3.0, "EUR");
        Balance balanceTo = new Balance(1, 2, 3.0, "USD");
        MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "EUR");

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(failureAnswer("currency conversion is not supported yet"));
    }

    @Test
    public void invalidTransferIfPayloadCurrencyDoesNotMatch() {
        Balance balanceFrom = new Balance(1, 2, 3.0, "EUR");
        Balance balanceTo = new Balance(1, 2, 3.0, "EUR");
        MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "USD");

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(failureAnswer("currency conversion is not supported yet"));
    }

    @Test
    public void validTransferIfCurrenciesMatch() {
        Balance balanceFrom = new Balance(1, 2, 3.0, "EUR");
        Balance balanceTo = new Balance(1, 2, 3.0, "EUR");
        MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "EUR");

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(new Answer(1, "valid"));
    }
}