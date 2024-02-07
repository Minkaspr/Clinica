<%@page import="java.util.List"%>
<%@page import="dto.MedicoDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <a href="medicoIns.jsp">Agregar</a>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombres</th>
                    <th>Apellidos</th>
                    <th>Número Colegiado</th>
                    <th>Especialidad</th>
                    <th>Operaciones</th>
                </tr>
            </thead>
            <tbody>
                <% List<MedicoDTO> lista = (List<MedicoDTO>) request.getAttribute("lista");
                    if (lista != null) {
                        for (MedicoDTO m : lista) {%>
                <tr>
                    <td><%= m.getId()%></td>
                    <td><%= m.getNombres()%></td>
                    <td><%= m.getApellidos()%></td>
                    <td><%= m.getNumeroColegiado()%></td>
                    <td><%= m.getEspecialidad()%></td>
                    <td>
                        <a href="?op=GET&id=<%= m.getId()%>">Editar</a>
                        <a href="?op=DEL&id=<%= m.getId()%>">Eliminar</a>
                    </td>
                </tr>
                <% }
            }%>
            </tbody>
        </table>
    </body>
</html>
