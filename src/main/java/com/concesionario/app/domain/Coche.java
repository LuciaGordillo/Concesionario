package com.concesionario.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Coche.
 */
@Entity
@Table(name = "coche")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Coche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "color", nullable = false)
    private String color;

    @NotNull
    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @Column(name = "potencia")
    private Integer potencia;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "puertas")
    private String puertas;

    @Column(name = "combustible")
    private String combustible;

    @NotNull
    @Column(name = "vendido", nullable = false)
    private Boolean vendido;

    @ManyToOne(optional = false)
    @NotNull
    private Marca marca;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "marca" }, allowSetters = true)
    private Modelo modelo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Coche id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return this.color;
    }

    public Coche color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Coche matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getPotencia() {
        return this.potencia;
    }

    public Coche potencia(Integer potencia) {
        this.setPotencia(potencia);
        return this;
    }

    public void setPotencia(Integer potencia) {
        this.potencia = potencia;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public Coche precio(Double precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getPuertas() {
        return this.puertas;
    }

    public Coche puertas(String puertas) {
        this.setPuertas(puertas);
        return this;
    }

    public void setPuertas(String puertas) {
        this.puertas = puertas;
    }

    public String getCombustible() {
        return this.combustible;
    }

    public Coche combustible(String combustible) {
        this.setCombustible(combustible);
        return this;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public Boolean getVendido() {
        return this.vendido;
    }

    public Coche vendido(Boolean vendido) {
        this.setVendido(vendido);
        return this;
    }

    public void setVendido(Boolean vendido) {
        this.vendido = vendido;
    }

    public Marca getMarca() {
        return this.marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Coche marca(Marca marca) {
        this.setMarca(marca);
        return this;
    }

    public Modelo getModelo() {
        return this.modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Coche modelo(Modelo modelo) {
        this.setModelo(modelo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coche)) {
            return false;
        }
        return id != null && id.equals(((Coche) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coche{" +
            "id=" + getId() +
            ", color='" + getColor() + "'" +
            ", matricula='" + getMatricula() + "'" +
            ", potencia=" + getPotencia() +
            ", precio=" + getPrecio() +
            ", puertas='" + getPuertas() + "'" +
            ", combustible='" + getCombustible() + "'" +
            ", vendido='" + getVendido() + "'" +
            "}";
    }
}
