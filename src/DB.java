import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    //Estabelece a coneão com o banco SQLite
    public static Connection connect() throws Exception{
        String url = "jdbc:sqlite:users.db";
        return DriverManager.getConnection(url);
    }
}
