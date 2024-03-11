package datasource;

import Banking.Application.DatabaseConnection;
import io.vavr.NotImplementedError;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;

public class DataSourceBean {


    public Connection getConnection(){
        return DatabaseConnection.getConnection();
    };

    public DSLContext getDSLContext(Connection connection, SQLDialect sqlDialect){
        return DSL.using(connection, sqlDialect);
    }
}
