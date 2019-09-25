package handler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoneyTransferPayload {
    private Integer accountIdFrom;
    private Integer accountIdTo;
    private Double amount;
    private String currency;

    @JsonCreator
    public MoneyTransferPayload(@JsonProperty(value= "accountIdFrom", required = true)Integer accountIdFrom,
                                @JsonProperty(value= "accountIdTo", required = true)Integer accountIdTo,
                                @JsonProperty(value= "amount", required = true)Double amount,
                                @JsonProperty(value= "currency", required = true)String currency) {
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.amount = amount;
        this.currency = currency;
    }

    public Integer getAccountIdFrom() {
        return accountIdFrom;
    }

    public Integer getAccountIdTo() {
        return accountIdTo;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
