/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.foro;

import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author diego
 */
@ManagedBean (name = "comentario")
public class Comentario {
      List<String> comments;
      
      public Comentario() {
            comments = new LinkedList<String>();
            comments.add("Esto es un comentario");
            comments.add("Otro comentario mas largo");
            comments.add("Odio la vida y a todo lo demás. ");
      }

      public List<String> getComments() {
            return comments;
      }

      public void setComments(List<String> comments) {
            this.comments = comments;
      }
      
      
      
}
