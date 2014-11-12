/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.alchemyapi.api.AlchemyAPI;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author diego
 */
public class Comentario {

      private final static String ALCHEMY_API_KEY = "65dc9c24facaefaacf4abe5ca703710288d52465";

      private int idComentario;
//      private Timestamp timestamp; No tiene timestamp porque echaba excecpiones
      private String usuario;
      private int curso;
      private double valNum;
      private String texto;
      private int votos;
      

      public Comentario(int idComentario,  String usuario, int curso, double valNum, String texto, int votos) {
            this.idComentario = idComentario;
//            this.timestamp = timestamp;
            this.usuario = usuario;
            this.curso = curso;
            this.valNum = valNum;
            this.texto = texto;
            this.votos = votos;
      }

      /**
       * Con este método podemos votar positiva o negativamente.
       *
       * @param positivo
       */
      public void votar(boolean positivo) {
            if (positivo) {
                  votos++;
            } else {
                  votos--;
            }

            int nr;
            String query = "Update comentario set votos = ? where idComentario = ?";

            Connection conn = null;
            PreparedStatement stmt = null;
            try {

                  conn = model.ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement(query);
                  stmt.setInt(1, votos);
                  stmt.setInt(2, idComentario);
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
       * Devuelve una lista de comentarios al buscar el id del profesor y de la materia.
       *
       * @param idProfesor
       * @param idMateria
       * @param orderByTimestamp Si es true esto entonces ordena por fecha de publicacion, si es false ordena por numero de votos
       * @param orderByAsc si es true ordena ascendentemente si es false ordena descendientemente, ya sea por numero de votos o fecha.
       * @return La lista de comentarios. En el peor de los casos regresa una lista vacía.
       */
      public static List<Comentario> encuentraComentarios(int idProfesor, int idMateria, boolean orderByTimestamp, boolean orderByAsc) {
            String query = "select * from curso join comentario ON(curso.idCurso = comentario.curso) where curso.Profesor = ? and curso.materia=? ";
            query += " ORDER BY ";
            query += orderByTimestamp ? " timestamp " : " votos ";
            query += orderByAsc ? " ASC " : " DESC ";

            List<Comentario> cs = new LinkedList<Comentario>();

            Connection conn = null;
            PreparedStatement stmt = null;
            try {

                  conn = ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement(query);
                  stmt.setInt(1, idProfesor);
                  stmt.setInt(2, idMateria);
                  ResultSet result = stmt.executeQuery();

                  while (result.next()) {
                        int idComentario = result.getInt("idComentario");
//                        Timestamp timestamp = result.getTimestamp("timestamp");
                        String usuario = result.getString("usuario");
                        int curso = result.getInt("curso");
                        double valNum = result.getDouble("valNum");
                        String texto = result.getString("texto");
                        int votos = result.getInt("votos");

                        Comentario c = new Comentario(idComentario, usuario, curso, valNum, texto, votos);
                        cs.add(c);
                  }

                  //Aquí debería haber un return cs; pero lo pongo hasta abajo para que no se queje java.
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
            return cs;
      }

      public static Comentario hacerComentarioParaPublicar(int curso, String usuario, String texto) {
//            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
            int idComentario = getMaxId() + 1;
            double valNum = sentimiento(texto);

            Comentario c = new Comentario(idComentario, usuario, curso, valNum, texto, 0);
            return c;
      }

      /**
       * Inserta un comentario creado por hacerComentarioParaPublicar
       *
       * @throws SQLException
       */
      public void insertar() throws SQLException {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                  conn = ConexionMySQL.darConexion();
                  stmt = conn.prepareStatement("insert into comentario (idComentario, usuario, curso, valNum, texto, votos) values (?,?,?,?,?,?)");
                  stmt.setInt(1, this.idComentario);
//                  stmt.setTimestamp(2, this.timestamp);
                  stmt.setString(2, this.usuario);
                  stmt.setInt(3, this.curso);
                  stmt.setDouble(4, this.valNum);
                  stmt.setString(5, this.texto);
                  stmt.setInt(6, this.votos);

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

      
      

      
      public static double sentimiento(String texto) {
            try {
                  AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString(ALCHEMY_API_KEY);
                  Document doc = alchemyObj.TextGetTextSentiment(texto);
                  Node score_n = doc.getElementsByTagName("score").item(0);
                  Node type_n = doc.getElementsByTagName("type").item(0);
                  String score_s = score_n.getTextContent();
                  String type_s = type_n.getTextContent();
                  double value = Double.parseDouble(score_s);
                  return value;

            } catch (IOException | SAXException | ParserConfigurationException | XPathExpressionException | DOMException | NumberFormatException e) {
                  return 0.0;
            }
      }

      private static int getMaxId() {
            Connection conn = null;
            Statement stmt = null;
            ResultSet result = null;
            int maxId = 0;
            try {
                  conn = ConexionMySQL.darConexion();
                  stmt = conn.createStatement();
                  //El ifnull es para que regrese cero si no hay máximo cuando la tabla está vacía.
                  result = stmt.executeQuery("SELECT IfNull(MAX(idComentario),0) AS max from comentario");
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

      // <editor-fold defaultstate="collapsed" desc="Verborrea: Getters y Setters.">

      
      public int getIdComentario() {
            return idComentario;
      }

      public void setIdComentario(int idComentario) {
            this.idComentario = idComentario;
      }




      public String getUsuario() {
            return usuario;
      }

      public void setUsuario(String usuario) {
            this.usuario = usuario;
      }

      public int getCurso() {
            return curso;
      }

      public void setCurso(int curso) {
            this.curso = curso;
      }

      public double getValNum() {
            return valNum;
      }

      public void setValNum(double valNum) {
            this.valNum = valNum;
      }

      public String getTexto() {
            return texto;
      }

      public void setTexto(String texto) {
            this.texto = texto;
      }

      public int getVotos() {
            return votos;
      }

      public void setVotos(int votos) {
            this.votos = votos;
      }

      @Override
      public int hashCode() {
            int hash = 5;
            hash = 29 * hash + this.idComentario;
            hash = 29 * hash + Objects.hashCode(this.usuario);
            hash = 29 * hash + this.curso;
            hash = 29 * hash + (int) (Double.doubleToLongBits(this.valNum) ^ (Double.doubleToLongBits(this.valNum) >>> 32));
            hash = 29 * hash + Objects.hashCode(this.texto);
            hash = 29 * hash + this.votos;
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
            final Comentario other = (Comentario) obj;
            if (this.idComentario != other.idComentario) {
                  return false;
            }

            if (!Objects.equals(this.usuario, other.usuario)) {
                  return false;
            }
            if (this.curso != other.curso) {
                  return false;
            }
            if (Double.doubleToLongBits(this.valNum) != Double.doubleToLongBits(other.valNum)) {
                  return false;
            }
            if (!Objects.equals(this.texto, other.texto)) {
                  return false;
            }
            if (this.votos != other.votos) {
                  return false;
            }
            return true;
      }

      @Override
      public String toString() {
            return "Comentario{" + "idComentario=" + idComentario + ", timestamp="  + ", usuario=" + usuario + ", curso=" + curso + ", valNum=" + valNum + ", texto=" + texto + ", votos=" + votos + '}';
      }
//</editor-fold>
}
