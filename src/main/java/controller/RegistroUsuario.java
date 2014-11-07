/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import funciones.Cifrado;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;

/**
 *
 * @author diego
 */
@WebServlet(name = "RegistroUsuario", urlPatterns = {"/RegistroUsuario"})
public class RegistroUsuario extends HttpServlet {

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
        
        String correo = limpiaCorreo(request.getParameter("correo"));
        String usuario = request.getParameter("usuario").trim();
        String numCuenta = request.getParameter("numCuenta");
        String pass1 = request.getParameter("pass1").trim();
        String pass2 = request.getParameter("pass2").trim();
        
        //Mostrar advertencia si el usuario ya está ocupado
        boolean existeUsuario = model.Usuario.existeUsuario(usuario);
        
        //No mostrar advertencia pero enviar un correo con el link a la página de recuperación
        boolean existeCorreo = model.Usuario.existeCorreo(correo);
        
        //Captcha
        boolean numCuentaEsValida = numCuentaValido(numCuenta);
        
        //Tienen que coincidir las contraseñas
        boolean passCoinciden = pass1.equals(pass2);
        
        //La contrasena tiene que tener el formato que dimos en el SRS
        boolean passValida = contrasenaValida(pass1);
        
        
        
        
        boolean ok = !existeUsuario && !existeCorreo; 
        ok = ok && numCuentaEsValida && passCoinciden && passValida;
        
        
        if (ok) {
            String passHash = Cifrado.sha1(pass2);
            Usuario nuevo = new Usuario(usuario, correo, passHash);
            try {
                nuevo.insertar();
            } catch (SQLException ex) {
                Logger.getLogger(RegistroUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
            SendGrid.enviarCorreo(correo+"@ciencias.unam.mx", "[PuertoCiencias] Confirmar Registro", "Hola, aquí debe ir un link para confirmar el correo.\nAhi había una nueva línea.\nSaludos.");
            
            
    
        }
        
        

        /*
         hay que ver que ningun campo sea vacio, que las contrasenas sean iguales,
         hay que hashearlas, el usuario no puede existir y el num de cuenta debe
         ser valido, si no es valido entonces regresarlo a la ventana anterior.
        
         */
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegistroUsuario</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegistroUsuario at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }


    /**
     * Dice si la contrasena tiene el formato que dimos en el SRS
     * @param pass la contrasena
     * @return True si sí, False si no
     */
    private static boolean contrasenaValida(String pass) {
        boolean ok = true;
        ok = ok && pass.length() >= 6;
        ok = ok && pass.length() <= 16;
        for (int i=1 ; i<pass.length(); i++){
            ok = ok && Character.isLetterOrDigit(pass.charAt(i));
        }
        return ok;
    }
    
    /**
     * Toma un string y devuelve la primera parte hasta encontrar un @
     * @param correo
     * @return 
     */
    private static String limpiaCorreo(String correo) {
        String[] arr = correo.split("@");
        return arr[0];
    }
    
    
    /**
     * Revisa que el dígito verificador coincida con el número de cuenta
     * @param cuenta Una string a la cual le saca los números. Puede tener guiones y más basura
     * @return True si es válido, false eoc
     */
    private static boolean numCuentaValido(String cuenta) {
        int tam = "411001896".length();
        char[] arreglo = cuenta.toCharArray();
        String numeros = "";
        for (int i = 0; i < cuenta.length(); i++) {
            if (Character.isDigit(arreglo[i])) {
                numeros += arreglo[i];
            }
        }
        System.out.println(numeros);
        if (numeros.length() == tam) {
            return validaCuenta(numeros);
        }
        return false;

    }

    /**
     * Esta ya recibe el string se solo números. La llama la otra función, no
     * debo usarla fuera de eso.
     * @param cuenta
     * @return 
     */
    private static boolean validaCuenta(String cuenta) {
        int suma = 0;
        for (int i = 0; i < cuenta.length() - 1; i++) {
            int num = Integer.parseInt("" + cuenta.charAt(i));
            if (i % 2 == 0) {
                suma += 3 * num;
            } else {
                suma += 7 * num;
            }
        }

        int verificador = Integer.parseInt("" + cuenta.charAt(cuenta.length() - 1));
        System.out.println(verificador);
        return verificador == suma % 10;
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
