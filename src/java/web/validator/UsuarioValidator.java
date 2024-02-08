package web.validator;

import dao.DaoHistorial;
import dao.DaoUsuario;
import dao.impl.DaoHistorialImpl;
import dao.impl.DaoUsuarioImpl;
import dto.UsuarioDTO;
import entity.Historial;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import util.Seguridad;

public class UsuarioValidator {

    private final HttpServletRequest request;
    private final DaoUsuario daoUsuario;
    private final DaoHistorial daoHistorial;

    public UsuarioValidator(HttpServletRequest request) {
        this.request = request;
        this.daoUsuario = new DaoUsuarioImpl();
        this.daoHistorial = new DaoHistorialImpl();

    }

    public String usuarioLi() {
        String result = null;
        String correo = request.getParameter("correo");
        String claveIngresada = request.getParameter("clave");

        // Obtén la clave almacenada en la base de datos
        String claveHash = daoUsuario.obtenerClave(correo);

        try {
            // Cifra y aplica un hash a la clave ingresada por el usuario
            String claveCifradaIngresada = Seguridad.cifrar(claveIngresada);
            String claveHashIngresada = Seguridad.hash(claveCifradaIngresada);

            // Verifica si el hash de la clave ingresada por el usuario coincide con el hash almacenado en la base de datos
            if (claveHashIngresada.equals(claveHash)) {
                // Si las claves coinciden, inicia la sesión
                UsuarioDTO usuarioDTO = daoUsuario.iniciarSesion(correo);
                if (usuarioDTO != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuarioDTO);

                    // Registra la acción en el historial
                    Historial historial = new Historial();
                    historial.setUsuario_id(usuarioDTO.getUsuarioId());
                    historial.setAccion("Inicio de sesión");
                    historial.setFecha_hora(LocalDateTime.now());
                    daoHistorial.insertar(historial);
                } else {
                    result = "No se pudo iniciar la sesión";
                }
            } else {
                result = "La clave ingresada no es correcta";
            }
        } catch (Exception e) {
            result = "Ocurrió un error al procesar la clave: " + e.getMessage();
        }
        return result;
    }

    public String usuarioLo() {
        String result = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Obtiene el usuario de la sesión
            UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");

            // Registra la acción en el historial
            Historial historial = new Historial();
            historial.setUsuario_id(usuarioDTO.getUsuarioId());
            historial.setAccion("Cierre de sesión");
            historial.setFecha_hora(LocalDateTime.now());
            daoHistorial.insertar(historial);

            session.invalidate();
        } else {
            result = "Error con la sesion";
        }
        return result;
    }

    public String historialPorUsuarioId() {
        String result = null;
        UsuarioDTO usuarioDTO = (UsuarioDTO) request.getSession().getAttribute("usuario");
        List<Historial> historial = daoHistorial.listarPorUsuarioId(usuarioDTO.getUsuarioId());
        if (historial != null) {
            request.setAttribute("historial", historial);
        } else {
            result = daoHistorial.getMensaje();
        }
        return result;
    }

    public String actualizarClave() {
        String result = null;
        // Obtén el correo del usuario de la sesión
        UsuarioDTO usuarioDTO = (UsuarioDTO) request.getSession().getAttribute("usuario");
        String correoUsuario = usuarioDTO.getCorreo();
        String passActual = request.getParameter("passActual");
        String passNueva = request.getParameter("passNueva");
        String passNuevaRepetida = request.getParameter("passNuevaRepetida");

        // Obtener el hash de la clave almacenada en la base de datos
        String claveHash = daoUsuario.obtenerClave(correoUsuario);

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
                    result = daoUsuario.actualizarClave(correoUsuario, nuevaClaveHash);

                    // Si la actualización de la clave fue exitosa, registra la acción en el historial
                    if (result == null) {
                        Historial historial = new Historial();
                        historial.setUsuario_id(usuarioDTO.getUsuarioId());
                        historial.setAccion("Actualización de contraseña");
                        historial.setFecha_hora(LocalDateTime.now());
                        daoHistorial.insertar(historial);
                    }
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

    public String actualizarPerfil() {
        // Obtén el objeto UsuarioDTO de la sesión
        UsuarioDTO usuarioDTO = (UsuarioDTO) request.getSession().getAttribute("usuario");
        // Obtén los nuevos nombres y apellidos del formulario
        String nuevoNombres = request.getParameter("nuevoNombres");
        String nuevoApellidos = request.getParameter("nuevoApellidos");
        // Actualiza el perfil del usuario
        String result = daoUsuario.actualizarPerfil(usuarioDTO, nuevoNombres, nuevoApellidos);

        // Si la actualización del perfil fue exitosa, registra la acción en el historial
        if (result == null) {
            Historial historial = new Historial();
            historial.setUsuario_id(usuarioDTO.getUsuarioId());
            historial.setAccion("Datos personales actualizados");
            historial.setFecha_hora(LocalDateTime.now());
            daoHistorial.insertar(historial);

            // Actualiza los nombres en el objeto UsuarioDTO de la sesión
            usuarioDTO.setNombres(nuevoNombres);
            request.getSession().setAttribute("usuario", usuarioDTO);
        }

        return result;
    }

    public String cargarDatos() {
        String result = null;
        // Obtén el objeto UsuarioDTO de la sesión
        UsuarioDTO usuarioDTO = (UsuarioDTO) request.getSession().getAttribute("usuario");
        // Obtén los datos del perfil del usuario
        UsuarioDTO usuarioConDatosPerfil = daoUsuario.obtenerDatosPerfil(usuarioDTO);
        if (usuarioConDatosPerfil != null) {
            request.setAttribute("usuario", usuarioConDatosPerfil);
        } else {
            result = daoUsuario.getMensaje();
        }
        return result;
    }
}
