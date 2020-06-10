/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByCedulamod", query = "SELECT u FROM Usuario u WHERE u.cedulamod = :cedulamod")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cedulamod")
    private String cedulamod;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private Collection<Deportista> deportistaCollection;

    public Usuario() {
    }

    public Usuario(String cedulamod) {
        this.cedulamod = cedulamod;
    }

    public String getCedulamod() {
        return cedulamod;
    }

    public void setCedulamod(String cedulamod) {
        this.cedulamod = cedulamod;
    }

    @XmlTransient
    public Collection<Deportista> getDeportistaCollection() {
        return deportistaCollection;
    }

    public void setDeportistaCollection(Collection<Deportista> deportistaCollection) {
        this.deportistaCollection = deportistaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedulamod != null ? cedulamod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.cedulamod == null && other.cedulamod != null) || (this.cedulamod != null && !this.cedulamod.equals(other.cedulamod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.bodyprogym.entity.Usuario[ cedulamod=" + cedulamod + " ]";
    }
    
}
