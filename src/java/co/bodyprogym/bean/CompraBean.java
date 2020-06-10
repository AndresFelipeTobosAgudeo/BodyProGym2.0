/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.bean;

import co.bodyprogym.dao.ClaseJpaController;
import co.bodyprogym.entity.Asistente;
import co.bodyprogym.entity.Clase;
import co.bodyprogym.entity.Deportista;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author usuario
 */
@ManagedBean
@RequestScoped
public class CompraBean implements Serializable {
    
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(CompraBean.class);
    private Deportista dp;
    private String ml;
    private Map session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

    public CompraBean() {
        oper=new ClaseJpaController();
    }
    
    
    private List<Clase> lisclase ;
    private ClaseJpaController oper; 
    
    @PostConstruct
    public void init(){
        lisclase = cargarCla();
    }

    public void verificar() {
        if (session.equals("")) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("sinpermiso.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(CompraBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            dp = (Deportista) session.get("usuario");
            ml=dp.getNombre();
        }

//        ml=asis.getCedulamod()+asis.getCedula()+asis.getNombreCompleto();
    }
    
    
    public void registro(Clase dato){
        List<Deportista> listDepo = (List<Deportista>) dato.getDeportistaCollection();
        listDepo.add((Deportista) session.get("usuario"));
        Collection<Deportista> col =listDepo;

        try {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Se inscribio a la clase y se registro su compra"));
            dato.setDeportistaCollection(col);
            oper.edit(dato);
            LOG.info("Se inscribio a la clase y se registro su compra"+dato);
        } catch (Exception ex) {
            Logger.getLogger(CompraBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private List<Clase> cargarCla(){
        return oper.findClaseEntities();
    }
    

    public String getMl() {
        return ml;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public List<Clase> getLisclase() {
        return lisclase;
    }

    public void setLisclase(List<Clase> lisclase) {
        this.lisclase = lisclase;
    }

    
}
