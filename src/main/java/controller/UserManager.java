/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import model.Usuario;

/**
 *
 * @author diego
 */
@ManagedBean(name = "userManager")
public class UserManager {

      /**
       * <p>
       * The key for the session scoped attribute holding the appropriate <code>Wuser</code> instance.</p>
       */
      public static final String USER_SESSION_KEY = "user";

      /**
       * <p>
       * The transaction resource.</p>
       */
      @Resource
      private UserTransaction utx;

      /**
       * <p>
       * User properties.</p>
       */
      private int idCuenta;
      private String username;
      private String pass1;
      private String pass2;
      private String correo;
      private String codigo;
      private String numCuenta;
      private boolean activada;

      /**
       * <p>
       * Creates a new <code>Usuario</code>. If the specified user name exists or an error occurs when persisting the Wuser instance, enqueue a
       * message detailing the problem to the <code>FacesContext</code>. If the user is created, move the user back to the login view.</p>
       *
       * @return <code>login</code> if the user is created, otherwise returns <code>null</code>
       */
      public String createUser() {
            FacesContext context = FacesContext.getCurrentInstance();
            Usuario wuser = getUser();
            //Cuando no existe el username en la DB
            if (wuser == null) {
                  //Las dos contraseñas deben coincidir
                  if (!pass1.equals(pass2)) {
                        FacesMessage message = new FacesMessage("Las contraseñas no coinciden. Escríbelas de nuevo.");
                        context.addMessage(null, message);
                        return null;
                  }

                  //Deben de tener el formato correcto
                  if (!contrasenaValida(pass1)) {
                        FacesMessage message = new FacesMessage("La contraseña debe tener entre 6 y 16 caracteres alfanuméricos.");
                        context.addMessage(null, message);
                        return null;
                  }

                  //El número de cuenta debe ser válido
                  if (!numCuentaValido(numCuenta)) {
                        FacesMessage message = new FacesMessage("El número de cuenta no es válido. Deben de ser 9 dígitos.");
                        context.addMessage(null, message);
                        return null;
                  }

                  //Si el correo ya está 
                  if (Usuario.existeCorreo(correo)) {
                        FacesMessage message = new FacesMessage("Este correo ya está registrado. Puedes recuperar tu contraseña");
                        context.addMessage(null, message);
                        return null;
                  }

                  //Si todo sale bien entonces creo el usuario en la base de datos y mando el mail de confirmación
                  try {

                        wuser = Usuario.hacerUsuarioDeRegistro(username, correo, pass1);
                        wuser.insertar();
                        wuser.mandarVerificacionCorreo();

                        return "login";

                  } catch (Exception e) {
                        //Estas excepciones no deben de pasar.
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error creating user!",
                                "Unexpected error when creating your account.  Please contact the system Administrator");
                        context.addMessage(null, message);
                        Logger.getAnonymousLogger().log(Level.SEVERE,
                                "Unable to create new user",
                                e);
                        return null;
                  }

                  //Si ya existe el usuario le aviso para que elija uno distinto
            } else {
                  FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                          "El usuario '"
                          + username
                          + "' ya existe.  ",
                          "Elige uno distinto.");
                  context.addMessage(null, message);
                  return null;
            }
      }

      /**
       * <p>
       * When invoked, it will invalidate the user's session and move them to the login view.</p>
       *
       * @return <code>login</code>
       */
      public String logout() {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                  session.invalidate();
            }
            return "login";
      }

//      public boolean isLoggedIn() {
//            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//            return session != null;
//      }
      /**
       * <p>
       * This will attempt to lookup a <code>Usuario</code> object based on the provided username.</p>
       *
       * @return a <code>Usuario</code> object associated with the current username, otherwise, if no <code>Usuario</code> can be found, returns
       * <code>null</code>
       */
      private Usuario getUser() {
            return Usuario.encontrarPorUsername(username);
      }

      /**
       * <p>
       * Validates the user. If the user doesn't exist or the password is incorrect, the appropriate message is added to the current
       * <code>FacesContext</code>. If the user successfully authenticates, navigate them to the page referenced by the outcome <code>app-main</code>.
       * </p>
       *
       * @return <code>app-main</code> if the user authenticates, otherwise returns <code>null</code>
       */
      public String validateUser() {
            FacesContext context = FacesContext.getCurrentInstance();
            Usuario user = getUser();
            if (user != null) {
                  if (!user.isActivada()) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Login Failed!",
                                "Tu cuenta no ha sido activada. Necesitas verificar tu correo.");
                        context.addMessage(null, message);
                        return null;
                  }

                  if (!user.passwordCorrecta(pass1)) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Login Failed!",
                                "La contraseña no es correcta!");
                        context.addMessage(null, message);
                        return null;
                  }

                  context.getExternalContext().getSessionMap().put(USER_SESSION_KEY, user);
                  return "app-main";
            } else {
                  FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                          "Login Failed!",
                          "El usuario '"
                          + username
                          + "' no existe.");
                  context.addMessage(null, message);
                  return null;
            }
      }
      
      
          
      
      public static void mandarVerificacionCorreo(Usuario usuario) {
            String texto = "¡Hola, " + usuario.getUsername() + "!\n\n";
            texto += "Te damos la bienvenida a Puerto Ciencias.\nPara poder usar tu cuenta por favor accede al siguiente enlace.\n\n";
            texto += constantes.Constantes.DIR_PUERTO + "VerificarCorreo?codigo=" + usuario.getCodigo() + "\n\n\n";
            texto += "Que la fuerza te acompañe,\nAstillero Ciencias";

            SendGrid.enviarCorreo(usuario.getCorreo() + "@ciencias.unam.mx", "[PuertoCiencias] Confirmar Registro", texto);
      }

      /**
       * Dice si la contrasena tiene el formato que dimos en el SRS
       *
       * @param pass la contrasena
       * @return True si sí, False si no
       */
      protected static boolean contrasenaValida(String pass) {
            boolean ok = true;
            ok = ok && pass.length() >= 6;
            ok = ok && pass.length() <= 16;
            for (int i = 1; i < pass.length(); i++) {
                  ok = ok && Character.isLetterOrDigit(pass.charAt(i));
            }
            return ok;
      }

      /**
       * Toma un string y devuelve la primera parte hasta encontrar un @
       *
       * @param correo
       * @return
       */
      protected static String limpiaCorreo(String correo) {
            String[] arr = correo.split("@");
            return arr[0];
      }

      /**
       * Revisa que el dígito verificador coincida con el número de cuenta
       *
       * @param cuenta Una string a la cual le saca los números. Puede tener guiones y más basura
       * @return True si es válido, false eoc
       */
      protected static boolean numCuentaValido(String cuenta) {
            if (cuenta == null) {
                  return false;
            }

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
       * Esta ya recibe el string se solo números. La llama la otra función, no debo usarla fuera de eso.
       *
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

      

      public String getUsername() {
            return username;
      }

      public void setUsername(String username) {
            this.username = username;
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

      public String getCorreo() {
            return correo;
      }

      public void setCorreo(String correo) {
            this.correo = correo;
      }

      public String getCodigo() {
            return codigo;
      }

      public void setCodigo(String codigo) {
            this.codigo = codigo;
      }

      public String getNumCuenta() {
            return numCuenta;
      }

      public void setNumCuenta(String numCuenta) {
            this.numCuenta = numCuenta;
      }

      public boolean isActivada() {
            return activada;
      }

      public void setActivada(boolean activada) {
            this.activada = activada;
      }

}
