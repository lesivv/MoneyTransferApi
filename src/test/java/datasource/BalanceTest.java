package datasource;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class BalanceTest {
    private Balance balance = new Balance(1, 2, 3.0, "EUR");

    @Test
    public void getId() {
        assertThat(balance.getId()).isEqualTo(1);
    }

    @Test
    public void getAccountId() {
        assertThat(balance.getAccountId()).isEqualTo(2);
    }

    @Test
    public void getAmount() {
        assertThat(balance.getAmount()).isEqualTo(3.0);
    }

    @Test
    public void getCurrency() {
        assertThat(balance.getCurrency()).isEqualTo("EUR");
    }

    @Test
    public void inactiveBalance() {
        assertThat(Balance.inactiveBalance()).isEqualTo(new Balance(-1, -1, 0, null));
    }

    @Test
    public void isValid() {
        assertThat(balance.isValid()).isEqualTo(true);
    }

    @Test
    public void isInvalidBecauseOfId() {
        assertThat(new Balance(-1, 1, 0, "USD").isValid()).isEqualTo(false);
    }

    @Test
    public void isInvalidBecauseOfAccountId() {
        assertThat(new Balance(1, -1, 0, "USD").isValid()).isEqualTo(false);
    }

    @Test
    public void isInvalidBecauseOfAmount() {
        assertThat(new Balance(1, 1, -1, "USD").isValid()).isEqualTo(false);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Balance.class).verify();
    }
}