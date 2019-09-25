package validators.transfer;

import answer.Answer;
import datasource.Account;
import datasource.Balance;
import handler.MoneyTransferPayload;

import static answer.Answer.failureAnswer;

public class TransferAmountValidator extends TransferValidator {
    public TransferAmountValidator(TransferValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public Answer validateTransferPossibility(Account accountFrom, Account accountTo, Balance balanceFrom, Balance balanceTo, MoneyTransferPayload payload) {
        if (balanceFrom.getAmount() < payload.getAmount()){
            return failureAnswer("not enough funds");
        } else if (nextValidator != null){
            return nextValidator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload);
        }
        return new Answer(1, "valid");
    }
}
