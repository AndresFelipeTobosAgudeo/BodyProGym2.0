/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "plan_deportista")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanDeportista.findAll", query = "SELECT p FROM PlanDeportista p")
    , @NamedQuery(name = "PlanDeportista.findByIdentificador", query = "SELECT p FROM PlanDeportista p WHERE p.identificador = :identificador")
    , @NamedQuery(name = "PlanDeportista.findByNombre", query = "SELECT p FROM PlanDeportista p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "PlanDeportista.findByFechaInicial", query = "SELECT p FROM PlanDeportista p WHERE p.fechaInicial = :fechaInicial")
    , @NamedQuery(name = "PlanDeportista.findByValor", query = "SELECT p FROM PlanDeportista p WHERE p.valor = :valor")})
public class PlanDeportista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "identificador")
    private Integer identificador;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    @Column(name = "valor")
    private Integer valor;
    @JoinColumn(name = "tipo_plan", referencedColumnName = "identificador")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoPlan tipoPlan;
    @OneToMany(mappedBy = "plan", fetch = FetchType.EAGER)
    private Collection<Deportista> deportistaCollection;

    public PlanDeportista() {
    }

    public PlanDeportista(Integer identificador) {
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

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public TipoPlan getTipoPlan() {
        return tipoPlan;
    }

    public void setTipoPlan(TipoPlan tipoPlan) {
        this.tipoPlan = tipoPlan;
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
        hash += (identificador != null ? identificador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanDeportista)) {
            return false;
        }
        PlanDeportista other = (PlanDeportista) object;
        if ((this.identificador == null && other.identificador != null) || (this.identificador != null && !this.identificador.equals(other.identificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.bodyprogym.entity.PlanDeportista[ identificador=" + identificador + " ]";
    }
    
}
