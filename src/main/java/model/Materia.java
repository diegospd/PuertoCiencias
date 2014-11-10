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
public class Materia {

      private int idMateria;
      private String nombre;

      public Materia(int idMateria, String nombre) {
            this.idMateria = idMateria;
            this.nombre = nombre;
      }

      /**
       * Ya que tenemos la materia, para un plan de estudios y un semestre podemos
       * obtener la lista de cursos para esta materia.
       * Cada curso tiene al profesor y el nombre de la materia.
       * 
       * El curso que devuelve tiene métodos para obtener todos los comentarios, publicar un nuevo
       * comentario y obtener el profesor de la materia. El profesor tiene un método para
       * dar el sumarizado.
       * 
       * @return 
       */
      public List<Curso> obtenerCursos() {
            return Curso.cursosPorMateria(idMateria);
      }
      
      /**
       * Toma el id de un plan, un número de semestre y devulve una lista de todas las materias que se
       * están dando para ese plan
       * @param planId
       * @param numSemestre
       * @return 
       */
      public static List<Materia> materiasPorPlanSemestre(int planId, int numSemestre) {
            String query = "SELECT materia.idMateria, materia.nombre FROM materiaplan join materia ";
            query += " ON (materiaplan.materia = materia.idMateria) where plan=? and semestre=?";

            List<Materia> todo = new LinkedList<Materia>();

            Connection conn = null;
            PreparedStatement stmt = null;
            try {

                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement(query);
                  stmt.setInt(1, planId);
                  stmt.setInt(2, numSemestre);
                  ResultSet result = stmt.executeQuery();

                  while (result.next()) {
                        int idMateria = result.getInt("idMateria");
                        String nombre = result.getString("nombre");
 
                        Materia m = new Materia(idMateria, nombre);
                        todo.add(m);
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

      public int getIdMateria() {
            return idMateria;
      }

      public void setIdMateria(int idMateria) {
            this.idMateria = idMateria;
      }

      public String getNombre() {
            return nombre;
      }

      public void setNombre(String nombre) {
            this.nombre = nombre;
      }

      @Override
      public String toString() {
            return "Materia{" + "idMateria=" + idMateria + ", nombre=" + nombre + '}';
      }
      
      

}
