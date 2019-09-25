import datasource.DbDataSource;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        MoneyTransferApi api = new MoneyTransferApi(new DbDataSource());
    }
}
