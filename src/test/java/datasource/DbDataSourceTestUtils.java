package datasource;

import transaction.Transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static datasource.DbDataSource.getConn;
import static datasource.DbDataSource.getStmt;

public class DbDataSourceTestUtils {
    private static Connection conn = getConn();
    private static Statement stmt = getStmt();

    public static void truncateTables() throws SQLException {
        stmt.executeUpdate("TRUNCATE TABLE account");
        stmt.executeUpdate("TRUNCATE TABLE balance");
        conn.commit();
    }

    public static void insertBalances(Balance... balances) throws SQLException {
        for (Balance balance : balances) {
            stmt.executeUpdate(String.format("INSERT INTO balance(id, account_id, amount, currency) VALUES (%d, %d, %f, '%s')",
                    balance.getId(), balance.getAccountId(), balance.getAmount(), balance.getCurrency()));
            conn.commit();
        }
    }

    public static void insertAccount(Account... accounts) throws SQLException {
        for (Account account : accounts) {
            stmt.executeUpdate(String.format("INSERT INTO account(id, active) VALUES (%d, %s)",
                    account.getId(), account.isActive()));
            conn.commit();
        }
    }

    public static Transaction selectTransaction() throws SQLException {
            ResultSet rs = stmt.executeQuery("SELECT balance_id_from, balance_id_to, amount, currency FROM transaction");
            rs.next();
            return new Transaction(rs.getInt("balance_id_from"),
                    rs.getInt("balance_id_to"),
                    rs.getDouble("amount"),
                    rs.getString("currency"));
    }

    public static void createTables() throws SQLException {
        stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE account (" +
                "id INT NOT NULL," +
                "active VARCHAR(5) NOT NULL," +
                "PRIMARY KEY (id));");
        stmt.executeUpdate("CREATE TABLE balance (" +
                "id INT NOT NULL," +
                "account_id INT NOT NULL," +
                "amount DOUBLE NOT NULL," +
                "currency VARCHAR(3) NOT NULL," +
                "PRIMARY KEY (id));");
        stmt.executeUpdate("CREATE TABLE transaction (" +
                "id INT IDENTITY," +
                "balance_id_from INT NOT NULL," +
                "balance_id_to INT NOT NULL," +
                "amount DOUBLE NOT NULL," +
                "currency VARCHAR(3) NOT NULL," +
                "timestamp TIMESTAMP DEFAULT NOW()," +
                "PRIMARY KEY (id));");
    }
}
