
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Ingresa tus credenciales</h1>
        <form action="Usuario" method="post">
            <input type="hidden" name="op" value="LI"/>
            <label>
                Correo
                <input type="text" name="correo"/>
            </label>
            <label>
                Clave
                <input type="password" name="clave"/>
            </label>
            <input type="submit" value="Ingresar"/>
        </form>

        <% if (request.getAttribute("message") != null) {%>
        <div><%= request.getAttribute("message")%></div>
        <% }%>
    </body>
</html>
