package com.eventops.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;


import org.springframework.lang.NonNull;

@Entity
@Table(name = "eventos")
public class Evento { 

    @Id
    @GeneratedValue
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    private String nombre;

    
    
    @Size(max = 600)
    private String descripcion;

    @NonNull
    @Min(0)
    @Max(1500)
    @Digits(fraction=2,integer=4)
    private Double precio;

    @NotNull
    private LocalDateTime fechaInicio;

    @NotNull
    private LocalDateTime fechaFin;

    @NonNull
    private Boolean mostrar;

   
    @Min(1)
    private Integer aforoMaximo;
    
    @NotBlank
    @Size(max = 80)
    private String ciudad;



    @ManyToOne
    @JoinColumn(name="user_id")
    private Client usuario;



    public Evento(){
    }

    public Evento(long id,Client usuario){
        this.id = id;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

  

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getMostrar() {
        return mostrar;
    }

    public void setMostrar(Boolean mostrar) {
        this.mostrar = mostrar;
    }

    public Integer getAforoMaximo() {
        return aforoMaximo;
    }

    public void setAforoMaximo(Integer aforoMaximo) {
        this.aforoMaximo = aforoMaximo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Client getUsuario() {
        return usuario;
    }

    public void setUsuario(Client usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aforoMaximo == null) ? 0 : aforoMaximo.hashCode());
        result = prime * result + ((ciudad == null) ? 0 : ciudad.hashCode());
        result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + ((fechaFin == null) ? 0 : fechaFin.hashCode());
        result = prime * result + ((fechaInicio == null) ? 0 : fechaInicio.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((mostrar == null) ? 0 : mostrar.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((precio == null) ? 0 : precio.hashCode());
        result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Evento other = (Evento) obj;
        if (aforoMaximo == null) {
            if (other.aforoMaximo != null)
                return false;
        } else if (!aforoMaximo.equals(other.aforoMaximo))
            return false;
        if (ciudad == null) {
            if (other.ciudad != null)
                return false;
        } else if (!ciudad.equals(other.ciudad))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (fechaFin == null) {
            if (other.fechaFin != null)
                return false;
        } else if (!fechaFin.equals(other.fechaFin))
            return false;
        if (fechaInicio == null) {
            if (other.fechaInicio != null)
                return false;
        } else if (!fechaInicio.equals(other.fechaInicio))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (mostrar == null) {
            if (other.mostrar != null)
                return false;
        } else if (!mostrar.equals(other.mostrar))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (precio == null) {
            if (other.precio != null)
                return false;
        } else if (!precio.equals(other.precio))
            return false;
        if (usuario == null) {
            if (other.usuario != null)
                return false;
        } else if (!usuario.equals(other.usuario))
            return false;
        return true;
    }

    
  
}