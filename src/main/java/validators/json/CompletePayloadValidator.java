package validators.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import handler.MoneyTransferPayload;
import spark.Request;

import java.io.IOException;

public class CompletePayloadValidator extends JsonValidator {
    public CompletePayloadValidator(JsonValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public boolean isRequestValid(Request request) throws IOException {
        MoneyTransferPayload payload = new ObjectMapper().readValue(request.body(), MoneyTransferPayload.class);
        if (nullFieldsInPayload(payload)) {
            return false;
        } else if (nextValidator != null) {
            return nextValidator.isRequestValid(request);
        }
        return true;
    }

    private boolean nullFieldsInPayload(MoneyTransferPayload payload) {
        return payload.getAccountIdFrom() == null ||
                payload.getAccountIdTo() == null ||
                payload.getAmount() == null ||
                payload.getAmount() <= 0 ||
                payload.getCurrency() == null;
    }
}
