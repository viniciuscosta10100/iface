package br.ufal.ic.iFace.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.ufal.ic.iFace.interfaces.Serializable;

@Entity
@Table(name = "usuario_comunidade")
public class ComunidadeParticipantes implements Serializable{
	
	private static final long serialVersionUID = -2744305942288583873L;

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name="comunidade_id")
	private Comunidade comunidade;
	
	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
