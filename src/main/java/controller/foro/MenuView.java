/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.foro;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import model.Comentario;
import model.Curso;
import model.Materia;
import model.Plan;
import model.Profesor;
import org.primefaces.context.RequestContext;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;

@ManagedBean
@SessionScoped
public class MenuView {

      private MenuModel model;
      private List<Comentario> comentarios;

      @PostConstruct
      public void init() {

            //Primero hago el menu principal
            model = new DefaultMenuModel();

            //Saco de la base de datos todos los planes de estudio
            List<Plan> planes = Plan.selectAll();

            for (Plan p : planes) {
                  //para cada plan de estudio hago un submenu nuevo y lo agrego al principal
                  String label = p.getCarrera() + " - " + p.getAno();
                  DefaultSubMenu sub = new DefaultSubMenu(label);
                  model.addElement(sub);

                  //Veo cuantos semestres hay con materias obligatorias
                  int semestres = p.maxSemestre();

                  for (int i = 1; i <= semestres; i++) {
                        //Para cada semestre hago un subsubmenu
                        DefaultSubMenu sem = new DefaultSubMenu("Semestre " + i);
                        sub.addElement(sem);

                        //Ahora veo cuales son las materias que dan en este semestre
                        List<Materia> materias = p.materiasPorSemestre(i);
                        for (Materia m : materias) {
                              //Ahora para cada materia hago otro submenu

                              DefaultSubMenu item_materia = new DefaultSubMenu(m.getNombre());
                              sem.addElement(item_materia);

                              //Ahora veo que profesores están dando la materia
                              List<Curso> cursos = m.obtenerCursos();
                              for (Curso c : cursos) {
                                    Profesor prof = c.obtenerProfesor();
                                    DefaultMenuItem profesor = new DefaultMenuItem(prof.getNombre());
                                    item_materia.addElement(profesor);
//                                  
                                    String idMateria = "" + c.getIdMateria();
                                    String idProfesor = "" + c.getIdProfesor();
                                    profesor.setParam("idMateria", idMateria);
                                    profesor.setParam("idProfesor", idProfesor);
                                    profesor.setCommand("#{menuView.mostrarComentarios}");
                              }

                        }

                  }

                  //Ahora meto las materias optativas.
                  DefaultSubMenu opt = new DefaultSubMenu("Optativas");
                  sub.addElement(opt);
                  List<Materia> optativas = p.materiasPorSemestre(0);
                  for (Materia m : optativas) {
                        //Ahora para cada materia hago un item.

                        DefaultSubMenu item_materia = new DefaultSubMenu(m.getNombre());
                        opt.addElement(item_materia);

                        //Ahora los profesores
                        List<Curso> cursos = m.obtenerCursos();
                        for (Curso c : cursos) {
                              Profesor prof = c.obtenerProfesor();
                              DefaultMenuItem profesor = new DefaultMenuItem(prof.getNombre());
                              profesor.setCommand("#{menuView.delete}");
                              item_materia.addElement(profesor);

                              String idMateria = "" + c.getIdMateria();
                              String idProfesor = "" + c.getIdProfesor();
                              profesor.setParam("idMateria", idMateria);
                              profesor.setParam("idProfesor", idProfesor);
                              profesor.setCommand("#{menuView.mostrarComentarios}");
                        }

                  }

            }

      }

      public void mostrarComentarios(ActionEvent ev) {
            //cast para obtener las propiedades 
            MenuActionEvent j = (MenuActionEvent) ev;
            //el menu item al que se dio clic
            DefaultMenuItem itm = (DefaultMenuItem) j.getMenuItem();
            int idMateria = Integer.parseInt(itm.getParams().get("idMateria").get(0));
            int idProfesor = Integer.parseInt(itm.getParams().get("idProfesor").get(0));

            List<Comentario> lista = Comentario.encuentraComentarios(idProfesor, idMateria, true, true);
            this.comentarios = lista;
            FacesContext fc=FacesContext.getCurrentInstance();
            fc.getPartialViewContext().getExecuteIds().add("formComentarios:tablaComentarios");
           // RequestContext.getCurrentInstance().update(":formComentarios");

      }

      public void init2() {
            model = new DefaultMenuModel();

            //First submenu
            DefaultSubMenu firstSubmenu = new DefaultSubMenu("Dynamic Submenu");

            DefaultMenuItem item = new DefaultMenuItem("External");
            item.setUrl("http://www.primefaces.org");
            item.setIcon("ui-icon-home");
            firstSubmenu.addElement(item);

            model.addElement(firstSubmenu);

            //Second submenu
            DefaultSubMenu secondSubmenu = new DefaultSubMenu("Dynamic Actions");

            item = new DefaultMenuItem("Save");
            item.setIcon("ui-icon-disk");
            item.setCommand("#{menuView.save}");
            item.setUpdate("messages");
            secondSubmenu.addElement(item);

            item = new DefaultMenuItem("Delete");
            item.setIcon("ui-icon-close");
            item.setCommand("#{menuView.delete}");
            item.setAjax(false);
            secondSubmenu.addElement(item);

            item = new DefaultMenuItem("Redirect");
            item.setIcon("ui-icon-search");
            item.setCommand("#{menuView.redirect}");
            secondSubmenu.addElement(item);

            model.addElement(secondSubmenu);
      }

      public MenuModel getModel() {
            return model;
      }

      public void setModel(MenuModel model) {
            this.model = model;
      }

      public List<Comentario> getComentarios() {
            return comentarios;
      }

      public void setComentarios(List<Comentario> comentarios) {
            this.comentarios = comentarios;
      }

      public void addMessage(String summary, String detail) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
            FacesContext.getCurrentInstance().addMessage(null, message);
      }
}
