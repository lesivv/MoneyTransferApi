package datasource;

import com.google.errorprone.annotations.Immutable;

import java.util.Objects;

@Immutable
public class Balance implements Validable {
    private int id;
    private int accountId;
    private double amount;
    private String currency;

    public Balance(int id, int accountId, double amount, String currency) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public static Balance inactiveBalance(){
        return new Balance(-1, -1, 0, null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Balance)) return false;
        Balance balance = (Balance) o;
        return getId() == balance.getId() &&
                getAccountId() == balance.getAccountId() &&
                Double.compare(balance.getAmount(), getAmount()) == 0 &&
                Objects.equals(getCurrency(), balance.getCurrency());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId(), getAccountId(), getAmount(), getCurrency());
    }

    @Override
    public boolean isValid() {
        return id != -1 && accountId != -1 && amount >= 0;
    }
}
