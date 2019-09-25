package transaction;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import transaction.Transaction;

import static org.assertj.core.api.Assertions.assertThat;


public class TransactionTest {
    private Transaction transaction = new Transaction(1, 2, 3.0, "EUR");

    @Test
    public void getBalanceIdFrom() {
        assertThat(transaction.getBalanceIdFrom()).isEqualTo(1);
    }

    @Test
    public void getBalanceIdTo() {
        assertThat(transaction.getBalanceIdTo()).isEqualTo(2);
    }

    @Test
    public void getAmount() {
        assertThat(transaction.getAmount()).isEqualTo(3.0);
    }

    @Test
    public void getCurrency() {
        assertThat(transaction.getCurrency()).isEqualTo("EUR");
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Transaction.class).verify();
    }
}