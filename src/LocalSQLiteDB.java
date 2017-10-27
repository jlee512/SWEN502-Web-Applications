import java.io.File;

/**
 * Created by Julian on 18/09/2017.
 */

//Concrete implementation of AbstractDB Class. Instances of this class are to be used in command-line query tool.
public class LocalSQLiteDB extends AbstractDB {

    public LocalSQLiteDB(String jdbcName, String filepath) {
        super(jdbcName, filepath);
    }

    public static LocalSQLiteDB getDatabaseObject() {
        //Database object to generate connections
        File database = new File("public_transport.sqlite");
        return new LocalSQLiteDB("sqlite", database.getAbsolutePath());
    }


}
