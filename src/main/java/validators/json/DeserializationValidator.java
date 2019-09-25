package validators.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import handler.MoneyTransferPayload;
import spark.Request;

import java.io.IOException;

public class DeserializationValidator extends JsonValidator {

    public DeserializationValidator(JsonValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public boolean isRequestValid(Request request) throws IOException {
        if (!payloadIsValid(request)){
            return false;
        } else if (nextValidator != null){
            return nextValidator.isRequestValid(request);
        }
        return true;
    }

    private boolean payloadIsValid(Request request){
        try {
            new ObjectMapper().readValue(request.body(), MoneyTransferPayload.class);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
