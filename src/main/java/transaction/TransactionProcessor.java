package transaction;

import answer.Answer;
import datasource.Balance;
import datasource.DataSource;
import handler.MoneyTransferPayload;

import java.sql.SQLException;

import static answer.Answer.failureAnswer;

public class TransactionProcessor {
    private final DataSource dataSource;

    public TransactionProcessor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Answer performTransaction(Balance balanceFrom, Balance balanceTo, MoneyTransferPayload payload) {
        Transaction transaction = new Transaction(balanceFrom.getId(), balanceTo.getId(), payload.getAmount(), payload.getCurrency());
        try {
            dataSource.saveTransaction(transaction);
            return new Answer(200, "money transferred");
        } catch (SQLException e) {
            return failureAnswer("transaction failed");
        }
    }
}
