package validators.json;

import org.junit.Test;
import spark.Request;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompletePayloadValidatorTest {
    private CompletePayloadValidator validator = new CompletePayloadValidator(null);
    private Request request = mock(Request.class);

    @Test
    public void returnsTrueForValidJson() throws IOException {
        String body = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 110.0,",
                "\"currency\": \"USD\"}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isTrue();
    }

    @Test
    public void returnsFalseIfAccountIdFromIsMissing() throws IOException {
        String body = String.join(" ",
                "{\"accountIdTo\": 222,",
                "\"amount\": 110.0,",
                "\"currency\": \"USD\"}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isFalse();
    }

    @Test
    public void returnsFalseIfAccountIdToIsMissing() throws IOException {
        String body = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"amount\": 110.0,",
                "\"currency\": \"USD\"}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isFalse();
    }

    @Test
    public void returnsFalseIfAmountIsMissing() throws IOException {
        String body = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"currency\": \"USD\"}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isFalse();
    }

    @Test
    public void returnsFalseIfAmountIsLessThanZero() throws IOException {
        String body = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": -0.01,",
                "\"currency\": \"USD\"}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isFalse();
    }

    @Test
    public void returnsFalseIfAmountIsZero() throws IOException {
        String body = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 0,",
                "\"currency\": \"USD\"}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isFalse();
    }

    @Test
    public void returnsFalseIfCurrencyIsMissing() throws IOException {
        String body = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amount\": 110.0}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isFalse();
    }
}