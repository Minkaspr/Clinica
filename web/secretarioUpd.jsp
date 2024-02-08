
<%@page import="entity.Secretario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            Secretario secretario = (Secretario) request.getAttribute("secretario");
            String nombres = (secretario != null) ? secretario.getNombres() : "";
            String apellidos = (secretario != null) ? secretario.getApellidos() : "";
            String salario = (secretario != null) ? String.valueOf(secretario.getSalario()) : "";
            String correo = (secretario != null) ? secretario.getUsuario().getCorreo() : "";
            String rol = (secretario != null) ? secretario.getUsuario().getRol() : "";
            Boolean estado = (secretario != null) ? secretario.getUsuario().getEstado() : true;
            String horaEntrada = (secretario != null) ? secretario.getHoraEntrada().toString() : "";
            String horaSalida = (secretario != null) ? secretario.getHoraSalida().toString() : "";
        %>
        <% request.getSession().setAttribute("idSecretario", secretario.getId());%>
        <a href="secretarioUpdPass.jsp">Cambiar contraseña</a>

        <h1>Actualizar Secretario</h1>
        <form action="Secretario" method="POST" class="form">
            <input type="hidden" name="op" value="UPD"/>
            <input type="hidden" name="idSecretario" value="<%= secretario.getId()%>"/>
            <div class="form__group">
                <div class="form__item">
                    <label for="nombre" class="form__label">Nombres</label>
                    <input type="text" id="nombre" class="form__input" name="nombres" value="<%= nombres%>" />
                </div>
                <div class="form__item">
                    <label for="apellidos" class="form__label">Apellidos</label>
                    <input type="text" id="apellidos" class="form__input" name="apellidos" value="<%= apellidos%>">
                </div>
                <div class="form__item">
                    <label for="correo" class="form__label">Correo</label>
                    <input type="email" id="correo" class="form__input" name="correo" value="<%= correo%>">
                </div>
                <div class="form__item">
                    <label for="salario" class="form__label">Salario</label>
                    <input type="number" id="salario" class="form__input" name="salario" value="<%= salario%>">
                </div>
                <div class="form__item">
                    <label for="horaEntrada" class="form__label">Hora de Entrada</label>
                    <input type="time" id="horaEntrada" class="form__input" name="horaEntrada" value="<%= horaEntrada%>">
                </div>
                <div class="form__item">
                    <label for="horaSalida" class="form__label">Hora de Salida</label>
                    <input type="time" id="horaSalida" class="form__input" name="horaSalida" value="<%= horaSalida%>">
                </div>
                <div class="form__item">
                    <label for="rol" class="form__label">Rol</label>
                    <select id="rol" class="form__input" name="rol">
                        <option value="Secretario" <%= "Secretario".equals(rol) ? "selected" : ""%>>Secretario</option>
                    </select>
                </div>
                <div class="form__item">
                    <label for="estado" class="form__label">Estado</label>
                    <select id="estado" class="form__input" name="estado">
                        <option value="1" <%= estado ? "selected" : ""%>>Activo</option>
                        <option value="0" <%= !estado ? "selected" : ""%>>Inactivo</option>
                    </select>
                </div>
                <div class="btn__group">
                    <button class="btn__item btn__item--cancel" onclick="location.href = 'medico.jsp'">
                        <i class="uil uil-times"></i>
                        <span class="btnText">Cancelar</span>
                    </button>
                    <button class="btn__item btn__item--add" type="submit">
                        <i class="uil uil-check"></i>
                        <span class="btnText">Actualizar</span>
                    </button>
                </div>
            </div>
        </form>
        <% if (request.getAttribute("message") != null) {%>
        <div><%= request.getAttribute("message")%></div>
        <% }%>
    </body>
</html>
