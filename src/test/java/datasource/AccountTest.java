package datasource;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AccountTest {
    private Account account = new Account(123, true);

    @Test
    public void getId() {
        assertThat(account.getId()).isEqualTo(123);
    }

    @Test
    public void isActive() {
        assertThat(account.isActive()).isEqualTo(true);
    }

    @Test
    public void inactiveAccount() {
        assertThat(Account.inactiveAccount()).isEqualTo(new Account(-1, false));
    }

    @Test
    public void isValid() {
        assertThat(account.isValid()).isEqualTo(true);
    }

    @Test
    public void isInvalidBecauseOfId() {
        assertThat(new Account(-1, true).isValid()).isEqualTo(false);
    }

    @Test
    public void isInvalidBecauseIsInactive() {
        assertThat(new Account(1, false).isValid()).isEqualTo(false);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Account.class).verify();
    }
}