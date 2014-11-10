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

/**
 *
 * @author diego
 */
public class Profesor {
      private int idProfesor;
      private String nombre;

      public Profesor(int idProfesor, String nombre) {
            this.idProfesor = idProfesor;
            this.nombre = nombre;
      }
      
      
      
      /**
       * Este método busca todos los comentarios en la base de datos sobre este profesor.
       * Cada valor del analisis sentimental lo discretiza a uno de cinco valores.
       * Muy malo, malo, super x, bueno, muy bueno.
       * @param idProfesor
       * @return Regresa el arreglo para hacer el histograma que encontró soni.
       */
      public static int[] sumarizado(int idProfesor) {
            int[] sumarizado = new int[5];
            for (int i=0; i<5; sumarizado[i++] = 0) {}
            String query = "select valNum from curso join comentario ON(curso.idCurso = comentario.curso) where curso.Profesor = ?";
            
            
            Connection conn = null;
            PreparedStatement stmt = null;
            try {

                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement(query);
                  stmt.setInt(1, idProfesor);
                  ResultSet result = stmt.executeQuery();

                  while (result.next()) {
                        double valNum = result.getDouble("valNum");
                        sumarizado[discretiza(valNum)]++;
                        
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
            return sumarizado;
            
      }
      
      /**
       * Este método busca todos los comentarios en la base de datos sobre este profesor.
       * Cada valor del analisis sentimental lo discretiza a uno de cinco valores.
       * Muy malo, malo, super x, bueno, muy bueno.
       * 
       * Devuelve un arreglo de 5 enteros contando cuántos comentarios quedaron en cada rango
       * en la posición 0 los muy malos y en la 4 los muy buenos.
       * @return 
       */
      public int[] sumarizado() {
            return sumarizado(this.idProfesor);
      }
      
      private static int discretiza(double val) {
            if (val < -0.65) {
                  return 0;
            }
            if (val >= -0.7 && val < -0.25) {
                  return 1;
            }
            if (val >= -0.25 && val < 0.25) {
                  return 2;
            }
            if (val >= 0.25 && val < 0.65) {
                  return 3;
            }
            return 4;
      } 

      
      
      
      public int getIdProfesor() {
            return idProfesor;
      }

      public void setIdProfesor(int idProfesor) {
            this.idProfesor = idProfesor;
      }

      public String getNombre() {
            return nombre;
      }

      public void setNombre(String nombre) {
            this.nombre = nombre;
      }
      
      
}
