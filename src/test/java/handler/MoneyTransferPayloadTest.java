package handler;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class MoneyTransferPayloadTest {
    private MoneyTransferPayload payload = new MoneyTransferPayload(1, 2, 3.0, "USD");

    @Test
    public void getAccountIdFrom() {
        assertThat(payload.getAccountIdFrom()).isEqualTo(1);
    }

    @Test
    public void getAccountIdTo() {
        assertThat(payload.getAccountIdTo()).isEqualTo(2);
    }

    @Test
    public void getAmount() {
        assertThat(payload.getAmount()).isEqualTo(3.0);
    }

    @Test
    public void getCurrency() {
        assertThat(payload.getCurrency()).isEqualTo("USD");
    }
}