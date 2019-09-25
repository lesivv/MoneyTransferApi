import answer.Answer;
import datasource.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import transaction.Transaction;

import java.sql.SQLException;

import static datasource.DbDataSource.closeDbConnection;
import static datasource.DbDataSourceTestUtils.*;
import static datasource.DbDataSourceTestUtils.selectTransaction;
import static org.assertj.core.api.Assertions.assertThat;

public class MoneyTransferIntegrationTest {
    private static MoneyTransferDriver driver;
    private static DataSource dataSource;
    private Account accountFrom = new Account(111, true);
    private Account accountTo = new Account(222, true);
    private Balance balanceFrom = new Balance(1, 111, 110, "USD");
    private Balance balanceTo =  new Balance(2, 222, 0, "USD");

    @Before
    public void setUp() throws SQLException {
        truncateTables();
    }

    @BeforeClass
    public static void setUpDb() throws SQLException {
        dataSource = new DbDataSource();
        driver = new MoneyTransferDriver();
        MoneyTransferApi api = new MoneyTransferApi(dataSource);
        createTables();
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        closeDbConnection();
    }

    @Test
    public void returnsErrorIfNumberOfParametersIsNotThree() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"amount\": 100.11,",
                "\"currency\": \"USD\"}");
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "wrong number/types of arguments"));
    }

    @Test
    public void returnsErrorIfWrongParametersNamesAreReceived() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"anotherId\": 111,",
                "\"amount\": 100.11,",
                "\"currency\": \"USD\"}");
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "wrong number/types of arguments"));
    }

    @Test
    public void returnsErrorIfWrongParametersTypesAreReceived() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": \"wet-24r-wr4\",",
                "\"anotherId\": 111,",
                "\"amount\": 100.11,",
                "\"currency\": \"USD\"}");
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "wrong number/types of arguments"));
    }

    @Test
    public void returnsErrorIfAmountIsZero() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 0,",
                "\"currency\": \"USD\"}");
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "wrong number/types of arguments"));
    }

    @Test
    public void returnsErrorIfAmountIsLessThanZero() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": -100.11,",
                "\"currency\": \"USD\"}");
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "wrong number/types of arguments"));
    }

    @Test
    public void returnsErrorIfAccountFromWasNotFound() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 100.11,",
                "\"currency\": \"USD\"}");
        insertAccount(new Account(222, true));
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "account doesn't exist or is inactive"));
    }

    @Test
    public void returnsErrorIfAccountToWasNotFound() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 100.11,",
                "\"currency\": \"USD\"}");
        insertAccount(new Account(111, true));
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "account doesn't exist or is inactive"));
    }

    @Test
    public void returnsErrorIfAccountFromIsInactive() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 100.11,",
                "\"currency\": \"USD\"}");
        insertAccount(new Account(111, false), new Account(222, true));
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "account doesn't exist or is inactive"));
    }

    @Test
    public void returnsErrorIfAccountToIsInactive() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 100.11,",
                "\"currency\": \"USD\"}");
        insertAccount(new Account(111, true), new Account(222, false));
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "account doesn't exist or is inactive"));
    }

    @Test
    public void returnsErrorIfAccountFromHasNotEnoughMoney() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 110.01,",
                "\"currency\": \"USD\"}");
        insertAccount(new Account(111, true), new Account(222, true));
        insertBalances(new Balance(1, 111, 110, "USD"),
                new Balance(2, 222, 0, "USD"));
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "not enough funds"));
    }

    @Test
    public void returnsErrorIfBalancesHaveDifferentCurrencies() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 99.01,",
                "\"currency\": \"USD\"}");
        insertAccount(new Account(111, true), new Account(222, true));
        insertBalances(new Balance(1, 111, 110, "USD"),
                new Balance(2, 222, 0, "PLN"));
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "currency conversion is not supported yet"));
    }

    @Test
    public void returnsErrorIfBalanceAndTransferAmountHaveDifferentCurrencies() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 99.01,",
                "\"currency\": \"PLN\"}");
        insertAccount(new Account(111, true), new Account(222, true));
        insertBalances(new Balance(1, 111, 110, "USD"),
                new Balance(2, 222, 0, "USD"));
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(400, "currency conversion is not supported yet"));
    }

    @Test
    public void successfulTransferAnswer() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 110.0,",
                "\"currency\": \"USD\"}");
        insertAccount(accountFrom, accountTo);
        insertBalances(balanceFrom, balanceTo);
        assertThat(driver.makeMoneyTransferRequest(payload))
                .isEqualTo(new Answer(200, "money transferred"));
    }

    @Test
    public void successfulTransactionWasCreated() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 110.0,",
                "\"currency\": \"USD\"}");
        insertAccount(accountFrom, accountTo);
        insertBalances(balanceFrom, balanceTo);
        Transaction expectedTransaction = new Transaction(balanceFrom.getId(), balanceTo.getId(), 110.0, "USD");
        driver.makeMoneyTransferRequest(payload);
        assertThat(selectTransaction()).isEqualTo(expectedTransaction);
    }

    @Test
    public void balancesWereChangedAfterSuccessfulTransaction() throws Exception {
        String payload = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 55.05,",
                "\"currency\": \"USD\"}");
        insertAccount(accountFrom, accountTo);
        insertBalances(balanceFrom, balanceTo);
        driver.makeMoneyTransferRequest(payload);
        assertThat(dataSource.getBalanceForAccount(111).getAmount()).isEqualTo(balanceFrom.getAmount() - 55.05);
        assertThat(dataSource.getBalanceForAccount(222).getAmount()).isEqualTo(balanceTo.getAmount() + 55.05);
    }
}
