
package dao;

import dto.UsuarioDTO;

public interface DaoUsuario {
    String obtenerClave(String correo);
    UsuarioDTO iniciarSesion(String correo);
    String actualizarClave(String correoUsuario, String nuevaClave);
    UsuarioDTO obtenerDatosPerfil(UsuarioDTO usuario);
    String actualizarPerfil(UsuarioDTO usuario, String nuevoNombres, String nuevoApellidos);
    String getMensaje();
}
