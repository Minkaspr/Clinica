
package dao;

import entity.Historial;
import java.util.List;

public interface DaoHistorial {
    String insertar(Historial historial);
    List<Historial> listarPorUsuarioId(Integer usuarioId);
    String getMensaje();
}
