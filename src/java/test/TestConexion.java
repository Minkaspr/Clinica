
package test;

import java.sql.Connection;
import java.sql.SQLException;
import util.ConexionBD;

public class TestConexion {
    public static void main(String[] args) {
        ConexionBD con = new ConexionBD();
        try (Connection c = con.conexionBD()){
            System.out.println("Conexión Exitosa");
        } catch (SQLException e) {
            System.out.println("Error: "+ e.getMessage());
        }
    }
}
