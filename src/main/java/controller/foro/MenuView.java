/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.foro;

import funciones.Pair;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean
@SessionScoped
public class MenuView {

      private MenuModel model;
      private List<Comentario> comentarios;
      private String materia;
      private String profesor;
      private String texto;
      private boolean asc;
      private boolean ordenarPorFecha;
      private PieChartModel pastel;

      //este atributo sí es privado
      private Curso curso;
      private ArrayList<Pair<Integer, Boolean>> comentariosVotados;

      @PostConstruct
      public void init() {
            //Valores iniciales
            profesor = "Elige un profesor";
            asc = false;
            ordenarPorFecha = true;
            comentariosVotados = new ArrayList<>();

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

                                    //quiero que cuando llegue a un profesor este tenga una acción
                                    //que al darle clic se active una función distinta para cada uno
                                    //Como java no tiene funciones de orden superior, y tampoco
                                    //me dejan pasarle objetos como argumentos tengo que pasar
                                    //cada uno de los campos del curso como string para reconstruirlo
                                    //en la función mostrarComentarios
                                    String idCurso = "" + c.getIdCurso();
                                    String idMateria = "" + c.getIdMateria();
                                    String idProfesor = "" + c.getIdProfesor();
                                    profesor.setParam("idCurso", idCurso);
                                    profesor.setParam("idMateria", idMateria);
                                    profesor.setParam("idProfesor", idProfesor);
                                    profesor.setParam("nombreProfesor", c.getProfesor());
                                    profesor.setParam("nombreMateria", c.getMateria());
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

                              //Lo mismo aqui le paso los argumentos y luego los recupero
                              String idCurso = "" + c.getIdCurso();
                              String idMateria = "" + c.getIdMateria();
                              String idProfesor = "" + c.getIdProfesor();
                              profesor.setParam("idCurso", idCurso);
                              profesor.setParam("idMateria", idMateria);
                              profesor.setParam("idProfesor", idProfesor);
                              profesor.setParam("nombreProfesor", c.getProfesor());
                              profesor.setParam("nombreMateria", c.getMateria());
                              profesor.setCommand("#{menuView.mostrarComentarios}");
                        }

                  }

            }

      }

      /**
       * No es un metodo en sí pero así es como vuelvo a cargar los comentarios.
       */
      private void refrescaLosComentarios() {
            this.comentarios = curso.obtenerComentarios(ordenarPorFecha, asc);

            //Esta era la línea que yo ya tenía, parece que el error era darle el id del form
            //en lugar de darle el de la tabla
            RequestContext.getCurrentInstance().update("formComentarios2");

            //Esto es lo que nos puso memo, parece que no funcionaba
            //FacesContext fc=FacesContext.getCurrentInstance();
            //fc.getPartialViewContext().getExecuteIds().add("formComentarios:tablaComentarios");
            //Esta línea dibuja todo otra vez pero es súper inutil, 
            //fc.getPartialViewContext().setRenderAll(true);
      }

      /**
       * Cuando le pican al nombre de un profesor se ejecuta esta función que hace que la lista de comentarios se actualicen a la del profesor y
       * materia en cuestión.
       *
       * @param ev
       */
      public void mostrarComentarios(ActionEvent ev) {
            //cast para obtener las propiedades 
            MenuActionEvent j = (MenuActionEvent) ev;
            //el menu item al que se dio clic
            DefaultMenuItem itm = (DefaultMenuItem) j.getMenuItem();

            //Ahora recupero todos los campos del curso
            int idCurso = Integer.parseInt(itm.getParams().get("idCurso").get(0));
            int idMateria = Integer.parseInt(itm.getParams().get("idMateria").get(0));
            int idProfesor = Integer.parseInt(itm.getParams().get("idProfesor").get(0));
            this.materia = itm.getParams().get("nombreMateria").get(0);
            this.profesor = itm.getParams().get("nombreProfesor").get(0);

            //Reconstruyo el curso y obtengo los comentarios
            this.curso = new Curso(idCurso, idMateria, idProfesor, materia, profesor);

            refrescaLosComentarios();
      }

      /**
       * Con esto publico comentarios. Recibo el nombre de usuario y mando llamar al método para publicar comentarios del curso que tengo guardado.
       *
       * @param usuario
       */
      public void publicarComentario(String usuario) {
            curso.publicarComentario(usuario, texto);
            texto = null;

            addMessage("¡Comentario publicado!");
            RequestContext.getCurrentInstance().reset("form_publicar");
            refrescaLosComentarios();
      }

      /**
       * Esto es para el boton de ordenar por fecha, recibe un booleano y cambia el orden de los comentarios
       *
       * @param si
       */
      public void ordenarPorFecha(boolean si) {
            ordenarPorFecha = si;
            refrescaLosComentarios();
      }

      public void ordenarAscendentemente(boolean si) {
            asc = si;
            refrescaLosComentarios();
      }

      /**
       * Con esto voto los comentarios, evito que voten mas de una vez por sesión
       *
       * @param com
       * @param positivo
       */
      public void votar(Comentario com, boolean positivo) {
            Integer idComentario = com.getIdComentario();
            Pair<Integer, Boolean> bueno = new Pair(idComentario, true);
            Pair<Integer, Boolean> malo = new Pair(idComentario, false);
            Pair<Integer, Boolean> voto = new Pair(idComentario, positivo);

            //Si ya habia votado positivamente entonces este voto cuenta por dos negativos
            if (comentariosVotados.contains(bueno)) {
                  if (!positivo) {
                        com.votar(false);
                        com.votar(false);
                        comentariosVotados.remove(bueno);
                        comentariosVotados.add(voto);
                  }
            } else if (comentariosVotados.contains(malo)) {
                  if (positivo) {
                        com.votar(true);
                        com.votar(true);
                        comentariosVotados.remove(malo);
                        comentariosVotados.add(voto);
                  }
            } else {
                  com.votar(positivo);
                  comentariosVotados.add(voto);
            }
            refrescaLosComentarios();

      }

      public void addMessage(String mensaje) {
            FacesMessage message = new FacesMessage(mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
      }

      /**
       * Esta función abre la ventana con el pastel sentimental
       *
       * @param e
       */
      public void pastelSentimental(ActionEvent e) {
            pastel = new PieChartModel();

            int[] rs = curso.obtenerProfesor().sumarizado(); //(sic)
            pastel.set("Terrible", rs[0]);
            pastel.set("Malo", rs[1]);
            pastel.set("Normal", rs[2]);
            pastel.set("Bueno", rs[3]);
            pastel.set("Grandiso", rs[4]);

            pastel.setTitle("Análisis sentimental para " + this.profesor);
            pastel.setLegendPosition("e");
            pastel.setShowDataLabels(true);
            pastel.setDiameter(250);

            //Ya que tengo configurado el pastel lo muestro en un dialogo
            Map<String, Object> options = new HashMap<String, Object>();
            options.put("modal", true);
            options.put("draggable", false);
            options.put("resizable", false);
            options.put("contentHeight", 480);

            RequestContext.getCurrentInstance().openDialog("pastel", options, null);

            profesor += "*";
      }

      // <editor-fold defaultstate="collapsed" desc="Verborrea: Getters y Setters.">
      public PieChartModel getPastel() {
            return pastel;
      }

      public void setPastel(PieChartModel pastel) {
            this.pastel = pastel;
      }

      public boolean isAsc() {
            return asc;
      }

      public void setAsc(boolean asc) {
            this.asc = asc;
      }

      public boolean isOrdenarPorFecha() {
            return ordenarPorFecha;
      }

      public void setOrdenarPorFecha(boolean ordenarPorFecha) {
            this.ordenarPorFecha = ordenarPorFecha;
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

      public MenuModel getModel() {
            return model;
      }

      public void setModel(MenuModel model) {
            this.model = model;
      }

      public String getTexto() {
            return texto;
      }

      public void setTexto(String texto) {
            this.texto = texto;
      }

      public List<Comentario> getComentarios() {
            return comentarios;
      }

      public void setComentarios(List<Comentario> comentarios) {
            this.comentarios = comentarios;
      }
//</editor-fold>
}
