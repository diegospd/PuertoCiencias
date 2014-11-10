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
import java.util.Objects;

/**
 *
 * @author diego
 */
public class Plan {
      private int idPlan;
      private String carrera;
      private int ano; //jeje je

      public Plan(int idPlan, String carrera, int ano) {
            this.idPlan = idPlan;
            this.carrera = carrera;
            this.ano = ano;
      }
      
      
      public List<Materia> materiasPorSemestre(int semestre) {
            return Materia.materiasPorPlanSemestre(idPlan, semestre);
      }
      
      public static List<Plan> selectAll() {
            String query = "Select * from plan";
            List<Plan> todo = new LinkedList<Plan>();
            
            Connection conn = null;
            PreparedStatement stmt = null;
            try {

                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement(query);
                  ResultSet result = stmt.executeQuery();

                  while (result.next()) {
                        int idPlan = result.getInt("idPlan");
                        String carrera = result.getString("carrera");
                        int ano = result.getInt("año");
                        
                        Plan p = new Plan(idPlan, carrera, ano);
                        todo.add(p);
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

      public int getIdPlan() {
            return idPlan;
      }

      public void setIdPlan(int idPlan) {
            this.idPlan = idPlan;
      }

      public String getCarrera() {
            return carrera;
      }

      public void setCarrera(String carrera) {
            this.carrera = carrera;
      }

      public int getAno() {
            return ano;
      }

      public void setAno(int ano) {
            this.ano = ano;
      }

      @Override
      public int hashCode() {
            int hash = 7;
            hash = 37 * hash + this.idPlan;
            hash = 37 * hash + Objects.hashCode(this.carrera);
            hash = 37 * hash + this.ano;
            return hash;
      }

      @Override
      public boolean equals(Object obj) {
            if (obj == null) {
                  return false;
            }
            if (getClass() != obj.getClass()) {
                  return false;
            }
            final Plan other = (Plan) obj;
            if (this.idPlan != other.idPlan) {
                  return false;
            }
            if (!Objects.equals(this.carrera, other.carrera)) {
                  return false;
            }
            if (this.ano != other.ano) {
                  return false;
            }
            return true;
      }

      @Override
      public String toString() {
            return "Plan{" + "idPlan=" + idPlan + ", carrera=" + carrera + ", ano=" + ano + '}';
      }
      
      
      
}
