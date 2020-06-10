/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.bean;

import co.bodyprogym.dao.ClaseJpaController;
import co.bodyprogym.dao.InstructorJpaController;
import co.bodyprogym.dao.exceptions.NonexistentEntityException;
import co.bodyprogym.entity.Asistente;
import co.bodyprogym.entity.Clase;
import co.bodyprogym.entity.Instructor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author anfel
 */
@ManagedBean
@ViewScoped
public class CrearClaseGrupalBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger(CrearClaseGrupalBean.class);
    private Map session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

    private String nm;
    private String nombre;
    private Date fecha;
    private String[] instructor;
    private int capacidad;
    private String objetivo;
    private int costo;
    private String lugar;

    private List<String> list;
    private InstructorJpaController oper;

    private List<Clase> list1;
    private Clase clase1;
    private ClaseJpaController oper1;

    public CrearClaseGrupalBean() {
        oper = new InstructorJpaController();
        oper1 = new ClaseJpaController();
        Asistente dep = (Asistente) session.get("usuario");
        nm = dep.getNombre();
    }

    @PostConstruct
    public void init() {
        list = new ArrayList<>();
        List<Instructor> listi = oper.findInstructorEntities();
        for (int i = 0; i < listi.size(); i++) {
            list.add(listi.get(i).getNombre());
        }
        list1 = cargarCla();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String[] getInstructor() {
        return instructor;
    }

    public void setInstructor(String[] instructor) {
        this.instructor = instructor;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<Clase> getList1() {
        return list1;
    }

    public void setList1(List<Clase> list1) {
        this.list1 = list1;
    }

    public void insertar() {

        if (nombre == null || nombre.isEmpty() || fecha == null || capacidad == 0 || objetivo == null || objetivo.isEmpty() || lugar == null || lugar.isEmpty() || instructor.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Datos nulos revise"));
            LOG.warn("Atención, Datos nulos revise");
            return;
        }

        InstructorJpaController jpa = new InstructorJpaController();
        List<Instructor> list1 = jpa.findInstructorEntities();
        Instructor ins = new Instructor();
        for (int i = 0; i < list1.size(); i++) {
            if (instructor[0].equals(list1.get(i).getNombre())) {
                ins = list1.get(i);
                i = list1.size();
            }
        }

        ClaseJpaController oper = new ClaseJpaController();
        Clase cg = new Clase();
        cg.setIdentificador(oper.findClaseEntities().size() + 1);
        cg.setNombre(nombre);
        cg.setFecha(fecha);
        cg.setInsCedula(ins);
        cg.setCapacidad(capacidad);
        cg.setDescripcion(objetivo);
        cg.setCosto(costo);
        cg.setLugar(lugar);

        try {
            oper.create(cg);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Se registro la clase grupal"));
            LOG.info("Atención, Se registro la clase grupal");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No se registro la clase grupal" + ex));
            LOG.warn("Atención, No se registro la clase grupal" + ex);
        }
    }

    private List<Clase> cargarCla() {
        return oper1.findClaseEntities();
    }

    public void eliminar(Clase e) {
        try {
            if (e.getDeportistaCollection().isEmpty()) {
                oper1.destroy(e.getIdentificador());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", "La clases ha sido, eliminada."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "La clases ya tiene intregrantes, no se peude eliminar."));
            }
        } catch (NonexistentEntityException ex) {
            LOG.error("Error al eliminar Clase " + ex);
        }
    }

    public void editar(Clase e) {
        nombre = e.getNombre();
        fecha = e.getFecha();
        capacidad = e.getCapacidad();
        objetivo = e.getDescripcion();
        costo = e.getCapacidad();
        lugar = e.getLugar();
        clase1 = e;
    }

    public void completarEditar() {
        if (clase1.getDeportistaCollection().isEmpty()) {
            if (nombre == null || nombre.isEmpty() || fecha == null || capacidad == 0 || objetivo == null || objetivo.isEmpty() || lugar == null || lugar.isEmpty() || instructor.length == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Datos nulos revise"));
                LOG.warn("Atención, Datos nulos revise");
                return;
            }

            InstructorJpaController jpa = new InstructorJpaController();
            List<Instructor> list1 = jpa.findInstructorEntities();
            Instructor ins = new Instructor();
            for (int i = 0; i < list1.size(); i++) {
                if (instructor[0].equals(list1.get(i).getNombre())) {
                    ins = list1.get(i);
                    i = list1.size();
                }
            }

            clase1.setNombre(nombre);
            clase1.setFecha(fecha);
            clase1.setInsCedula(ins);
            clase1.setCapacidad(capacidad);
            clase1.setDescripcion(objetivo);
            clase1.setCosto(costo);
            clase1.setLugar(lugar);

            try {
                oper1.edit(clase1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Se actualizo la clase grupal"));
                LOG.info("Atención, Se actualizo la clase grupal");
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No se actualizo la clase grupal" + ex));
                LOG.warn("Atención, No se actualizo la clase grupal" + ex);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Hay deportistas inscritos, solo se actualizo el instructor"));
            InstructorJpaController jpa = new InstructorJpaController();
            List<Instructor> list1 = jpa.findInstructorEntities();
            Instructor ins = new Instructor();
            for (int i = 0; i < list1.size(); i++) {
                if (instructor[0].equals(list1.get(i).getNombre())) {
                    ins = list1.get(i);
                    i = list1.size();
                }
            }
            clase1.setInsCedula(ins);
            try {
                oper1.edit(clase1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Se actualizo la clase grupal"));
                LOG.info("Atención, Se actualizo la clase grupal");
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No se actualizo la clase grupal" + ex));
                LOG.warn("Atención, No se actualizo la clase grupal" + ex);
            }
        }
    }

}
