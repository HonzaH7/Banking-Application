package datasource;

import Banking.Application.DatabaseConnection;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import utils.Nothing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class DataSourceBean {

    private final DatabaseConnection databaseConnection;

    public DataSourceBean(DatabaseConnection databaseConnection){
        this.databaseConnection = databaseConnection;
    }

    public Connection getConnection(){
        return this.databaseConnection.getConnection();
    };

    public <T> T dslContext(Function<DSLContext, T> function){
        try (Connection conn = this.getConnection()) {
            DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
            return function.apply(create);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
