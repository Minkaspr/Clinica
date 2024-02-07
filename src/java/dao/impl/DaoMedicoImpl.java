package dao.impl;

import dao.DaoMedico;
import dto.MedicoDTO;
import entity.Medico;
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

public class DaoMedicoImpl implements DaoMedico {

    private final ConexionBD conexion;
    private String mensaje;

    public DaoMedicoImpl() {
        this.conexion = new ConexionBD();
    }

    @Override
    public List<MedicoDTO> obtenerMedicos() {
        List<MedicoDTO> lista = null;
        StringBuilder query = new StringBuilder();
        query.append("SELECT ")
                .append("id, ")
                .append("nombres, ")
                .append("apellidos, ")
                .append("numero_colegiado, ")
                .append("especialidad ")
                .append("FROM medico");
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ResultSet rs = ps.executeQuery();
            lista = new ArrayList<>();
            while (rs.next()) {
                MedicoDTO medicoDTO = new MedicoDTO();
                medicoDTO.setId(rs.getInt(1));
                medicoDTO.setNombres(rs.getString(2));
                medicoDTO.setApellidos(rs.getString(3));
                medicoDTO.setNumeroColegiado(rs.getString(4));
                medicoDTO.setEspecialidad(rs.getString(5));
                lista.add(medicoDTO);
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return lista;
    }

    @Override
    public Medico obtenerMedicoPorId(Integer id) {
        Medico medico = null;
        StringBuilder query = new StringBuilder();
        query.append("SELECT ")
                .append("m.id, ")
                .append("u.correo, ")
                .append("u.rol, ")
                .append("u.estado, ")
                .append("m.nombres, ")
                .append("m.apellidos, ")
                .append("m.numero_colegiado, ")
                .append("m.especialidad, ")
                .append("m.hora_entrada, ")
                .append("m.hora_salida ")
                .append("FROM medico m ")
                .append("INNER JOIN usuario u ON m.usuario_id = u.id ")
                .append("WHERE m.id = ?");
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

                medico = new Medico();
                medico.setId(rs.getInt(1));
                medico.setUsuario(usuario);
                medico.setNombres(rs.getString(5));
                medico.setApellidos(rs.getString(6));
                medico.setNumeroColegiado(rs.getString(7));
                medico.setEspecialidad(rs.getString(8));
                medico.setHoraEntrada(rs.getTime(9).toLocalTime());
                medico.setHoraSalida(rs.getTime(10).toLocalTime());
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return medico;
    }

    @Override
    public String agregarMedico(Medico medico) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO usuario (")
                .append("correo, clave, rol, estado")
                .append(") VALUES (?, ?, ?, ?)");
        try (Connection cn = conexion.conexionBD()) {
            // Iniciar transacción
            cn.setAutoCommit(false);
            PreparedStatement ps = cn.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, medico.getUsuario().getCorreo());
            ps.setString(2, medico.getUsuario().getClave());
            ps.setString(3, medico.getUsuario().getRol());
            ps.setBoolean(4, medico.getUsuario().getEstado());

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
                    // Nueva consulta
                    query = new StringBuilder();
                    query.append("INSERT INTO medico (")
                            .append("usuario_id, nombres, apellidos, numero_colegiado, ")
                            .append("especialidad, hora_entrada, hora_salida")
                            .append(") VALUES (?, ?, ?, ?, ?, ?, ?)");
                    // Insertar en la tabla medico
                    ps = cn.prepareStatement(query.toString());
                    ps.setInt(1, usuarioId);
                    ps.setString(2, medico.getNombres());
                    ps.setString(3, medico.getApellidos());
                    ps.setString(4, medico.getNumeroColegiado());
                    ps.setString(5, medico.getEspecialidad());
                    ps.setTime(6, Time.valueOf(medico.getHoraEntrada()));
                    ps.setTime(7, Time.valueOf(medico.getHoraSalida()));
                    ctos = ps.executeUpdate();
                    if (ctos == 0) {
                        mensaje = "Error al insertar el médico";
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
    public String actualizarMedico(Medico medico) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE usuario SET ")
                .append("correo = ?, rol = ? ")
                .append("WHERE id = ?");
        try (Connection cn = conexion.conexionBD()) {
            // Iniciar transacción
            cn.setAutoCommit(false);
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setString(1, medico.getUsuario().getCorreo());
            ps.setString(2, medico.getUsuario().getRol());
            ps.setInt(3, medico.getUsuario().getId());
            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                mensaje = "No se pudo actualizar el usuario";
            } else {
                query = new StringBuilder();
                query.append("UPDATE medico SET ")
                        .append("nombres = ?, apellidos = ?, numero_colegiado = ?, ")
                        .append("especialidad = ?, hora_entrada = ?, hora_salida = ? ")
                        .append("WHERE id = ?");
                ps = cn.prepareStatement(query.toString());
                ps.setString(1, medico.getNombres());
                ps.setString(2, medico.getApellidos());
                ps.setString(3, medico.getNumeroColegiado());
                ps.setString(4, medico.getEspecialidad());
                ps.setTime(5, Time.valueOf(medico.getHoraEntrada()));
                ps.setTime(6, Time.valueOf(medico.getHoraSalida()));
                ps.setInt(7, medico.getId());
                ctos = ps.executeUpdate();
                if (ctos == 0) {
                    mensaje = "No se pudo actualizar el médico";
                }
            }
            // Si no hay mensajes de error, realiza los cambios
            if (mensaje.isEmpty()) {
                cn.commit();
                mensaje = "Actualización exitosa";
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
    public String eliminarMedico(Integer id) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM usuario ")
                .append("WHERE id = (SELECT usuario_id FROM medico WHERE id = ?)");
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
    public String getMensaje() {
        return mensaje;
    }
}
