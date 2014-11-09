/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import model.Usuario;

/**
 *
 * @author diego
 */
@ManagedBean(name = "recuperar")
public class Recuperar {

      private String correo;
      private String numCuenta;
      private String pass1;
      private String pass2;

      private static void mandarCorreoRecuperacion(String correo, String password) {
            String hash = funciones.Cifrado.sha1(password);
            String codigo = funciones.Cifrado.sha1(correo + password + hash);
            model.Usuario.nuevoCodigo(correo, codigo);
            String texto = "Hola!\n\nHas solicitado cambiar tu contraseña de Puerto Ciencias.\n";
            texto += "La contraseña que elegiste fue: " + password + "\n\n";
            texto += "Si fuiste tú quien solicitó el cambio, puedes acceder al siguiente enlace para que tome efecto.\n\n";
            texto += constantes.Constantes.DIR_PUERTO + "ConfirmarCambioPassword?codigo=" + codigo + "&h=" + hash;
            texto += "\n\nQue la fuerza te acompañe,\nAstillero Ciencias";
            SendGrid.enviarCorreo(correo + "@ciencias.unam.mx", "[Puerto Ciencias] Confirma tu cambio de contraseña", texto);
      }

      public String recuperar() {
            FacesContext context = FacesContext.getCurrentInstance();
            if (!UserManager.numCuentaValido(numCuenta)) {
                  FacesMessage message = new FacesMessage("El número de cuenta no es válido. Deben de ser 9 dígitos.");
                  context.addMessage(null, message);
                  return null;
            }

            if (!pass1.equals(pass2)) {
                  FacesMessage message = new FacesMessage("Las contraseñas no coinciden. Escríbelas de nuevo.");
                  context.addMessage(null, message);
                  return null;
            }

            //Deben de tener el formato correcto
            if (!UserManager.contrasenaValida(pass1)) {
                  FacesMessage message = new FacesMessage("La contraseña debe tener entre 6 y 16 caracteres alfanuméricos.");
                  context.addMessage(null, message);
                  return null;
            }

            if (Usuario.existeCorreo(correo)) {
                  mandarCorreoRecuperacion(correo, pass1);
            }
            return "app-main";
      }

      public String getCorreo() {
            return correo;
      }

      public void setCorreo(String correo) {
            this.correo = correo;
      }

      public String getNumCuenta() {
            return numCuenta;
      }

      public void setNumCuenta(String numCuenta) {
            this.numCuenta = numCuenta;
      }

      public String getPass1() {
            return pass1;
      }

      public void setPass1(String pass1) {
            this.pass1 = pass1;
      }

      public String getPass2() {
            return pass2;
      }

      public void setPass2(String pass2) {
            this.pass2 = pass2;
      }

      
      
      
}
