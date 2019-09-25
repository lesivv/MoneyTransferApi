package transaction;

import answer.Answer;
import datasource.Balance;
import datasource.DataSource;
import handler.MoneyTransferPayload;
import org.junit.Test;

import java.sql.SQLException;

import static answer.Answer.failureAnswer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TransactionProcessorTest {
    private DataSource dataSource = mock(DataSource.class);
    private TransactionProcessor processor = new TransactionProcessor(dataSource);
    private Balance balanceFrom = new Balance(1, 2, 3.0, "EUR");
    private Balance balanceTo = new Balance(3, 4, 3.0, "EUR");
    private MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "EUR");

    @Test
    public void returnsSuccessfulAnswerIfTransactionIsSaved() throws SQLException {
        doNothing().when(dataSource)
                .saveTransaction(any(Transaction.class));
        assertThat(processor.performTransaction(balanceFrom, balanceTo, payload))
                .isEqualTo(new Answer(200, "money transferred"));
    }

    @Test
    public void returnsFailureAnswerIfTransactionIsNotSaved() throws SQLException {
        doThrow(SQLException.class).when(dataSource)
                .saveTransaction(any(Transaction.class));
        assertThat(processor.performTransaction(balanceFrom, balanceTo, payload))
                .isEqualTo(failureAnswer("transaction failed"));
    }
}