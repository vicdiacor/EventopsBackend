package com.eventops.model;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue
    private Long id;

	@ManyToOne
    @JoinColumn(name="evento_id")
    private Evento evento;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Client usuario;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((evento == null) ? 0 : evento.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Like other = (Like) obj;
        if (evento == null) {
            if (other.evento != null)
                return false;
        } else if (!evento.equals(other.evento))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (usuario == null) {
            if (other.usuario != null)
                return false;
        } else if (!usuario.equals(other.usuario))
            return false;
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Client getUsuario() {
        return usuario;
    }

    public void setUsuario(Client usuario) {
        this.usuario = usuario;
    }

    

}