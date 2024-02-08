
<%@page import="entity.Medico"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            Medico medico = (Medico) request.getAttribute("medico");
            String nombres = (medico != null) ? medico.getNombres() : "";
            String apellidos = (medico != null) ? medico.getApellidos() : "";
            String numeroColegiado = (medico != null) ? medico.getNumeroColegiado() : "";
            String especialidad = (medico != null) ? medico.getEspecialidad() : "";
            String correo = (medico != null) ? medico.getUsuario().getCorreo() : "";
            String rol = (medico != null) ? medico.getUsuario().getRol() : "";
            Boolean estado = (medico != null) ? medico.getUsuario().getEstado() : true;
            String horaEntrada = (medico != null) ? medico.getHoraEntrada().toString() : "";
            String horaSalida = (medico != null) ? medico.getHoraSalida().toString() : "";
        %>
        <% request.getSession().setAttribute("idMedico", medico.getId());%>
        <a href="medicoUpdPass.jsp">Cambiar contraseña</a>

        <h1>Actualizar Medico</h1>
        <form action="Medico" method="POST" class="form">
            <input type="hidden" name="op" value="UPD"/>
            <input type="hidden" name="idMedico" value="<%= medico.getId()%>"/>
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
                    <label for="numeroColegiado" class="form__label">Numero Colegiado</label>
                    <input type="number" id="numeroColegiado" class="form__input" name="numeroColegiado" value="<%= numeroColegiado%>">
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
                    <label for="especialidad" class="form__label">Especialidad</label>
                    <select id="especialidad" class="form__input" name="especialidad">
                        <option value="Cardiologia" <%= "Cardiologia".equals(especialidad) ? "selected" : ""%>>Cardiología</option>
                        <option value="Dermatologia" <%= "Dermatologia".equals(especialidad) ? "selected" : ""%>>Dermatología</option>
                        <option value="Neurologia" <%= "Neurologia".equals(especialidad) ? "selected" : ""%>>Neurología</option>
                        <option value="Pediatria" <%= "Pediatria".equals(especialidad) ? "selected" : ""%>>Pediatría</option>
                    </select>
                </div>
                <div class="form__item">
                    <label for="rol" class="form__label">Rol</label>
                    <select id="rol" class="form__input" name="rol">
                        <option value="Medico" <%= "Medico".equals(rol) ? "selected" : ""%>>Médico</option>
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
        </form>
        <% if (request.getAttribute("message") != null) {%>
        <div><%= request.getAttribute("message")%></div>
        <% }%>
    </body>
</html>
