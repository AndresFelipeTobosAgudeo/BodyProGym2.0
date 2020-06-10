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
import javax.persistence.ManyToMany;
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
@Table(name = "deportista")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Deportista.findAll", query = "SELECT d FROM Deportista d")
    , @NamedQuery(name = "Deportista.findByCedula", query = "SELECT d FROM Deportista d WHERE d.cedula = :cedula")
    , @NamedQuery(name = "Deportista.findByNombre", query = "SELECT d FROM Deportista d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "Deportista.findByCiudad", query = "SELECT d FROM Deportista d WHERE d.ciudad = :ciudad")
    , @NamedQuery(name = "Deportista.findByFechaNacimiento", query = "SELECT d FROM Deportista d WHERE d.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Deportista.findByTelefono", query = "SELECT d FROM Deportista d WHERE d.telefono = :telefono")})
public class Deportista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cedula")
    private Integer cedula;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "ciudad")
    private String ciudad;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "telefono")
    private Long telefono;
    @ManyToMany(mappedBy = "deportistaCollection", fetch = FetchType.EAGER)
    private Collection<Clase> claseCollection;
    @OneToMany(mappedBy = "cedulaDeportista", fetch = FetchType.EAGER)
    private Collection<RegistroCompra> registroCompraCollection;
    @JoinColumn(name = "plan", referencedColumnName = "identificador")
    @ManyToOne(fetch = FetchType.EAGER)
    private PlanDeportista plan;
    @JoinColumn(name = "usuario", referencedColumnName = "cedulamod")
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    public Deportista() {
    }

    public Deportista(Integer cedula) {
        this.cedula = cedula;
    }

    public Integer getCedula() {
        return cedula;
    }

    public void setCedula(Integer cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    @XmlTransient
    public Collection<Clase> getClaseCollection() {
        return claseCollection;
    }

    public void setClaseCollection(Collection<Clase> claseCollection) {
        this.claseCollection = claseCollection;
    }

    @XmlTransient
    public Collection<RegistroCompra> getRegistroCompraCollection() {
        return registroCompraCollection;
    }

    public void setRegistroCompraCollection(Collection<RegistroCompra> registroCompraCollection) {
        this.registroCompraCollection = registroCompraCollection;
    }

    public PlanDeportista getPlan() {
        return plan;
    }

    public void setPlan(PlanDeportista plan) {
        this.plan = plan;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedula != null ? cedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Deportista)) {
            return false;
        }
        Deportista other = (Deportista) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.bodyprogym.entity.Deportista[ cedula=" + cedula + " ]";
    }
    
}
