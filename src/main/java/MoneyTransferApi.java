import datasource.DataSource;
import handler.MoneyTransferHandler;
import transaction.TransactionProcessor;
import validators.json.CompletePayloadValidator;
import validators.json.DeserializationValidator;
import validators.json.JsonValidator;
import validators.transfer.AccountsTransferValidator;
import validators.transfer.TransferAmountValidator;
import validators.transfer.TransferCurrenciesValidator;
import validators.transfer.TransferValidator;

import static spark.Spark.post;

public class MoneyTransferApi {

    public MoneyTransferApi(DataSource dataSource){
        post("/moneyTransfer", new MoneyTransferHandler(dataSource,
                getJsonValidatorsChain(),
                getTransferValidatorsChain(),
                new TransactionProcessor(dataSource)));
    }

    private JsonValidator getJsonValidatorsChain(){
        JsonValidator completePayloadValidator = new CompletePayloadValidator(null);
        return new DeserializationValidator(completePayloadValidator);
    }

    private TransferValidator getTransferValidatorsChain(){
        TransferValidator currenciesValidator = new TransferCurrenciesValidator(null);
        TransferAmountValidator amountValidator = new TransferAmountValidator(currenciesValidator);
        return new AccountsTransferValidator(amountValidator);
    }
}
