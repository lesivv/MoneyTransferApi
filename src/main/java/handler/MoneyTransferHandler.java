package handler;

import answer.Answer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import datasource.Account;
import datasource.Balance;
import datasource.DataSource;
import spark.Request;
import spark.Response;
import spark.Route;
import transaction.TransactionProcessor;
import validators.json.JsonValidator;
import validators.transfer.TransferValidator;

import java.io.IOException;
import java.io.StringWriter;

import static answer.Answer.failureAnswer;

public class MoneyTransferHandler implements Route {
    private final DataSource dataSource;
    private final JsonValidator jsonValidators;
    private final TransferValidator transferValidators;
    private final TransactionProcessor transactionProcessor;

    public MoneyTransferHandler(DataSource dataSource,
                                JsonValidator jsonValidators,
                                TransferValidator transferValidators,
                                TransactionProcessor transactionProcessor) {
        this.dataSource = dataSource;
        this.jsonValidators = jsonValidators;
        this.transferValidators = transferValidators;
        this.transactionProcessor = transactionProcessor;
    }

    @Override
    public Object handle(Request request, Response response) throws IOException {
        if (jsonValidators.isRequestValid(request)) {
            return buildResponse(response, process(jsonToData(request)));
        } else {
            return buildResponse(response, failureAnswer("wrong number/types of arguments"));
        }
    }

    private Answer process(MoneyTransferPayload payload) {
        Account accountFrom = dataSource.getAccount(payload.getAccountIdFrom());
        Account accountTo = dataSource.getAccount(payload.getAccountIdTo());
        Balance balanceFrom = dataSource.getBalanceForAccount(accountFrom.getId());
        Balance balanceTo = dataSource.getBalanceForAccount(accountTo.getId());

        Answer validationAnswer = transferValidators.validateTransferPossibility(accountFrom, accountTo, balanceFrom, balanceTo, payload);
        return validationAnswer.getStatusCode() != 1 ?
                validationAnswer :
                transactionProcessor.performTransaction(balanceFrom, balanceTo, payload);
    }

    private String buildResponse(Response response, Answer answer) throws IOException {
        response.status(answer.getStatusCode());
        response.type("application/json");
        response.body(dataToJson(answer));
        return response.body();
    }

    private String dataToJson(Object data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, data);
        return sw.toString();
    }

    private MoneyTransferPayload jsonToData(Request request) throws IOException {
        return new ObjectMapper().readValue(request.body(), MoneyTransferPayload.class);
    }
}
