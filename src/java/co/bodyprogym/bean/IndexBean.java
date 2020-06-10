/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.bean;

import java.io.Serializable;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author anfel
 */
@ManagedBean
@RequestScoped
public class IndexBean implements Serializable{
    
    private Map sesion = (Map) FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    private static final Logger LOG = LogManager.getLogger(IndexBean.class);
   
   public String irCompras(){
      //System.out.println("Ingreso ..");
      sesion.put("Opcion", "Compras");
      LOG.info("Ingreso a la pagina de crear clase grupal ");
      return "Autenticacion?faces-redirect=true";
   }
   
   public String irClases(){
      //System.out.println("Ingreso ..");
      sesion.put("Opcion", "Clases");
      LOG.info("Ingreso a la pagina de crear clase grupal ");
      return "Autenticacion?faces-redirect=true";
   }
   
   public String irInformacion(){
      //System.out.println("Ingreso ..");
      sesion.put("Opcion", "Informacion");
      LOG.info("Ingreso a la pagina de crear clase grupal ");
      return "Autenticacion?faces-redirect=true";
   }
   
   
   public String irListaCG(){
       LOG.info("Ingreso a la pagina de la lista de clases grupales ");
       return "ListaClaseGrupal";
   }
   
   public String regresarPP(){
       return "index";
   }
   
   public void cerrarSesion(){
       FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
   }
   /*
   public String irListaE(){
       return "listaempresas";
   }
   
   public String irListas(){
       return "listas";
   }
   */
   
}
    

