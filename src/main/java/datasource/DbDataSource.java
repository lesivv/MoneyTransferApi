package datasource;

import transaction.Transaction;

import java.sql.*;

import static datasource.Account.inactiveAccount;
import static datasource.Balance.inactiveBalance;

public class DbDataSource implements DataSource {

    private static Connection conn;
    private static Statement stmt;

    public DbDataSource() throws SQLException {
        conn = DriverManager.getConnection("jdbc:hsqldb:mem:balancesDb", "user", "pass");
        stmt = conn.createStatement();
    }

    public static void closeDbConnection() throws SQLException {
        conn.close();
    }

    static Connection getConn() {
        return conn;
    }

    static Statement getStmt() {
        return stmt;
    }

    @Override
    public Account getAccount(int id) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT id, active FROM account WHERE id = " + id);
            if (!rs.next()) return inactiveAccount();
            return new Account(rs.getInt("id"), rs.getBoolean("active"));
        } catch (SQLException e) {
            return inactiveAccount();
        }
    }

    @Override
    public Balance getBalanceForAccount(int accountId) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT id, account_id, amount, currency FROM balance WHERE account_id = " + accountId);
            if (!rs.next()) return inactiveBalance();
            return new Balance(rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getDouble("amount"),
                    rs.getString("currency"));
        } catch (SQLException e) {
            return inactiveBalance();
        }
    }

    @Override
    public void saveTransaction(Transaction transaction) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO transaction(balance_id_from, balance_id_to, amount, currency) " +
                        "VALUES (%d, %d, %f, '%s')",
                transaction.getBalanceIdFrom(),
                transaction.getBalanceIdTo(),
                transaction.getAmount(),
                transaction.getCurrency()));
        conn.commit();
        updateBalanceAfterTransaction(transaction.getBalanceIdFrom(), -transaction.getAmount());
        updateBalanceAfterTransaction(transaction.getBalanceIdTo(), transaction.getAmount());
    }

    private void updateBalanceAfterTransaction(int balanceId, double amount) throws SQLException {
        stmt.executeUpdate(String.format("UPDATE balance " +
                        "SET amount = amount + %f " +
                        "WHERE id = %d ",
                amount, balanceId));
        conn.commit();
    }
}
