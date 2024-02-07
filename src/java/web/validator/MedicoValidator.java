package web.validator;

import dao.DaoMedico;
import dao.impl.DaoMedicoImpl;
import dto.MedicoDTO;
import entity.Medico;
import entity.Usuario;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import util.Seguridad;

/*
Y para descifrarla cuando sea necesario:
String claveHash = usuario.getClave();
String claveCifrada = UtilidadSeguridad.descifrar(claveHash);
 */
public class MedicoValidator {

    private final HttpServletRequest request;
    private final DaoMedico daoMedico;

    public MedicoValidator(HttpServletRequest request) {
        this.request = request;
        this.daoMedico = new DaoMedicoImpl();
    }

    public String medicoSel() {
        String result = null;
        List<MedicoDTO> lista = daoMedico.obtenerMedicos();
        if (lista != null) {
            request.setAttribute("lista", lista);
        } else {
            result = daoMedico.getMensaje();
        }
        return result;
    }

    public String medicoInsUpd(boolean agreActu) {
        StringBuilder result = new StringBuilder("<ul>");

        String idMedicoParam = request.getParameter("idMedico");
        Integer idMedico = (idMedicoParam != null && !idMedicoParam.isEmpty()) ? Integer.valueOf(idMedicoParam) : null;

        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String numeroColegiado = request.getParameter("numeroColegiado");
        String especialidad = request.getParameter("especialidad");

        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");
        String rol = request.getParameter("rol");
        Boolean estado = Boolean.valueOf(request.getParameter("estado"));

        String horaEntrada = request.getParameter("horaEntrada");
        String horaSalida = request.getParameter("horaSalida");

        if (agreActu && idMedico == null) {
            result.append("<li>ID requerido</li>");
        }

        if (nombres == null || nombres.trim().length() == 0) {
            result.append("<li>Nombres requeridos</li>");
        } else if (nombres.trim().length() < 3 || nombres.trim().length() > 30) {
            result.append("<li>La dimensión del nombre debe estar entre 3 a 30 caracteres</li>");
        }

        if (apellidos == null || apellidos.trim().length() == 0) {
            result.append("<li>Apellidos requeridos</li>");
        } else if (apellidos.trim().length() < 3 || apellidos.trim().length() > 50) {
            result.append("<li>La dimensión de los apellidos debe estar entre 3 a 50 caracteres</li>");
        }

        if (numeroColegiado == null || numeroColegiado.trim().length() == 0) {
            result.append("<li>Número Colegiado requerido</li>");
        }

        if (especialidad == null || especialidad.trim().length() == 0) {
            result.append("<li>Especialidad requerida</li>");
        }

        if (horaEntrada == null || horaEntrada.trim().length() == 0) {
            result.append("<li>Hora de Entrada requerida</li>");
        }

        if (horaSalida == null || horaSalida.trim().length() == 0) {
            result.append("<li>Hora de Salida requerida</li>");
        }

        Medico medico = new Medico();
        medico.setId(idMedico);
        medico.setNombres(nombres);
        medico.setApellidos(apellidos);
        medico.setNumeroColegiado(numeroColegiado);
        medico.setEspecialidad(especialidad);
        medico.setHoraEntrada(LocalTime.parse(horaEntrada));
        medico.setHoraSalida(LocalTime.parse(horaSalida));

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        String claveCifrada;
        String claveHash;
        try {
            claveCifrada = Seguridad.cifrar(clave);
            claveHash = Seguridad.hash(claveCifrada);
            usuario.setClave(claveHash);
        } catch (Exception ex) {
            result.append("<li>Error al cifrar la contraseña: ").append(ex.getMessage()).append("</li>");
        }

        if (agreActu) {
            usuario.setRol(rol);
            usuario.setEstado(estado);
        } else {
            usuario.setRol("Medico");
            usuario.setEstado(true);
        }

        medico.setUsuario(usuario);

        if (result.length() == 4) {
            String msg = agreActu
                    ? daoMedico.actualizarMedico(medico)
                    : daoMedico.agregarMedico(medico);
            if (msg != null) {
                result.append("<li>").append(msg).append("<li>");
            }
        }

        if (result.length() > 4) {
            request.setAttribute("medico", medico);
        }

        return result.length() == 4 ? null : result.append("</ul>").toString();
    }
}
