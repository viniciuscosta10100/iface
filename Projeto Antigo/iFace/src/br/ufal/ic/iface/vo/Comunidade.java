package br.ufal.ic.iface.vo;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.iface.interfaces.Serializable;

public class Comunidade implements Serializable {
	
	public Comunidade(){
		init();
	}
	
	public Comunidade(String nome, String descricao){
		this.nome = nome;
		this.descricao = descricao;
		init();
	}
	
	private int id;
	private String nome;
	private String descricao;
	private Usuario responsavel;
	private List<Usuario> participantes;
	
	private void init(){
		this.responsavel = new Usuario();
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
