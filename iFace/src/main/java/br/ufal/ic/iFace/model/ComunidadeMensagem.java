package br.ufal.ic.iFace.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.ufal.ic.iFace.interfaces.Serializable;

@Entity
@Table(name = "mensagem_comunidade")
public class ComunidadeMensagem implements Serializable{

	private static final long serialVersionUID = 207404855772673425L;

	@Id
	@GeneratedValue
	private int id;
	
	private String texto;
	
	@ManyToOne
	@JoinColumn(name = "comunidade_id")
	private Comunidade comunidade;
	
	@ManyToOne
	@JoinColumn(name = "remetente_id")
	private Usuario remetente;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Comunidade getComunidade() {
		return comunidade;
	}
	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}
	public Usuario getRemetente() {
		return remetente;
	}
	public void setRemetente(Usuario remetente) {
		this.remetente = remetente;
	}
	
	@Override
	public String toString(){
		String s = remetente.getNome()+": "+texto;
		return s;
	}
}
