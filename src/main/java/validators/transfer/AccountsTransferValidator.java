package validators.transfer;

import answer.Answer;
import datasource.Account;
import datasource.Balance;
import handler.MoneyTransferPayload;

import static answer.Answer.failureAnswer;

public class AccountsTransferValidator extends TransferValidator {

    public AccountsTransferValidator(TransferValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public Answer validateTransferPossibility(Account accountFrom, Account accountTo, Balance balanceFrom, Balance balanceTo, MoneyTransferPayload payload) {
        if (!accountFrom.isValid() || !accountTo.isValid()){
            return failureAnswer("account doesn't exist or is inactive");
        } else if (nextValidator != null){
            return nextValidator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload);
        }
        return new Answer(1, "valid");
    }
}
