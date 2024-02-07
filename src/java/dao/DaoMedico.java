
package dao;

import dto.MedicoDTO;
import entity.Medico;
import java.util.List;

public interface DaoMedico {
    List<MedicoDTO> obtenerMedicos();
    Medico obtenerMedicoPorId(Integer id);
    String agregarMedico(Medico medico);
    String actualizarMedico(Medico medico);
    String eliminarMedico(Integer id);
    String obtenerClave(String idUsuario);
    String actualizarClave(String idMedico, String nuevaClave);
    String getMensaje();
}
