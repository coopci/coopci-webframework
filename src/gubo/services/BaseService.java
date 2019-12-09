package gubo.services;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public interface BaseService {

    DataSource getDatasource();

    default Connection getConnection() throws SQLException {
        Connection dbconn = this.getDatasource().getConnection();
        dbconn.setReadOnly(false);
        return dbconn;
    }

    void setDatasource(DataSource datasource);
}
