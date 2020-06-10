/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.bean;

import co.bodyprogym.dao.AsistenteJpaController;
import co.bodyprogym.dao.DeportistaJpaController;
import co.bodyprogym.dao.UsuarioJpaController;
import co.bodyprogym.entity.Asistente;
import co.bodyprogym.entity.Deportista;
import co.bodyprogym.entity.Usuario;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ManagedBean
@RequestScoped
public class AutenticacionBean implements Serializable {

    private String cedulamod;
    private Map session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    private static final Logger LOG = LogManager.getLogger(AutenticacionBean.class);

    public AutenticacionBean() {
    }

    public String getCedulamod() {

        return cedulamod;
    }

    public void setCedulamod(String cedulamod) {
        this.cedulamod = cedulamod;
    }

    public String autenticar() {
        try {
            UsuarioJpaController dao = new UsuarioJpaController();
            Usuario usu = dao.findUsuario(cedulamod);
            
            if (usu != null) {
                LOG.info("Se encuentra registrado, SE DA ACCESO");

                AsistenteJpaController opasis = new AsistenteJpaController();

                List<Asistente> asis = opasis.findAsistenteEntities();
                Asistente a = null;
                for (int i = 0; i < asis.size(); i++) {
                    if (cedulamod.equals(asis.get(i).getCedulamod())) {
                        a = asis.get(i);
                        i = asis.size();
                    }
                }

                DeportistaJpaController jpa = new DeportistaJpaController();

                List<Deportista> list = jpa.findDeportistaEntities();
                Deportista dep = null;
                for (int i = 0; i < list.size(); i++) {
                    if (cedulamod.equals(list.get(i).getUsuario().getCedulamod())) {
                        dep = list.get(i);
                        i = list.size();
                    }
                }

                if (a != null) {
                    if (session.get("Opcion").equals("Compras")) {
                        return "sinpermiso?faces-redirect=true";
                    } else if (session.get("Opcion").equals("Clases")) {
                        session.put("usuario", a);
                        return "RegistroClases?faces-redirect=true";
                    } else if (session.get("Opcion").equals("Informacion")) {
                        session.put("usuario", a);
                        return "Informacion?faces-redirect=true";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cuidado!", "Caso no existe"));
                    }
                }
                
                if (dep != null) {
                    if (session.get("Opcion").equals("Compras")) {
                        session.put("usuario", dep);
                        return "Compras?faces-redirect=true";
                    } else if (session.get("Opcion").equals("Clases")) {
                        session.put("usuario", dep);
                        return "ClaseDeportista?faces-redirect=true";
                    } else if (session.get("Opcion").equals("Informacion")) {
                        session.put("usuario", dep);
                        return "Informacion?faces-redirect=true";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cuidado!", "Caso no existe"));
                    }
                }
                
            } else {
                LOG.warn("No se encuentra registrado");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "No se encuentra registrado"));
                return "";
            }
        } catch (Exception e) {
            LOG.error("Error al autenticar " + e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error al autenticar"));
        }
        return null;

    }

}
