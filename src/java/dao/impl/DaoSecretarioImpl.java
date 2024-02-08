package dao.impl;

import dao.DaoSecretario;
import entity.Secretario;
import entity.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import util.ConexionBD;

public class DaoSecretarioImpl implements DaoSecretario {

    public static Integer ultimoIdUsuario = null;

    private final ConexionBD conexion;
    private String mensaje;

    public DaoSecretarioImpl() {
        this.conexion = new ConexionBD();
    }

    @Override
    public List<Secretario> obtenerSecretarios() {
        List<Secretario> lista = null;
        StringBuilder query = new StringBuilder();
        query.append("SELECT ")
                .append("s.id, ")
                .append("u.correo, ")
                .append("u.rol, ")
                .append("u.estado, ")
                .append("s.nombres, ")
                .append("s.apellidos, ")
                .append("s.salario, ")
                .append("s.hora_entrada, ")
                .append("s.hora_salida ")
                .append("FROM secretario s ")
                .append("INNER JOIN usuario u ON s.usuario_id = u.id");
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ResultSet rs = ps.executeQuery();
            lista = new ArrayList<>();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt(1));
                usuario.setCorreo(rs.getString(2));
                usuario.setRol(rs.getString(3));
                usuario.setEstado(rs.getBoolean(4));

                Secretario secretario = new Secretario();
                secretario.setId(rs.getInt(1));
                secretario.setUsuario(usuario);
                secretario.setNombres(rs.getString(5));
                secretario.setApellidos(rs.getString(6));
                secretario.setSalario(rs.getDouble(7));
                secretario.setHoraEntrada(rs.getTime(8).toLocalTime());
                secretario.setHoraSalida(rs.getTime(9).toLocalTime());
                lista.add(secretario);
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }

        return lista;
    }

    @Override
    public Secretario obtenerSecretarioPorId(Integer id) {
        Secretario secretario = null;
        StringBuilder query = new StringBuilder();
        query.append("SELECT ")
                .append("s.id, ")
                .append("u.correo, ")
                .append("u.rol, ")
                .append("u.estado, ")
                .append("s.nombres, ")
                .append("s.apellidos, ")
                .append("s.salario, ")
                .append("s.hora_entrada, ")
                .append("s.hora_salida ")
                .append("FROM secretario s ")
                .append("INNER JOIN usuario u ON s.usuario_id = u.id ")
                .append("WHERE s.id = ?");
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt(1));
                usuario.setCorreo(rs.getString(2));
                usuario.setRol(rs.getString(3));
                usuario.setEstado(rs.getBoolean(4));

                secretario = new Secretario();
                secretario.setId(rs.getInt(1));
                secretario.setUsuario(usuario);
                secretario.setNombres(rs.getString(5));
                secretario.setApellidos(rs.getString(6));
                secretario.setSalario(rs.getDouble(7));
                secretario.setHoraEntrada(rs.getTime(8).toLocalTime());
                secretario.setHoraSalida(rs.getTime(9).toLocalTime());
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return secretario;
    }

    @Override
    public String agregarSecretario(Secretario secretario) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO usuario (")
                .append("correo, clave, rol, estado")
                .append(") VALUES (?, ?, ?, ?)");
        try (Connection cn = conexion.conexionBD()) {
            // Iniciar transacción
            cn.setAutoCommit(false);
            PreparedStatement ps = cn.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, secretario.getUsuario().getCorreo());
            ps.setString(2, secretario.getUsuario().getClave());
            ps.setString(3, secretario.getUsuario().getRol());
            ps.setBoolean(4, secretario.getUsuario().getEstado());

            int ctos = ps.executeUpdate();
            boolean transaccionExitosa = true;
            if (ctos == 0) {
                mensaje = "cero filas insertadas";
                transaccionExitosa = false;
            } else {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    // Obtener el ID generado para el usuario
                    int usuarioId = rs.getInt(1);
                    ultimoIdUsuario = usuarioId; // Almacena el ID del último usuario creado para MH
                    // Nueva consulta
                    query = new StringBuilder();
                    query.append("INSERT INTO secretario (")
                            .append("usuario_id, nombres, apellidos, salario, ")
                            .append("hora_entrada, hora_salida")
                            .append(") VALUES (?, ?, ?, ?, ?, ?)");
                    // Insertar en la tabla secretario
                    ps = cn.prepareStatement(query.toString());
                    ps.setInt(1, usuarioId);
                    ps.setString(2, secretario.getNombres());
                    ps.setString(3, secretario.getApellidos());
                    ps.setDouble(4, secretario.getSalario());
                    ps.setTime(5, Time.valueOf(secretario.getHoraEntrada()));
                    ps.setTime(6, Time.valueOf(secretario.getHoraSalida()));
                    ctos = ps.executeUpdate();
                    if (ctos == 0) {
                        mensaje = "Error al insertar el secretario";
                        transaccionExitosa = false;
                    }
                }
            }
            // Si todas las operaciones fueron exitosas, realiza los cambios
            if (transaccionExitosa) {
                cn.commit();
            } else {
                // Si hubo algún error, deshacemos la transacción
                cn.rollback();
            }
            // Volvemos a habilitar el modo de confirmación automática
            cn.setAutoCommit(true);
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return mensaje;
    }

    @Override
    public String actualizarSecretario(Secretario secretario) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE usuario u ")
                .append("JOIN secretario s ON u.id = s.usuario_id ")
                .append("SET u.correo = ?, u.rol = ?, u.estado = ? ")
                .append("WHERE s.id = ?");
        try (Connection cn = conexion.conexionBD()) {
            // Iniciar transacción
            cn.setAutoCommit(false);
            boolean transaccionExitosa = true;
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setString(1, secretario.getUsuario().getCorreo());
            ps.setString(2, secretario.getUsuario().getRol());
            ps.setBoolean(3, secretario.getUsuario().getEstado());
            ps.setInt(4, secretario.getId());
            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                mensaje = "No se pudo actualizar el usuario";
                transaccionExitosa = false;
            } else {
                query = new StringBuilder();
                query.append("UPDATE secretario SET ")
                        .append("nombres = ?, apellidos = ?, salario = ?, ")
                        .append("hora_entrada = ?, hora_salida = ? ")
                        .append("WHERE id = ?");
                ps = cn.prepareStatement(query.toString());
                ps.setString(1, secretario.getNombres());
                ps.setString(2, secretario.getApellidos());
                ps.setDouble(3, secretario.getSalario());
                ps.setTime(4, Time.valueOf(secretario.getHoraEntrada()));
                ps.setTime(5, Time.valueOf(secretario.getHoraSalida()));
                ps.setInt(6, secretario.getId());
                ctos = ps.executeUpdate();
                if (ctos == 0) {
                    mensaje = "No se pudo actualizar el secretario";
                    transaccionExitosa = false;
                }
            }
            // Si no hay mensajes de error, realiza los cambios
            if (transaccionExitosa) {
                cn.commit();
            } else {
                // Si hubo algún error, deshacemos la transacción
                cn.rollback();
            }
            // Volvemos a habilitar el modo de confirmación automática
            cn.setAutoCommit(true);
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return mensaje;
    }

    @Override
    public String eliminarSecretario(Integer id) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM usuario ")
                .append("WHERE id = (SELECT usuario_id FROM secretario WHERE id = ?)");
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setInt(1, id);
            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                mensaje = "ID: " + id + " no existe";
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return mensaje;
    }

    @Override
    public String obtenerClave(String idSecretario) {
        String clave = null;
        String query = "SELECT u.clave FROM usuario u JOIN secretario s ON u.id = s.usuario_id WHERE s.id = ?";
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, idSecretario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                clave = rs.getString(1);
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return clave;
    }

    @Override
    public String actualizarClave(String idSecretario, String nuevaClave) {
        String result = null;
        String query = "UPDATE usuario u JOIN secretario s ON u.id = s.usuario_id SET u.clave = ? WHERE s.id = ?";
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, nuevaClave);
            ps.setString(2, idSecretario);
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
    public Integer obtenerUsuarioIdPorSecretarioId(Integer secretarioId) {
        Integer usuarioId = null;
        String query = "SELECT usuario_id FROM secretario WHERE id = ?";
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, secretarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuarioId = rs.getInt("usuario_id");
            }
        } catch (SQLException e) {
            mensaje = "Error al obtener el ID del usuario: " + e.getMessage();
        }
        return usuarioId;
    }

    @Override
    public String getMensaje() {
        return mensaje;
    }
}
