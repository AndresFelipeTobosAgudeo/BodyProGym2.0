/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.entity;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "registro_compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistroCompra.findAll", query = "SELECT r FROM RegistroCompra r")
    , @NamedQuery(name = "RegistroCompra.findByIdentificador", query = "SELECT r FROM RegistroCompra r WHERE r.identificador = :identificador")
    , @NamedQuery(name = "RegistroCompra.findByFecha", query = "SELECT r FROM RegistroCompra r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "RegistroCompra.findByProducto", query = "SELECT r FROM RegistroCompra r WHERE r.producto = :producto")
    , @NamedQuery(name = "RegistroCompra.findByDescripcion", query = "SELECT r FROM RegistroCompra r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "RegistroCompra.findByValor", query = "SELECT r FROM RegistroCompra r WHERE r.valor = :valor")})
public class RegistroCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "identificador")
    private Integer identificador;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "producto")
    private String producto;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "valor")
    private Integer valor;
    @JoinColumn(name = "cedula_deportista", referencedColumnName = "cedula")
    @ManyToOne(fetch = FetchType.EAGER)
    private Deportista cedulaDeportista;

    public RegistroCompra() {
    }

    public RegistroCompra(Integer identificador) {
        this.identificador = identificador;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Deportista getCedulaDeportista() {
        return cedulaDeportista;
    }

    public void setCedulaDeportista(Deportista cedulaDeportista) {
        this.cedulaDeportista = cedulaDeportista;
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
        if (!(object instanceof RegistroCompra)) {
            return false;
        }
        RegistroCompra other = (RegistroCompra) object;
        if ((this.identificador == null && other.identificador != null) || (this.identificador != null && !this.identificador.equals(other.identificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.bodyprogym.entity.RegistroCompra[ identificador=" + identificador + " ]";
    }
    
}
