package validators.json;

import org.junit.Test;
import spark.Request;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeserializationValidatorTest {
    private DeserializationValidator validator = new DeserializationValidator(null);
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
    public void returnsTrueIfFieldsAreMissing() throws IOException {
        String body = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"currency\": \"USD\"}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isTrue();
    }

    @Test
    public void returnsTrueForEmptyJson() throws IOException {
        String body = "{}";
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isTrue();
    }

    @Test
    public void returnsFalseIfFieldHasDifferentKey() throws IOException {
        String body = String.join(" ",
                "{\"accountIdFrom\": 111,",
                "\"accountIdTo\": 222,",
                "\"amountaaaa\": 110.0,",
                "\"currency\": \"USD\"}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isFalse();
    }

    @Test
    public void returnsFalseIfFieldHasDifferentDataType() throws IOException {
        String body = String.join(" ",
                "{\"accountIdFrom\": \"USD\",",
                "\"accountIdTo\": 222,",
                "\"amount\": 110.0,",
                "\"currency\": \"USD\"}");
        when(request.body()).thenReturn(body);
        assertThat(validator.isRequestValid(request)).isFalse();
    }
}