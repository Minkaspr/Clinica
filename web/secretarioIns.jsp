
<%@page import="entity.Secretario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Nuevo Secretario</h1>
        <%
            Secretario secretario = (Secretario) request.getAttribute("secretario");
            String nombres = (secretario != null) ? secretario.getNombres() : "";
            String apellidos = (secretario != null) ? secretario.getApellidos() : "";
            String salario = (secretario != null) ? String.valueOf(secretario.getSalario()) : "";
            String correo = (secretario != null) ? secretario.getUsuario().getCorreo() : "";
            String clave = (secretario != null) ? secretario.getUsuario().getClave() : "";
            String horaEntrada = (secretario != null) ? secretario.getHoraEntrada().toString() : "";
            String horaSalida = (secretario != null) ? secretario.getHoraSalida().toString() : "";
        %>
        <form action="Secretario" method="POST" class="form">
            <input type="hidden" name="op" value="INS"/>
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
                    <label for="clave" class="form__label">Clave</label>
                    <input type="password" id="clave" class="form__input" name="clave" value="<%= clave%>">
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
                <div class="btn__group">
                    <button class="btn__item btn__item--cancel" onclick="location.href = 'medico.jsp'">
                        <i class="uil uil-times"></i>
                        <span class="btnText">Cancelar</span>
                    </button>
                    <button class="btn__item btn__item--add" type="submit">
                        <i class="uil uil-check"></i>
                        <span class="btnText">Agregar</span>
                    </button>
                </div>
            </div>
        </form>
        <% if (request.getAttribute("message") != null) {%>
        <div><%= request.getAttribute("message")%></div>
        <% }%>
    </body>
</html>
