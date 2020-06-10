/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.bean;

import co.bodyprogym.dao.ClaseJpaController;
import co.bodyprogym.entity.Clase;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author anfel
 */
@ManagedBean
@ViewScoped
public class ListaClaseGrupalBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger(ListaClaseGrupalBean.class);

    private List<Clase> clasegrupal;
    //@Inject
    private ClaseJpaController oper;
    
    public ListaClaseGrupalBean() {
        oper = new ClaseJpaController();
    }

    @PostConstruct
    public void init() {
        clasegrupal = cargarCla();
    }

    public String regresar() {
        LOG.info("Regreso a la pagina Clase Grupal");
        return "ClaseGrupal";
    }

    public List<Clase> getClasegrupal() {
        return clasegrupal;
    }

    public void setClasegrupal(List<Clase> clasegrupal) {
        this.clasegrupal = clasegrupal;
    }

    private List<Clase> cargarCla() {

        return oper.findClaseEntities();
    }

}
