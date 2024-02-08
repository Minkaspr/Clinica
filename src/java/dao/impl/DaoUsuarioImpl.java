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
    public String actualizarClave(String correoUsuario, String nuevaClave) {
        String result = null;
        String query = "UPDATE usuario SET clave = ? WHERE correo = ?";
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, nuevaClave);
            ps.setString(2, correoUsuario);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                result = "No se pudo actualizar la contraseña";
            }
        } catch (SQLException e) {
            result = "Error al actualizar la contraseña: " + e.getMessage();
        }
        return result;
    }

    @Override
    public String actualizarPerfil(UsuarioDTO usuario, String nuevoNombres, String nuevoApellidos) {
        String result = null;
        StringBuilder query = new StringBuilder();
        if ("medico".equalsIgnoreCase(usuario.getRol())) {
            query.append("UPDATE medico SET nombres = ?, apellidos = ? WHERE usuario_id = ?");
        } else if ("secretario".equalsIgnoreCase(usuario.getRol())) {
            query.append("UPDATE secretario SET nombres = ?, apellidos = ? WHERE usuario_id = ?");
        }
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setString(1, nuevoNombres);
            ps.setString(2, nuevoApellidos);
            ps.setInt(3, usuario.getUsuarioId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                result = "No se pudo actualizar el perfil";
            }
        } catch (SQLException e) {
            result = "Error al actualizar el perfil: " + e.getMessage();
        }
        return result;
    }

    @Override
    public UsuarioDTO obtenerDatosPerfil(UsuarioDTO usuario) {
        StringBuilder query = new StringBuilder();
        if ("medico".equalsIgnoreCase(usuario.getRol())) {
            query.append("SELECT nombres, apellidos FROM medico WHERE usuario_id = ?");
        } else if ("secretario".equalsIgnoreCase(usuario.getRol())) {
            query.append("SELECT nombres, apellidos FROM secretario WHERE usuario_id = ?");
        }
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setInt(1, usuario.getUsuarioId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
            }
        } catch (SQLException e) {
            mensaje = "Error al actualizar el perfil: " + e.getMessage();
        }
        return usuario;
    }

    @Override
    public String getMensaje() {
        return mensaje;
    }
}
