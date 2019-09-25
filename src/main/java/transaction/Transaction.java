package transaction;


import com.google.errorprone.annotations.Immutable;

import java.util.Objects;

@Immutable
public class Transaction {
    private int balanceIdFrom;
    private int balanceIdTo;
    private double amount;
    private String currency;

    public Transaction(int balanceIdFrom, int balanceIdTo, double amount, String currency) {
        this.balanceIdFrom = balanceIdFrom;
        this.balanceIdTo = balanceIdTo;
        this.amount = amount;
        this.currency = currency;
    }

    public int getBalanceIdFrom() {
        return balanceIdFrom;
    }

    public int getBalanceIdTo() {
        return balanceIdTo;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return getBalanceIdFrom() == that.getBalanceIdFrom() &&
                getBalanceIdTo() == that.getBalanceIdTo() &&
                Double.compare(that.getAmount(), getAmount()) == 0 &&
                Objects.equals(getCurrency(), that.getCurrency());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getBalanceIdFrom(), getBalanceIdTo(), getAmount(), getCurrency());
    }
}
