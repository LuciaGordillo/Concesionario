package com.concesionario.app.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "unidades", nullable = false)
    private Integer unidades;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "descuento")
    private Double descuento;

    @NotNull
    @Column(name = "precio_total", nullable = false)
    private Double precio_total;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUnidades() {
        return this.unidades;
    }

    public Venta unidades(Integer unidades) {
        this.setUnidades(unidades);
        return this;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Venta fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getDescuento() {
        return this.descuento;
    }

    public Venta descuento(Double descuento) {
        this.setDescuento(descuento);
        return this;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getPrecio_total() {
        return this.precio_total;
    }

    public Venta precio_total(Double precio_total) {
        this.setPrecio_total(precio_total);
        return this;
    }

    public void setPrecio_total(Double precio_total) {
        this.precio_total = precio_total;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", unidades=" + getUnidades() +
            ", fecha='" + getFecha() + "'" +
            ", descuento=" + getDescuento() +
            ", precio_total=" + getPrecio_total() +
            "}";
    }
}
