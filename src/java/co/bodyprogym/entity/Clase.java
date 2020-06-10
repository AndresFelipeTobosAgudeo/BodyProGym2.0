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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "clase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clase.findAll", query = "SELECT c FROM Clase c")
    , @NamedQuery(name = "Clase.findByIdentificador", query = "SELECT c FROM Clase c WHERE c.identificador = :identificador")
    , @NamedQuery(name = "Clase.findByNombre", query = "SELECT c FROM Clase c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Clase.findByFecha", query = "SELECT c FROM Clase c WHERE c.fecha = :fecha")
    , @NamedQuery(name = "Clase.findByDescripcion", query = "SELECT c FROM Clase c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Clase.findByCapacidad", query = "SELECT c FROM Clase c WHERE c.capacidad = :capacidad")
    , @NamedQuery(name = "Clase.findByCosto", query = "SELECT c FROM Clase c WHERE c.costo = :costo")
    , @NamedQuery(name = "Clase.findByLugar", query = "SELECT c FROM Clase c WHERE c.lugar = :lugar")})
public class Clase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "identificador")
    private Integer identificador;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "capacidad")
    private Integer capacidad;
    @Column(name = "costo")
    private Integer costo;
    @Column(name = "lugar")
    private String lugar;
    @JoinTable(name = "deportista_clase", joinColumns = {
        @JoinColumn(name = "clase", referencedColumnName = "identificador")}, inverseJoinColumns = {
        @JoinColumn(name = "deportista", referencedColumnName = "cedula")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Deportista> deportistaCollection;
    @JoinColumn(name = "ins_cedula", referencedColumnName = "cedula")
    @ManyToOne(fetch = FetchType.EAGER)
    private Instructor insCedula;

    public Clase() {
    }

    public Clase(Integer identificador) {
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getCosto() {
        return costo;
    }

    public void setCosto(Integer costo) {
        this.costo = costo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    @XmlTransient
    public Collection<Deportista> getDeportistaCollection() {
        return deportistaCollection;
    }

    public void setDeportistaCollection(Collection<Deportista> deportistaCollection) {
        this.deportistaCollection = deportistaCollection;
    }

    public Instructor getInsCedula() {
        return insCedula;
    }

    public void setInsCedula(Instructor insCedula) {
        this.insCedula = insCedula;
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
        if (!(object instanceof Clase)) {
            return false;
        }
        Clase other = (Clase) object;
        if ((this.identificador == null && other.identificador != null) || (this.identificador != null && !this.identificador.equals(other.identificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.bodyprogym.entity.Clase[ identificador=" + identificador + " ]";
    }
    
}
