package br.ufal.ic.iFace.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.ufal.ic.iFace.interfaces.Serializable;

@Entity
@Table(name = "estado_civil")
public class EstadoCivil implements Serializable {
	
	private static final long serialVersionUID = -4305757809644997495L;

	@Id
	@GeneratedValue
	private int id;
	
	private String nome;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

}
