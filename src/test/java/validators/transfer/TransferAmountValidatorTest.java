package validators.transfer;

import answer.Answer;
import datasource.Account;
import datasource.Balance;
import handler.MoneyTransferPayload;
import org.junit.Test;

import static answer.Answer.failureAnswer;
import static org.assertj.core.api.Assertions.assertThat;


public class TransferAmountValidatorTest {
    private TransferValidator validator = new TransferAmountValidator(null);
    private Account accountFrom = new Account(1, true);
    private Account accountTo = new Account(2, true);

    @Test
    public void invalidTransferIfBalanceFromIsLessThanPayloadAmount() {
        Balance balanceFrom = new Balance(1, 2, 2.9, "EUR");
        Balance balanceTo = new Balance(1, 2, 3.0, "EUR");
        MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "EUR");

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(failureAnswer("not enough funds"));
    }

    @Test
    public void validTransferIfBalanceFromIsEqualToPayloadAmount() {
        Balance balanceFrom = new Balance(1, 2, 3.0, "EUR");
        Balance balanceTo = new Balance(1, 2, 3.0, "EUR");
        MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "EUR");

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(new Answer(1, "valid"));
    }

    @Test
    public void validTransferIfBalanceFromIsBiggerThanPayloadAmount() {
        Balance balanceFrom = new Balance(1, 2, 3.1, "EUR");
        Balance balanceTo = new Balance(1, 2, 3.0, "EUR");
        MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "EUR");

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(new Answer(1, "valid"));
    }
}