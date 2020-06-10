/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.bean;

import co.bodyprogym.dao.ClaseJpaController;
import co.bodyprogym.entity.Clase;
import co.bodyprogym.entity.Deportista;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author usuario
 */
@ManagedBean
@RequestScoped
public class ClaseDeportistaBean {

    private Map session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    private static final Logger LOG = LogManager.getLogger(AutenticacionBean.class);

    private List<Clase> list;
    private ClaseJpaController oper;
    
    private String Nombre;

    public ClaseDeportistaBean() {
        oper = new ClaseJpaController();
        Deportista dep = (Deportista) session.get("usuario");
        Nombre=dep.getNombre();
    }

    @PostConstruct
    public void init() {
        list = cargarCla();
    }

    public List<Clase> getList() {
        return list;
    }

    public void setList(List<Clase> list) {
        this.list = list;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    private List<Clase> cargarCla() {
        Deportista dep = (Deportista) session.get("usuario");
        List<Clase> resp = new ArrayList<>();
        List<Clase> list1 = oper.findClaseEntities();
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).getDeportistaCollection().contains(dep)) {
                resp.add(list1.get(i));
            }
        }
        return resp;
    }

}
