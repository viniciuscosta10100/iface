package br.ufal.ic.iface.vo;

import br.ufal.ic.iface.interfaces.Serializable;

public class EstadoCivil implements Serializable {
	
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
