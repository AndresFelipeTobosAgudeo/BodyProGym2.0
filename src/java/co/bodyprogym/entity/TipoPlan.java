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
@Table(name = "tipo_plan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPlan.findAll", query = "SELECT t FROM TipoPlan t")
    , @NamedQuery(name = "TipoPlan.findByIdentificador", query = "SELECT t FROM TipoPlan t WHERE t.identificador = :identificador")
    , @NamedQuery(name = "TipoPlan.findByNombre", query = "SELECT t FROM TipoPlan t WHERE t.nombre = :nombre")})
public class TipoPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "identificador")
    private Integer identificador;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "tipoPlan", fetch = FetchType.EAGER)
    private Collection<PlanDeportista> planDeportistaCollection;

    public TipoPlan() {
    }

    public TipoPlan(Integer identificador) {
        this.identificador = identificador;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<PlanDeportista> getPlanDeportistaCollection() {
        return planDeportistaCollection;
    }

    public void setPlanDeportistaCollection(Collection<PlanDeportista> planDeportistaCollection) {
        this.planDeportistaCollection = planDeportistaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identificador != null ? identificador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPlan)) {
            return false;
        }
        TipoPlan other = (TipoPlan) object;
        if ((this.identificador == null && other.identificador != null) || (this.identificador != null && !this.identificador.equals(other.identificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.bodyprogym.entity.TipoPlan[ identificador=" + identificador + " ]";
    }
    
}
