package dao.impl;

import dao.DaoUsuario;
import dto.UsuarioDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.ConexionBD;

public class DaoUsuarioImpl implements DaoUsuario {

    private final ConexionBD conexion;
    private String mensaje;

    public DaoUsuarioImpl() {
        this.conexion = new ConexionBD();
    }

    @Override
    public String obtenerClave(String correo) {
        String clave = null;
        StringBuilder query = new StringBuilder();
        query.append("SELECT clave ")
                .append("FROM usuario ")
                .append("WHERE correo = ?");
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                clave = rs.getString("clave");
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return clave;
    }

    @Override
    public UsuarioDTO iniciarSesion(String correo) {
        UsuarioDTO usuarioDTO = null;
        StringBuilder query = new StringBuilder();
        query.append("SELECT u.id, u.correo, u.rol, m.nombres AS nombresMedico, s.nombres AS nombresSecretario ")
                .append("FROM usuario u ")
                .append("LEFT JOIN medico m ON u.id = m.usuario_id ")
                .append("LEFT JOIN secretario s ON u.id = s.usuario_id ")
                .append("WHERE u.correo = ?");
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuarioDTO = new UsuarioDTO();
                usuarioDTO.setUsuarioId(rs.getInt("id"));
                usuarioDTO.setCorreo(rs.getString("correo"));
                usuarioDTO.setRol(rs.getString("rol"));
                // Dependiendo del rol del usuario, establece los nombres
                if ("medico".equalsIgnoreCase(usuarioDTO.getRol())) {
                    usuarioDTO.setNombres(rs.getString("nombresMedico"));
                } else if ("secretario".equalsIgnoreCase(usuarioDTO.getRol())) {
                    usuarioDTO.setNombres(rs.getString("nombresSecretario"));
                }
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return usuarioDTO;
    }

    @Override
    public String getMensaje() {
        return mensaje;
    }
}
