package br.ufal.ic.iFace.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.ufal.ic.iFace.interfaces.Serializable;

@Entity
@Table(name = "amizade")
public class Amizade implements Serializable {
	
	private static final long serialVersionUID = 5940370923612548233L;

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name="usuario1_id")
	private Usuario usuario1;
	
	@ManyToOne
	@JoinColumn(name="usuario2_id")
	private Usuario usuario2;
	
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuario1() {
		return usuario1;
	}

	public void setUsuario1(Usuario usuario1) {
		this.usuario1 = usuario1;
	}

	public Usuario getUsuario2() {
		return usuario2;
	}

	public void setUsuario2(Usuario usuario2) {
		this.usuario2 = usuario2;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
