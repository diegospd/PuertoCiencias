/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

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


    public Usuario(String username, String correo, String pass) {
        this.idCuenta = getMaxId() + 1;
        this.username = username;
        this.correo = correo;
        this.pass = pass;
        this.codigo = funciones.Cifrado.sha1(username + correo + pass);
        this.activada = false;
    }
    
    /**
     * Dice si el correo existe en la base de datos
     * @param correo el correo sin @ciencias.unam.mx
     * @return True si existe, false si no
     */
    public static boolean existeCorreo(String correo) {
        correo = correo.trim();
        Connection conn = null;
        ResultSet result = null;
        PreparedStatement stmt = null;
        boolean existe = false;
        try {
            conn = model.ConexionMySQL.darConexion();
            stmt = conn.prepareStatement("Select count(*) as count from usuario where correo=?");
            stmt.setString(1, correo);
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
     * Dice si un username existe en la base de datos;
     * @param username
     * @return  True si existe false si no
     */
    public static boolean existeUsuario(String username) {
        username = username.trim();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        boolean existe = false;
        try {
            conn = model.ConexionMySQL.darConexion();
            stmt = conn.prepareStatement("SELECT count(*) as count from usuario where username=?");
            stmt.setString(1, username);
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
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
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
