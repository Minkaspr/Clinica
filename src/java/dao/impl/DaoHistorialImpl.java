package dao.impl;

import dao.DaoHistorial;
import entity.Historial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import util.ConexionBD;

public class DaoHistorialImpl implements DaoHistorial {

    private final ConexionBD conexion;
    private String mensaje;

    public DaoHistorialImpl() {
        this.conexion = new ConexionBD();
    }

    @Override
    public String insertar(Historial historial) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO historial (usuario_id, accion, fecha_hora, observaciones) ")
                .append("VALUES (?, ?, ?, ?)");
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setInt(1, historial.getUsuario_id());
            ps.setString(2, historial.getAccion());
            Timestamp timestamp = Timestamp.valueOf(historial.getFecha_hora());
            ps.setTimestamp(3, timestamp);
            ps.setString(4, historial.getObservaciones());
            ps.executeUpdate();
        } catch (SQLException e) {
            mensaje = "Error al insertar el historial: " + e.getMessage();
        }
        return mensaje;
    }

    @Override
    public List<Historial> listarPorUsuarioId(Integer usuarioId) {
        List<Historial> historiales = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM historial ")
                .append("WHERE usuario_id = ?");
        try (Connection cn = conexion.conexionBD()) {
            PreparedStatement ps = cn.prepareStatement(query.toString());
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Historial historial = new Historial();
                historial.setId(rs.getInt("id"));
                historial.setUsuario_id(rs.getInt("usuario_id"));
                historial.setAccion(rs.getString("accion"));
                historial.setFecha_hora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                historial.setObservaciones(rs.getString("observaciones"));
                historiales.add(historial);
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        }
        return historiales;
    }

    @Override
    public String getMensaje() {
        return mensaje;
    }
}
