
<%@page import="dto.UsuarioDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <% UsuarioDTO usuario = (UsuarioDTO) request.getAttribute("usuario");%>
        <h1>Actualizar Mis Datos Personales</h1>
        <form action="Usuario" method="post">
            <input type="hidden" name="op" value="UPD">
            <label for="nuevoNombres">Nuevos nombres:</label>
            <input type="text" id="nuevoNombres" name="nuevoNombres" value="<%= usuario.getNombres() %>">
            <label for="nuevoApellidos">Nuevos apellidos:</label>
            <input type="text" id="nuevoApellidos" name="nuevoApellidos" value="<%= usuario.getApellidos()%>">
            <input type="submit" value="Actualizar perfil">
        </form>
        <% if (request.getAttribute("message") != null) {%>
        <div><%= request.getAttribute("message")%></div>
        <% }%>

    </body>
</html>
