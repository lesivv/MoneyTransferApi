package validators.json;

import spark.Request;

import java.io.IOException;

public abstract class JsonValidator {
    public JsonValidator nextValidator;

    public JsonValidator(JsonValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    public abstract boolean isRequestValid(Request request) throws IOException;
}
