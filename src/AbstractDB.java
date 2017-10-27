/**
 * Created by Julian on 18/09/2017.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*Setup abstract DB class to store JDBC connection. LocalSQLite concrete-child class will extend this class - created during laboratory. Can be modified to be compatible with other database types.*/

/*NOTE: */
public class AbstractDB {

    protected final String jdbcName;
    protected final String filepath;

//    Constructor for the AbstractDB object
    public AbstractDB(String jdbcName, String filepath) {
        this.jdbcName = jdbcName;
        this.filepath = filepath;
    }

//    Method which returns the connection string used in the connection() method
    public String connectionString() {
        return "jdbc:" + this.jdbcName + ":" + this.filepath;
    }

//    Connection method which returns a connection object on which queries can be executed
    public Connection connection() throws ClassNotFoundException, SQLException {
        Connection c = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection(connectionString());
//        System.out.println("Connection to Wellington Public Transport Database has been established");

        return c;
    }
}
