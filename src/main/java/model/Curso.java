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

      public List<Comentario> obtenerComentarios(boolean orderByTimestamp, boolean orderByAsc) {
            return Comentario.encuentraComentarios(idProfesor, idMateria, orderByTimestamp, orderByAsc);
      }
      
      public void publicarComentario(String usuario, String comentario) throws SQLException {
            Comentario c = Comentario.hacerComentarioParaPublicar(idCurso, usuario, comentario);
            c.insertar();
      }
      
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

}
