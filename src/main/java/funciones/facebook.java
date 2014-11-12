/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones;

import constantes.Constantes;
import static constantes.Constantes.DIR_PUERTO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author diego
 */
@WebServlet(name = "facebook", urlPatterns = {"/facebook"})
public class facebook extends HttpServlet {

      private static final String FB_API_SECRET = "c2828bdb97414306bf4de89fb429d6ac";
      private static final String FB_APP_ID = "732943860124575";

      public void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
            String code = req.getParameter("code");
            String state = req.getParameter("state");
            int idCuenta = Integer.parseInt(state);

            if (code == null || code.equals("")) {
                  // an error occurred, handle this
            }

            String token = null;
            try {
                  String g = "https://graph.facebook.com/oauth/access_token?client_id=";
                  g += FB_APP_ID + "&redirect_uri=";
                  g += URLEncoder.encode(Constantes.DIR_PUERTO + "facebook", "UTF-8");
                  g += "&client_secret=" + FB_API_SECRET + "&code=" + code;

                  URL u = new URL(g);
                  URLConnection c = u.openConnection();
                  BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
                  String inputLine;
                  StringBuffer b = new StringBuffer();
                  while ((inputLine = in.readLine()) != null) {
                        b.append(inputLine + "\n");
                  }
                  in.close();
                  token = b.toString();
                  
                  if (token.startsWith("{")) {
                        throw new Exception("error on requesting token: " + token + " with code: " + code);
                  }
            } catch (Exception e) {
                  // an error occurred, handle this
            }

            token = token.split("=")[1];
            Usuario.actualizarFBtoken(idCuenta, token);
            res.sendRedirect(DIR_PUERTO + "faces/principal.xhtml");

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
