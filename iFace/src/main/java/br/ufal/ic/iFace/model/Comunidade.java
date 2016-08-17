package br.ufal.ic.iFace.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.interfaces.Serializable;

@Entity
@Table(name="comunidade")
public class Comunidade implements Serializable {

	private static final long serialVersionUID = 2802337039513422058L;

	public Comunidade(){
		init();
	}
	
	public Comunidade(String nome, String descricao){
		this.nome = nome;
		this.descricao = descricao;
		this.responsavel = Main.usuarioRepository.findById(Main.sessao.getUsuario().getId());
		init();
	}
	
	@Id
	@GeneratedValue
	private int id;
	
	private String nome;
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name = "responsavel_id")
	private Usuario responsavel;
	
	@Transient
	private List<Usuario> participantes;
	
	private void init(){
		this.participantes = new ArrayList<Usuario>();
	}
	
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Usuario getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}
	public List<Usuario> getParticipantes() {
		return participantes;
	}
	public void setParticipantes(List<Usuario> participantes) {
		this.participantes = participantes;
	}
	
	@Override
	public String toString(){
		String s = "Id: "+getId()+"\n"
				+ "Nome: "+getNome()+"\n"
				+ "Descricao: "+getDescricao()+"\n"
				+ "Responsável: "+getResponsavel().getNome()+"\n\n";
		return s;
	}
	
}
