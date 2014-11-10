/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.foro;

import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import model.Comentario;

/**
 *
 * @author diego
 */

@ManagedBean (name="comentarios")
public class Comentarios {
      
      @ManagedProperty (value = "#{comentariosLista}")
      private List<Comentario> lista;
      
      
      
      
}
