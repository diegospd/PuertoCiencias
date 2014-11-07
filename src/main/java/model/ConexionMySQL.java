/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author diego
 */
public class ConexionMySQL {
    
    //WARNING quiza el mvc tenga que ser el nombre del proyecto, pero no creo
    private static final String database = "proyectoingsoft";
    private static final String server = "jdbc:mysql://localhost:3306/" + database;
    private static final String login = "root";
    private static final String pass = "Halderman";

    
    /**
     * Regresa una conexion con la base de datos.
     * @return
     * @throws SQLException 
     */
    public static Connection darConexion() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection mySqlConn = (Connection) DriverManager.getConnection(server, login, pass);
            return mySqlConn;
        } catch (ClassNotFoundException e) {
           return null;
        }
    
    }
    
    
    
    
}
