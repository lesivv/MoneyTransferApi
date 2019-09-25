package datasource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import transaction.Transaction;

import java.sql.SQLException;

import static datasource.Account.inactiveAccount;
import static datasource.Balance.inactiveBalance;
import static datasource.DbDataSource.closeDbConnection;
import static datasource.DbDataSourceTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DbDataSourceTest {
    private static DataSource dataSource;
    private Account account = new Account(111, true);
    private Balance balance = new Balance(1, 111, 110, "USD");

    @BeforeClass
    public static void setUpDataSource() throws SQLException {
        dataSource = new DbDataSource();
        createTables();
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        closeDbConnection();
    }

    @Before
    public void setUp() throws SQLException {
        truncateTables();
    }

    @Test
    public void getsAccountIfPresentInDb() throws SQLException {
        insertAccount(account);
        assertThat(dataSource.getAccount(account.getId())).isEqualTo(account);
    }

    @Test
    public void getsInactiveAccountIfNotPresentInDb() {
        assertThat(dataSource.getAccount(account.getId())).isEqualTo(inactiveAccount());
    }

    @Test
    public void getsBalanceForAccountIfPresentInDb() throws SQLException {
        insertBalances(balance);
        assertThat(dataSource.getBalanceForAccount(account.getId())).isEqualTo(balance);
    }

    @Test
    public void getsInactiveBalanceForAccountIfNotPresentInDb() {
        assertThat(dataSource.getBalanceForAccount(account.getId())).isEqualTo(inactiveBalance());
    }

    @Test
    public void savesTransaction() throws SQLException {
        insertBalances(balance);
        dataSource.saveTransaction(new Transaction(balance.getId(), balance.getId(), 1.0, "USD"));
        assertThat(selectTransaction()).isEqualTo(new Transaction(balance.getId(), balance.getId(), 1.0, "USD"));
    }

    @Test
    public void updatesBalancesAmountsAfterSavingTransaction() throws SQLException {
        Balance otherBalance = new Balance(2, 222, 11, "USD");
        insertBalances(balance, otherBalance);
        dataSource.saveTransaction(new Transaction(balance.getId(), otherBalance.getId(), 1.0, "USD"));
        assertThat(dataSource.getBalanceForAccount(111).getAmount()).isEqualTo(109);
        assertThat(dataSource.getBalanceForAccount(222).getAmount()).isEqualTo(12);
    }
}