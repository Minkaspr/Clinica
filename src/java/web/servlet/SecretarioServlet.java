package web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.validator.SecretarioValidator;

@WebServlet(name = "SecretarioServlet", urlPatterns = {"/Secretario"})
public class SecretarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String opcion = request.getParameter("op");
        opcion = (opcion == null) ? "" : opcion;

        String result = null;
        String target = "secretario.jsp";

        SecretarioValidator validator = new SecretarioValidator(request);
        switch (opcion) {
            case "SEL":
                result = validator.secretarioSel();
                target = "secretarioSel.jsp";
                break;
            case "INS":
                result = validator.secretarioInsUpd(false);
                target = result == null ? "secretario.jsp" : "secretarioIns.jsp";
                break;
            case "GET":
                result = validator.secretarioGet();
                target = "secretarioUpd.jsp";
                break;
            case "UPD":
                result = validator.secretarioInsUpd(true);
                target = result == null ? "secretario.jsp" : "secretarioUpd.jsp";
                break;
            case "UPD_PASS":
                result = validator.actualizarClave();
                target = result == null ? "secretario.jsp" : "secretarioUpdPass.jsp";
                break;
            case "DEL":
                result = validator.secretarioDel();
                target = "Secretario?op=SEL";
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
