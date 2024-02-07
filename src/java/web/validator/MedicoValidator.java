package web.validator;

import dao.DaoMedico;
import dao.impl.DaoMedicoImpl;
import dto.MedicoDTO;
import entity.Medico;
import entity.Usuario;
import java.time.LocalTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import util.Seguridad;

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
        String estadoParam = request.getParameter("estado");
        Boolean estado = "1".equals(estadoParam);

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

        if (agreActu) {
            usuario.setRol(rol);
            usuario.setEstado(estado);
        } else {
            usuario.setRol("Medico");
            usuario.setEstado(true);

            String claveCifrada;
            String claveHash;
            try {
                claveCifrada = Seguridad.cifrar(clave);
                claveHash = Seguridad.hash(claveCifrada);
                usuario.setClave(claveHash);
            } catch (Exception ex) {
                result.append("<li>Error al cifrar la contraseña: ").append(ex.getMessage()).append("</li>");
            }
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

    public String medicoGet() {
        String result = null;
        String idMedicoAux = request.getParameter("id");
        Integer idMedico = Integer.valueOf(idMedicoAux);
        Medico medico = daoMedico.obtenerMedicoPorId(idMedico);
        if (medico != null) {
            request.setAttribute("medico", medico);
        } else {
            result = daoMedico.getMensaje();
        }
        return result;
    }

    public String actualizarClave() {
        String result = null;
        String idMedico = request.getParameter("idMedico");
        String passActual = request.getParameter("passActual");
        String passNueva = request.getParameter("passNueva");
        String passNuevaRepetida = request.getParameter("passNuevaRepetida");

        // Obtener el hash de la clave almacenada en la base de datos
        String claveHash = daoMedico.obtenerClave(idMedico);

        try {
            // Cifrar y aplicar un hash a la clave actual ingresada por el usuario
            String claveCifradaActual = Seguridad.cifrar(passActual);
            String claveHashActual = Seguridad.hash(claveCifradaActual);

            // Verificar si el hash de la clave actual ingresada por el usuario coincide con el hash almacenado en la base de datos
            if (claveHashActual.equals(claveHash)) {
                // Verificar si la nueva clave ingresada por el usuario coincide con la repetida
                if (passNueva.equals(passNuevaRepetida)) {
                    // Cifrar la nueva clave y aplicarle un hash, y luego actualizarla en la base de datos
                    String nuevaClaveCifrada = Seguridad.cifrar(passNueva);
                    String nuevaClaveHash = Seguridad.hash(nuevaClaveCifrada);
                    result = daoMedico.actualizarClave(idMedico, nuevaClaveHash);
                } else {
                    result = "Las contraseñas nuevas no coinciden";
                }
            } else {
                result = "La contraseña actual no es correcta";
            }
        } catch (Exception e) {
            result = "Ocurrió un error al procesar la contraseña: " + e.getMessage();
        }

        return result;
    }

    public String medicoDel() {
        String idParam = request.getParameter("id");
        String resultado = null;
        if (idParam != null && !idParam.isEmpty()) {
            Integer id = Integer.parseInt(idParam);
            resultado = daoMedico.eliminarMedico(id);
        } else {
            resultado = "ID incorrecto";
        }
        return resultado;
    }
}
