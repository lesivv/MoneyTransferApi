package validators.transfer;

import answer.Answer;
import datasource.Account;
import datasource.Balance;
import handler.MoneyTransferPayload;

public abstract class TransferValidator {
    public TransferValidator nextValidator;

    public TransferValidator(TransferValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    public abstract Answer validateTransferPossibility(Account accountFrom, Account accountTo,
                                                Balance balanceFrom, Balance balanceTo,
                                                MoneyTransferPayload payload);
}
