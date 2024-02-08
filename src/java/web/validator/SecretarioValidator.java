package web.validator;

import dao.DaoHistorial;
import dao.DaoSecretario;
import dao.impl.DaoHistorialImpl;
import dao.impl.DaoSecretarioImpl;
import dto.UsuarioDTO;
import entity.Historial;
import entity.Secretario;
import entity.Usuario;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import util.Seguridad;

public class SecretarioValidator {

    private final HttpServletRequest request;
    private final DaoSecretario daoSecretario;
    private final DaoHistorial daoHistorial;

    public SecretarioValidator(HttpServletRequest request) {
        this.request = request;
        this.daoSecretario = new DaoSecretarioImpl();
        this.daoHistorial = new DaoHistorialImpl();
    }

    public String secretarioSel() {
        String result = null;
        List<Secretario> lista = daoSecretario.obtenerSecretarios();
        if (lista != null) {
            request.setAttribute("lista", lista);
        } else {
            result = daoSecretario.getMensaje();
        }
        return result;
    }

    public String secretarioInsUpd(boolean agreActu) {
        StringBuilder result = new StringBuilder("<ul>");

        String idSecretarioParam = request.getParameter("idSecretario");
        Integer idSecretario = (idSecretarioParam != null && !idSecretarioParam.isEmpty()) ? Integer.valueOf(idSecretarioParam) : null;

        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String salario = request.getParameter("salario");

        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");
        String rol = request.getParameter("rol");
        String estadoParam = request.getParameter("estado");
        Boolean estado = "1".equals(estadoParam);

        String horaEntrada = request.getParameter("horaEntrada");
        String horaSalida = request.getParameter("horaSalida");

        Secretario secretario = new Secretario();
        secretario.setId(idSecretario);
        secretario.setNombres(nombres);
        secretario.setApellidos(apellidos);
        secretario.setSalario(Double.valueOf(salario));
        secretario.setHoraEntrada(LocalTime.parse(horaEntrada));
        secretario.setHoraSalida(LocalTime.parse(horaSalida));

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);

        if (agreActu) {
            usuario.setRol(rol);
            usuario.setEstado(estado);
        } else {
            usuario.setRol("Secretario");
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

        secretario.setUsuario(usuario);

        if (result.length() == 4) {
            String msg = agreActu
                    ? daoSecretario.actualizarSecretario(secretario)
                    : daoSecretario.agregarSecretario(secretario);
            if (msg != null) {
                result.append("<li>").append(msg).append("<li>");
            } else {
                // Obtén el ID del usuario logueado
                UsuarioDTO usuarioDTO = (UsuarioDTO) request.getSession().getAttribute("usuario");
                Integer usuarioId = usuarioDTO.getUsuarioId();
                // Crea un nuevo objeto Historial
                Historial historial = new Historial();
                historial.setUsuario_id(usuarioId);
                historial.setAccion(agreActu ? "Actualización de secretario" : "Creación de secretario");
                historial.setFecha_hora(LocalDateTime.now());
                if (usuarioDTO.getRol().equalsIgnoreCase("administrador")) {
                    Integer usuarioAfectadoId = agreActu ? daoSecretario.obtenerUsuarioIdPorSecretarioId(idSecretario) : DaoSecretarioImpl.ultimoIdUsuario;
                    historial.setObservaciones("ID del usuario afectado: " + usuarioAfectadoId);
                }
                // Inserta el nuevo historial en la base de datos
                daoHistorial.insertar(historial);
            }
        }

        if (result.length() > 4) {
            request.setAttribute("secretario", secretario);
        }

        return result.length() == 4 ? null : result.append("</ul>").toString();
    }

    public String secretarioGet() {
        String result = null;
        String idSecretarioAux = request.getParameter("id");
        Integer idSecretario = Integer.valueOf(idSecretarioAux);
        Secretario secretario = daoSecretario.obtenerSecretarioPorId(idSecretario);
        if (secretario != null) {
            request.setAttribute("secretario", secretario);
        } else {
            result = daoSecretario.getMensaje();
        }
        return result;
    }

    public String actualizarClave() {
        String result = null;
        String idSecretario = request.getParameter("idSecretario");
        String passActual = request.getParameter("passActual");
        String passNueva = request.getParameter("passNueva");
        String passNuevaRepetida = request.getParameter("passNuevaRepetida");

        // Obtener el hash de la clave almacenada en la base de datos
        String claveHash = daoSecretario.obtenerClave(idSecretario);

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
                    result = daoSecretario.actualizarClave(idSecretario, nuevaClaveHash);
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

    public String secretarioDel() {
        String idParam = request.getParameter("id");
        String resultado = null;
        if (idParam != null && !idParam.isEmpty()) {
            Integer id = Integer.parseInt(idParam);
            int secretarioId =  daoSecretario.obtenerUsuarioIdPorSecretarioId(id);
            resultado = daoSecretario.eliminarSecretario(id);

            // Obtén el ID del usuario logueado
            UsuarioDTO usuarioDTO = (UsuarioDTO) request.getSession().getAttribute("usuario");
            Integer usuarioId = usuarioDTO.getUsuarioId();
            // Crea un nuevo objeto Historial
            Historial historial = new Historial();
            historial.setUsuario_id(usuarioId);
            historial.setAccion("Eliminación de secretario");
            historial.setFecha_hora(LocalDateTime.now());
            historial.setObservaciones("ID del usuario eliminado: " +secretarioId);
            // Inserta el nuevo historial en la base de datos
            daoHistorial.insertar(historial);
        } else {
            resultado = "ID incorrecto";
        }
        return resultado;
    }
}
