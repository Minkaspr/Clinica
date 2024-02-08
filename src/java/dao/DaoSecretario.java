
package dao;

import entity.Secretario;
import java.util.List;

public interface DaoSecretario {
    List<Secretario> obtenerSecretarios();
    Secretario obtenerSecretarioPorId(Integer id);
    String agregarSecretario(Secretario secretario);
    String actualizarSecretario(Secretario secretario);
    String eliminarSecretario(Integer id);
    String obtenerClave(String idSecretario);
    String actualizarClave(String idSecretario, String nuevaClave);
    Integer obtenerUsuarioIdPorSecretarioId(Integer secretarioId);
    String getMensaje();
}
