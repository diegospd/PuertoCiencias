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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class Curso {

      private int idCurso;
      private int idMateria;
      private int idProfesor;
      private String materia;
      private String profesor;

      public Curso(int idCurso, int idMateria, int idProfesor, String materia, String profesor) {
            this.idCurso = idCurso;
            this.idMateria = idMateria;
            this.idProfesor = idProfesor;
            this.materia = materia;
            this.profesor = profesor;
      }

      /**
       * Con este método obtenemos la lista de comentarios.
       * @param orderByTimestamp Si esto es true entonces se ordenan por la fecha de publicacion, si es false se ordenan por número de votos
       * @param orderByAsc si es true se ordenan ascendentemente si es false se ordenan descendentemente. 
       * @return 
       */
      public List<Comentario> obtenerComentarios(boolean orderByTimestamp, boolean orderByAsc) {
            return Comentario.encuentraComentarios(idProfesor, idMateria, orderByTimestamp, orderByAsc);
      }
      
      /**
       * Con esto publicamos un nuevo comentario
       * @param usuario El nombre de usuario
       * @param comentario El comentario que va a publicar.
       */
      public void publicarComentario(String usuario, String comentario) {
            Comentario c = Comentario.hacerComentarioParaPublicar(idCurso, usuario, comentario);
            try {
                  c.insertar();
            } catch (SQLException ex) {
                  Logger.getLogger(Curso.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
      
      /**
       * Devuelve el profesor de este curso. El profesor tiene un método para obtener el sumarizado.
       * @return 
       */
      public Profesor obtenerProfesor() {
            return new Profesor(idProfesor, profesor);
      }
      
      /**
       * Regresa una lista con los profesores que dan una materia.
       *
       * @param idMateria
       * @return
       */
      public static List<Curso> cursosPorMateria(int idMateria) {
            String query = "select idMateria, idCurso, idProfesor, materia.nombre as materia, profesor.nombre as profesor ";
            query += "from materia join curso join profesor ";
            query += " on (materia.idMateria = curso.materia and curso.profesor = profesor.idprofesor) where curso.materia = ?";
            
            
            List<Curso> todo = new LinkedList<Curso>();

            Connection conn = null;
            PreparedStatement stmt = null;
            try {

                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement(query);
                  stmt.setInt(1, idMateria);
                  ResultSet result = stmt.executeQuery();

                  while (result.next()) {
                        int idCurso = result.getInt("idCurso");
                        int idProfesor = result.getInt("idProfesor");
                        String materia = result.getString("materia");
                        String profesor = result.getString("profesor");
                        
                        Curso c = new Curso(idCurso, idMateria, idProfesor, materia, profesor);
                        
                        todo.add(c);
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
            return todo;
      }

      public int getIdCurso() {
            return idCurso;
      }

      public void setIdCurso(int idCurso) {
            this.idCurso = idCurso;
      }

      public int getIdMateria() {
            return idMateria;
      }

      public void setIdMateria(int idMateria) {
            this.idMateria = idMateria;
      }

      public int getIdProfesor() {
            return idProfesor;
      }

      public void setIdProfesor(int idProfesor) {
            this.idProfesor = idProfesor;
      }

      public String getMateria() {
            return materia;
      }

      public void setMateria(String materia) {
            this.materia = materia;
      }

      public String getProfesor() {
            return profesor;
      }

      public void setProfesor(String profesor) {
            this.profesor = profesor;
      }

      
      
      
      
      
}
