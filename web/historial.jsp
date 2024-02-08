
<%@page import="entity.Historial"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/header.jspf" %>
        <h1>Mi Historial</h1>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Acción</th>
                    <th>Fecha y Hora</th>
                    <th>Observaciones</th>
                </tr>
            </thead>
            <tbody>
                <% for (Historial h : (List<Historial>) request.getAttribute("historial")) {%>
                <tr>
                    <td><%= h.getId()%></td>
                    <td><%= h.getAccion()%></td>
                    <td><%= h.getFecha_hora()%></td>
                    <td><%= h.getObservaciones()%></td>
                </tr>
                <% }%>
            </tbody>
        </table>

    </body>
</html>
