<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Actualizar mi contraseña</h1>
        <form action="Usuario" method="post">
            <input type="hidden" name="op" value="UP">
            <input type="hidden" name="idUsuario" value="<%= request.getSession().getAttribute("idUsuario")%>">
            <label for="passActual">Contraseña actual:</label>
            <input type="password" id="passActual" name="passActual">
            <label for="passNueva">Nueva contraseña:</label>
            <input type="password" id="passNueva" name="passNueva">
            <label for="passNuevaRepetida">Repite la nueva contraseña:</label>
            <input type="password" id="passNuevaRepetida" name="passNuevaRepetida">
            <input type="submit" value="Actualizar contraseña">
        </form>
        <% if (request.getAttribute("message") != null) {%>
        <div><%= request.getAttribute("message")%></div>
        <% }%>
    </body>
</html>
