
package dao;

import dto.UsuarioDTO;

public interface DaoUsuario {
    String obtenerClave(String correo);
    UsuarioDTO iniciarSesion(String correo);
    String getMensaje();
}
