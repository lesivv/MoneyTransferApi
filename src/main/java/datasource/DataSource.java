package datasource;

import transaction.Transaction;

import java.sql.SQLException;

public interface DataSource {

    Account getAccount(int id);

    Balance getBalanceForAccount(int accountId);

    void saveTransaction(Transaction transaction) throws SQLException;
}
