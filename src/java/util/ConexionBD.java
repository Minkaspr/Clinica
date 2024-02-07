
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private final String HOST = "localhost";
    private final int PORT = 3306;
    private final String DATABASE = "bd_clinica";
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
    private final String USER = "root";
    private final String PASS = "";

    public Connection conexionBD() throws SQLException {
        Connection conexion = null;
        try {
            Class.forName(DRIVER).newInstance();
            conexion = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException
                | IllegalAccessException
                | InstantiationException
                | SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return conexion;
    }
}
