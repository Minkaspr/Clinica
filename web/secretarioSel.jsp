
<%@page import="java.util.List"%>
<%@page import="entity.Secretario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/header.jspf" %>
        <a href="secretarioIns.jsp">Agregar</a>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombres</th>
                    <th>Apellidos</th>
                    <th>Salario</th>
                    <th>Hora de Entrada</th>
                    <th>Hora de Salida</th>
                    <th>Operaciones</th>
                </tr>
            </thead>
            <tbody>
                <% List<Secretario> lista = (List<Secretario>) request.getAttribute("lista");
                    if (lista != null) {
                        for (Secretario s : lista) {%>
                <tr>
                    <td><%= s.getId()%></td>
                    <td><%= s.getNombres()%></td>
                    <td><%= s.getApellidos()%></td>
                    <td><%= s.getSalario()%></td>
                    <td><%= s.getHoraEntrada()%></td>
                    <td><%= s.getHoraSalida()%></td>
                    <td>
                        <a href="?op=GET&id=<%= s.getId()%>">Editar</a>
                        <a href="?op=DEL&id=<%= s.getId()%>">Eliminar</a>
                    </td>
                </tr>
                <% }
                }%>
            </tbody>
        </table>
    </body>

</html>
