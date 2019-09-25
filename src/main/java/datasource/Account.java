package datasource;

import com.google.errorprone.annotations.Immutable;

import java.util.Objects;

@Immutable
public class Account implements Validable{
    private int id;
    private boolean active;

    public Account(int id, boolean active) {
        this.id = id;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public static Account inactiveAccount(){
        return new Account(-1, false);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Account)) return false;
        Account account = (Account) o;
        return getId() == account.getId() &&
                isActive() == account.isActive();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId(), isActive());
    }

    @Override
    public boolean isValid() {
        return id != -1 && active;
    }
}
