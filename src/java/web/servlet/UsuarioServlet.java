package web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.validator.UsuarioValidator;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/Usuario"})
public class UsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String opcion = request.getParameter("op");
        opcion = (opcion == null) ? "" : opcion;

        String result = null;
        String target = "index.html";

        UsuarioValidator validator = new UsuarioValidator(request);
        switch (opcion) {
            case "LI": // LogIn
                result = validator.usuarioLi();
                target = result == null ? "dashboard.jsp" : "login.jsp";
                break;
            case "LO": //LogOut
                result = validator.usuarioLo();
                target = result == null ? "index.html" : "dashboard.jsp";
                break;
            case "MH": // My History
                result = validator.historialPorUsuarioId();
                target = result == null ? "historial.jsp" : "dashboard.jsp";
                break;
            case "UP": // Update Password
                result = validator.actualizarClave();
                target = result == null ? "dashboard.jsp" : "usuarioUpdPass.jsp";
                break;
            case "LPD": // Load Personal Data
                result = validator.cargarDatos();
                target = "usuarioUpdPf.jsp";
                break;
            case "UPD": // Update Personal Data
                result = validator.actualizarPerfil();
                target = result == null ? "dashboard.jsp" : "usuarioUpdPf.jsp";
                break;
            case "":
                result = "Solicitud requerida";
                break;
            default:
                result = "Solicitud no reconocida";
        }

        if (result != null) {
            request.setAttribute("message", result);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(target);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
