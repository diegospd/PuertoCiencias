/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.RegistroUsuario.contrasenaValida;
import static controller.RegistroUsuario.numCuentaValido;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author diego
 */
@WebServlet(name = "RecuperarPassword", urlPatterns = {"/RecuperarPassword"})
public class RecuperarPassword extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String correo = RegistroUsuario.limpiaCorreo(request.getParameter("correo"));
        String numCuenta = request.getParameter("numCuenta");
        String pass1 = request.getParameter("pass1").trim();
        String pass2 = request.getParameter("pass2").trim();
        
        //No mostrar advertencia pero enviar un correo con el link a la página de recuperación
        boolean existeCorreo = model.Usuario.existeCorreo(correo);
        
        //Captcha
        boolean numCuentaEsValida = numCuentaValido(numCuenta);
        
        //Tienen que coincidir las contraseñas
        boolean passCoinciden = pass1.equals(pass2);
        
        //La contrasena tiene que tener el formato que dimos en el SRS
        boolean passValida = contrasenaValida(pass1);
        
        boolean ok = existeCorreo && numCuentaEsValida && passCoinciden && passValida;
        
        if (ok) {
            mandarCorreoRecuperacion(correo, pass1);
        }
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RecuperarPassword</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RecuperarPassword at " + request.getContextPath() + "</h1>");
            out.println("Te mandamos un correo a " + correo + "@ciencias.unam.mx");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    
    private static void mandarCorreoRecuperacion(String correo, String password) {
        String hash = funciones.Cifrado.sha1(password);
        String codigo = funciones.Cifrado.sha1(correo+password+hash);
        model.Usuario.nuevoCodigo(correo, codigo);
        String texto = "Hola!\n\nHas solicitado cambiar tu contraseña de Puerto Ciencias.\n";
        texto += "La contraseña que elegiste fue: " + password + "\n\n";
        texto += "Si fuiste tú quien solicitó el cambio, puedes acceder al siguiente enlace para que tome efecto.\n\n";
        texto += constantes.Constantes.DIR_PUERTO + "ConfirmarCambioPassword?codigo=" +codigo+ "&h="+hash;
        texto += "\n\nQue la fuerza te acompañe,\nAstillero Ciencias";
        SendGrid.enviarCorreo(correo + "@ciencias.unam.mx", "[Puerto Ciencias] Confirma tu cambio de contraseña", texto);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
