package validators.transfer;

import answer.Answer;
import datasource.Account;
import datasource.Balance;
import handler.MoneyTransferPayload;

import static answer.Answer.failureAnswer;

public class TransferCurrenciesValidator extends TransferValidator {
    public TransferCurrenciesValidator(TransferValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public Answer validateTransferPossibility(Account accountFrom, Account accountTo, Balance balanceFrom, Balance balanceTo, MoneyTransferPayload payload) {
        if (currenciesDoNotMatch(balanceFrom, balanceTo, payload)){
            return failureAnswer("currency conversion is not supported yet");
        } else if (nextValidator != null){
            return nextValidator.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload);
        }
        return new Answer(1, "valid");
    }

    private boolean currenciesDoNotMatch(Balance balanceFrom, Balance balanceTo, MoneyTransferPayload payload){
        return !balanceFrom.getCurrency().equals(balanceTo.getCurrency()) ||
                !balanceFrom.getCurrency().equals(payload.getCurrency()) ||
                !balanceTo.getCurrency().equals(payload.getCurrency());
    }
}
