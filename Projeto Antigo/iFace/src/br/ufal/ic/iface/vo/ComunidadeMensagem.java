package br.ufal.ic.iface.vo;

public class ComunidadeMensagem {

	private int id;
	private String texto;
	private Comunidade comunidade;
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
