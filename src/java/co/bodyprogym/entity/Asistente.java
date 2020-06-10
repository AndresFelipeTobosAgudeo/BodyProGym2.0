/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "asistente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asistente.findAll", query = "SELECT a FROM Asistente a")
    , @NamedQuery(name = "Asistente.findByNombre", query = "SELECT a FROM Asistente a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "Asistente.findByCedula", query = "SELECT a FROM Asistente a WHERE a.cedula = :cedula")
    , @NamedQuery(name = "Asistente.findByCedulamod", query = "SELECT a FROM Asistente a WHERE a.cedulamod = :cedulamod")})
public class Asistente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "cedula")
    private String cedula;
    @Id
    @Basic(optional = false)
    @Column(name = "cedulamod")
    private String cedulamod;

    public Asistente() {
    }

    public Asistente(String cedulamod) {
        this.cedulamod = cedulamod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCedulamod() {
        return cedulamod;
    }

    public void setCedulamod(String cedulamod) {
        this.cedulamod = cedulamod;
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
        if (!(object instanceof Asistente)) {
            return false;
        }
        Asistente other = (Asistente) object;
        if ((this.cedulamod == null && other.cedulamod != null) || (this.cedulamod != null && !this.cedulamod.equals(other.cedulamod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.bodyprogym.entity.Asistente[ cedulamod=" + cedulamod + " ]";
    }
    
}
