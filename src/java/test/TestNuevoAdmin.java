package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.ConexionBD;
import util.Seguridad;

public class TestNuevoAdmin {

    public static void main(String[] args) {
        // Datos del administrador
        String correo = "admin@admin.com"; 
        String clave = "admin";
        String rol = "administrador";
        boolean estado = true;

        // Cifra y aplica un hash a la clave
        String claveCifrada = null;
        String claveHash = null;
        try {
            claveCifrada = Seguridad.cifrar(clave);
            claveHash = Seguridad.hash(claveCifrada);
        } catch (Exception ex) {
            System.out.println("Ocurrió un error al cifrar y hashear la clave: " + ex.getMessage());
            return; // Si hay un error al cifrar o aplicar el hash, termina el método
        }

        // Consulta SQL para insertar el nuevo administrador
        String query = "INSERT INTO usuario (correo, clave, rol, estado) VALUES (?, ?, ?, ?)";

        ConexionBD conexion = new ConexionBD();
        try (Connection cn = conexion.conexionBD()) {
            // Prepara la consulta
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, correo);
            ps.setString(2, claveHash);
            ps.setString(3, rol);
            ps.setBoolean(4, estado);

            // Ejecuta la consulta
            int filasInsertadas = ps.executeUpdate();

            // Imprime el resultado
            if (filasInsertadas > 0) {
                System.out.println("El administrador se ha creado correctamente.");
            } else {
                System.out.println("No se pudo crear el administrador.");
            }
        } catch (SQLException e) {
            System.out.println("Ocurrió un error al crear el administrador: " + e.getMessage());
        }
    }
}
