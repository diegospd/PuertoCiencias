/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author diego
 */
public class SendGrid {
    
    /**
     * Manda un correo electrónico
     * @param para La dirección a la que se manda
     * @param asunto El asunto del correo
     * @param texto El cuerpo del correo
     */
    public static void enviarCorreo(String para, String asunto, String texto) {
        try {
            texto = URLEncoder.encode(texto, "UTF-8");
            asunto = URLEncoder.encode(asunto, "UTF-8");
            URL sendgrid = new URL("https://api.sendgrid.com/api/mail.send.json?api_"
                    + "user=josesoni1&api_key=Puerto123&to="+para
                    + "&subject="+asunto+"&text="+texto+"&from=josesoni1@gmail.com");
            BufferedReader in;
            in = new BufferedReader(
                    new InputStreamReader(sendgrid.openStream()));
            
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(SendGrid.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SendGrid.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
