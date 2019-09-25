package validators.transfer;

import answer.Answer;
import datasource.Account;
import datasource.Balance;
import handler.MoneyTransferPayload;
import org.junit.Test;

import static answer.Answer.failureAnswer;
import static datasource.Account.inactiveAccount;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountsTransferValidatorTest {
    private TransferValidator validator = new AccountsTransferValidator(null);
    private Balance balanceFrom = new Balance(1, 2, 2.9, "EUR");
    private Balance balanceTo = new Balance(1, 2, 3.0, "EUR");
    private MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "EUR");

    @Test
    public void invalidTransferIfAccountFromIsInvalid() {
        Account accountFrom = inactiveAccount();
        Account accountTo = new Account(2, true);

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(failureAnswer("account doesn't exist or is inactive"));
    }

    @Test
    public void invalidTransferIfAccountToIsInvalid() {
        Account accountFrom = new Account(1, true);
        Account accountTo = inactiveAccount();

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(failureAnswer("account doesn't exist or is inactive"));
    }

    @Test
    public void validTransferIfBothAccountsAreValid() {
        Account accountFrom = new Account(1, true);
        Account accountTo = new Account(2, true);

        assertThat(validator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload))
                .isEqualTo(new Answer(1, "valid"));
    }
}