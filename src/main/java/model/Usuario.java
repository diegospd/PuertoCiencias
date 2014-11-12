/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.SendGrid;
import static funciones.Cifrado.sha1;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class Usuario {

      private final int idCuenta;
      private String username;
      private String correo;
      private String pass;
      private String codigo;
      private boolean activada;

      /**
       * Hace un Usuario con los datos tal cual se le pasan.
       *
       * @param idCuenta
       * @param username
       * @param correo
       * @param pass
       * @param codigo
       * @param activada
       */
      public Usuario(int idCuenta, String username, String correo, String pass, String codigo, boolean activada) {
            this.idCuenta = idCuenta;
            this.username = username;
            this.correo = correo;
            this.pass = pass;
            this.codigo = codigo;
            this.activada = activada;
      }

      /**
       * Hace un Usuario. La password es la misma que se le pasa. El codigo es hash de username correo y pass el id es el máximo + 1
       *
       * @param username
       * @param correo
       * @param pass Debe de estar hasheada antes
       */
      public Usuario(String username, String correo, String pass) {
            this.idCuenta = getMaxId() + 1;
            this.username = username;
            this.correo = correo;
            this.pass = pass;
            this.codigo = sha1(username + correo + pass);
            this.activada = false;
      }

      /**
       * Este es el usuario que creo para registrar gente.
       *
       * @param username
       * @param correo
       * @param pass
       * @return
       */
      public static Usuario hacerUsuarioDeRegistro(String username, String correo, String pass) {
            pass = sha1(pass);
            username = username.trim();
            correo = correo.trim();
            String codigo = sha1(username + correo + pass);
            int idCuenta = getMaxId() + 1;
            return new Usuario(idCuenta, username, correo, pass, codigo, false);
      }

      /**
       * Dice si el hash del password del objeto is igual al hash del password que se le pasa.
       *
       * @param password
       * @return
       */
      public boolean passwordCorrecta(String password) {
            return this.pass.equals(sha1(password.trim()));
      }

      public void mandarVerificacionCorreo() {

            String texto = "¡Hola, " + getUsername() + "!\n\n";
            texto += "Te damos la bienvenida a Puerto Ciencias.\nPara poder usar tu cuenta por favor accede al siguiente enlace.\n\n";
            texto += constantes.Constantes.DIR_PUERTO + "VerificarCorreo?codigo=" + getCodigo() + "\n\n\n";
            texto += "Que la fuerza te acompañe,\nAstillero Ciencias";
            SendGrid.enviarCorreo(getCorreo() + "@ciencias.unam.mx", "[PuertoCiencias] Confirmar Registro", texto);
      }

      /**
       * Recibe una cadena con el password, le aplica el hash y lo cambia como nueva contrasena en la base de datos
       *
       * @param nuevaPassword
       */
      public void cambiarPassword(String nuevaPassword) {
            nuevaPassword = sha1(nuevaPassword);
            Connection conn = null;
            PreparedStatement stmt = null;
            int nr = -1;
            try {

                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("UPDATE usuario set pass = ? where idCuenta = ?");
                  stmt.setString(1, nuevaPassword);
                  stmt.setInt(2, this.idCuenta);
                  nr = stmt.executeUpdate();

            } catch (SQLException e) {
                  //No Quiero que pase
            } finally {

                  if (stmt != null) {
                        try {
                              stmt.close();
                        } catch (SQLException ex) {
                              //Que no pasa!
                        }
                  }

                  if (conn != null) {
                        try {
                              conn.close();
                        } catch (SQLException ex) {
                              //Tampoco acá
                        }
                  }

            }
      }

      /**
       * Busca por username y devuelve un Usuario con los datos de la DB o null si no ecnotnro.
       *
       * @param username
       * @return
       */
      public static Usuario encontrarPorUsername(String username) {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {

                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("SELECT idCuenta, username, correo, pass, codigo, activada FROM usuario WHERE username = ?");
                  stmt.setString(1, username.trim());
                  ResultSet result = stmt.executeQuery();

                  while (result.next()) {
                        int idCuenta = result.getInt("idCuenta");
                        String correo = result.getString("correo");
                        String codigo = result.getString("codigo");
                        String pass = result.getString("pass");
                        boolean activada = result.getBoolean("activada");

                        return new Usuario(idCuenta, username, correo, pass, codigo, activada);
                  }

                  return null;

            } catch (SQLException e) {
                  //No Quiero que pase
            } finally {

                  if (stmt != null) {
                        try {
                              stmt.close();
                        } catch (SQLException ex) {
                              //Que no pasa!
                        }
                  }

                  if (conn != null) {
                        try {
                              conn.close();
                        } catch (SQLException ex) {
                              //Tampoco acá
                        }
                  }

            }
            return null;
      }

      /**
       * Dice si el correo existe en la base de datos
       *
       * @param correo el correo sin @ciencias.unam.mx
       * @return True si existe, false si no
       */
      public static boolean existeCorreo(String correo) {
            Connection conn = null;
            PreparedStatement stmt = null;
            boolean existe = false;
            try {

                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("Select count(*) as count from usuario where correo=?");
                  stmt.setString(1, correo.trim());
                  ResultSet result = stmt.executeQuery();

                  while (result.next()) {
                        existe = (Integer.parseInt(result.getString("count")) > 0) ? true : existe;
                  }

            } catch (SQLException e) {
                  //No Quiero que pase
            } finally {

                  if (stmt != null) {
                        try {
                              stmt.close();
                        } catch (SQLException ex) {
                              //Que no pasa!
                        }
                  }

                  if (conn != null) {
                        try {
                              conn.close();
                        } catch (SQLException ex) {
                              //Tampoco acá
                        }
                  }

            }
            return existe;
      }

      /**
       * Dice si un username existe en la base de datos;
       *
       * @param username
       * @return True si existe false si no
       */
      public static boolean existeUsuario(String username) {
            username = username.trim();
            Connection conn = null;
            PreparedStatement stmt = null;
            boolean existe = false;
            try {
                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("SELECT count(*) as count from usuario where username=?");
                  stmt.setString(1, username);
                  ResultSet result = stmt.executeQuery();
                  while (result.next()) {
                        int val = Integer.parseInt(result.getString("count"));
                        if (val > 0) {
                              existe = true;
                        }
                  }
            } catch (SQLException e) {
            } finally {
                  if (stmt != null) {
                        try {
                              stmt.close();
                        } catch (SQLException ex) {
                        }
                  }
                  if (conn != null) {
                        try {
                              conn.close();
                        } catch (SQLException ex) {
                        }
                  }
            }
            return existe;
      }

      /**
       * Obtiene el id más grande que haya en la tabla usuario
       *
       * @return
       * @throws SQLException
       */
      private static int getMaxId() {
            Connection conn = null;
            Statement stmt = null;
            ResultSet result = null;
            int maxId = 0;
            try {
                  conn = ConexionMySQL.darConexion();
                  stmt = conn.createStatement();
                  //El ifnull es para que regrese cero si no hay máximo cuando la tabla está vacía.
                  result = stmt.executeQuery("SELECT IfNull(MAX(idCuenta),0) AS max from usuario");
                  while (result.next()) {
                        maxId = Integer.parseInt(result.getString("max"));
                  }

            } catch (SQLException e) {
            } finally {
                  try {
                        if (stmt != null) {
                              stmt.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
                  try {
                        if (conn != null) {
                              conn.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
            }
            return maxId;
      }

      /**
       * Inserta al usuario en la base de datos
       *
       * @throws SQLException
       */
      public void insertar() throws SQLException {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                  conn = ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("INSERT INTO usuario (idCuenta, username, correo, pass, codigo) values (?, ?, ?, ?, ?)");
                  stmt.setInt(1, getIdCuenta());
                  stmt.setString(2, getUsername());
                  stmt.setString(3, getCorreo());
                  stmt.setString(4, getPass());
                  stmt.setString(5, getCodigo());
                  int nr = stmt.executeUpdate();
            } finally {
                  try {
                        if (stmt != null) {
                              stmt.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
                  try {
                        if (conn != null) {
                              conn.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
            }
      }

      /**
       * Dice si hay algun registro en la DB con el codigo dado.
       *
       * @param codigo
       * @return true si si, false si no.
       */
      public static boolean existeCodigo(String codigo) {
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet result = null;
            boolean existe = false;
            try {
                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("SELECT count(*) as count from usuario where codigo=?");
                  stmt.setString(1, codigo);
                  result = stmt.executeQuery();
                  while (result.next()) {
                        int val = Integer.parseInt(result.getString("count"));
                        if (val > 0) {
                              existe = true;
                        }
                  }
            } catch (SQLException e) {
            } finally {
                  if (stmt != null) {
                        try {
                              stmt.close();
                        } catch (SQLException ex) {
                        }
                  }
                  if (conn != null) {
                        try {
                              conn.close();
                        } catch (SQLException ex) {
                        }
                  }
            }
            return existe;

      }

      /**
       * Busca el codigo y cuando lo encuentra lo hace null y cambia el hash del password por que se le pasa. No avisa si hace cambio o no, debe de
       * ser llamado sólo si existeCodigo(codigo) fue true
       *
       * @param codigo
       * @param nuevoHash
       */
      public static void verificarCambioPassword(String codigo, String nuevoHash) {

            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                  conn = ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("UPDATE usuario SET pass = ?, codigo = Null  WHERE codigo = ?");
                  stmt.setString(1, nuevoHash);
                  stmt.setString(2, codigo);
                  int nr = stmt.executeUpdate();

            } catch (SQLException ex) {
                  Logger.getLogger(Usuario.class
                          .getName()).log(Level.SEVERE, null, ex);
            } finally {
                  try {
                        if (stmt != null) {
                              stmt.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
                  try {
                        if (conn != null) {
                              conn.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
            }

      }

      /**
       * Busca en la tabla usuario por el correo y actualiza el codigo al que se le da
       *
       * @param correo
       * @param codigo
       */
      public static void nuevoCodigo(String correo, String codigo) {

            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                  conn = ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("UPDATE usuario SET codigo = ?  WHERE correo = ?");
                  stmt.setString(1, codigo);
                  stmt.setString(2, correo);
                  int nr = stmt.executeUpdate();

            } catch (SQLException ex) {
                  Logger.getLogger(Usuario.class
                          .getName()).log(Level.SEVERE, null, ex);
            } finally {
                  try {
                        if (stmt != null) {
                              stmt.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
                  try {
                        if (conn != null) {
                              conn.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
            }

      }

      public String obtenerFBtoken() {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {

                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("select fb_token from usuario where idCuenta = ?");
                  stmt.setInt(1, idCuenta);
                  ResultSet result = stmt.executeQuery();

                  while (result.next()) {
                        String token = result.getString("fb_token");
                        return token;
                  }


            } catch (SQLException e) {
                  //No Quiero que pase
            } finally {

                  if (stmt != null) {
                        try {
                              stmt.close();
                        } catch (SQLException ex) {
                              //Que no pasa!
                        }
                  }

                  if (conn != null) {
                        try {
                              conn.close();
                        } catch (SQLException ex) {
                              //Tampoco acá
                        }
                  }

            }
            return null;

      }

      public static void actualizarFBtoken(int idCuenta, String token) {
            Connection conn = null;
            PreparedStatement stmt = null;
            int nr;
            try {
                  conn = ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("Update usuario set fb_token = ? where idCuenta = ?");
                  stmt.setString(1, token);
                  stmt.setInt(2, idCuenta);

                  nr = stmt.executeUpdate();
            } catch (Exception e) {
            } finally {
                  try {
                        if (stmt != null) {
                              stmt.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
                  try {
                        if (conn != null) {
                              conn.close();
                        }
                  } catch (Exception e) {
                        // log this error
                  }
            }
      }

      /**
       * Recibe un codigo, busca en la tabla a quien tenga ese codigo. Cuando lo encuentra lo actualiza a null y en activada pone 1
       *
       * @param codigo Lo que va a buscar
       * @return true si hizo algún cambio en la BD, false eoc.
       */
      public static boolean verificarCorreo(String codigo) {
            if (existeCodigo(codigo)) {
                  Connection conn = null;
                  PreparedStatement stmt = null;
                  try {
                        conn = ConexionMySQL.darConexion();
                        stmt = conn.prepareStatement("UPDATE usuario SET codigo = NULL, activada = 1 WHERE codigo =?");
                        stmt.setString(1, codigo);
                        int nr = stmt.executeUpdate();
                        return true;

                  } catch (SQLException ex) {
                        Logger.getLogger(Usuario.class
                                .getName()).log(Level.SEVERE, null, ex);
                  } finally {
                        try {
                              if (stmt != null) {
                                    stmt.close();
                              }
                        } catch (Exception e) {
                              // log this error
                        }
                        try {
                              if (conn != null) {
                                    conn.close();
                              }
                        } catch (Exception e) {
                              // log this error
                        }
                  }
            }
            return false;
      }

      // <editor-fold defaultstate="collapsed" desc="Verborrea: Getters y Setters.">
      @Override
      public String toString() {
            return "Usuario{" + "idCuenta=" + idCuenta + ", username=" + username + ", correo=" + correo + ", pass=" + pass + ", codigo=" + codigo + ", activada=" + activada + '}';
      }

      public int getIdCuenta() {
            return idCuenta;
      }

      public String getUsername() {
            return username;
      }

      public void setUsername(String username) {
            this.username = username;
      }

      public String getCorreo() {
            return correo;
      }

      public void setCorreo(String correo) {
            this.correo = correo;
      }

      public String getPass() {
            return pass;
      }

      public void setPass(String pass) {
            this.pass = pass;
      }

      public String getCodigo() {
            return codigo;
      }

      public void setCodigo(String codigo) {
            this.codigo = codigo;
      }

      public boolean isActivada() {
            return activada;
      }

      public void setActivada(boolean activada) {
            this.activada = activada;
      }
      //</editor-fold>

}
